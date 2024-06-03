package com.example.online.shopping.user.service;

import com.example.online.shopping.dto.PageRequestDto;
import com.example.online.shopping.exception.BusinessException;
import com.example.online.shopping.exception.ErrorCode;
import com.example.online.shopping.jwt.JwtUtil;
import com.example.online.shopping.order.dto.OrderResponseDto;
import com.example.online.shopping.order.service.OrderService;
import com.example.online.shopping.refresh.RefreshEntity;
import com.example.online.shopping.refresh.RefreshRepository;
import com.example.online.shopping.user.dto.UserPatchDto;
import com.example.online.shopping.user.dto.UserPostDto;
import com.example.online.shopping.user.dto.UserResponseDto;
import com.example.online.shopping.user.entity.User;
import com.example.online.shopping.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RefreshRepository refreshRepository;

    private final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserResponseDto post(UserPostDto userPostDto) {
        User user = toEntity(userPostDto);
        User save = userRepository.save(user);
        return toResponseDto(save);
    }

    public UserResponseDto get(Long userId) {
        User user = verifiedUserWithQueryDsl(userId);
        return toResponseDto(user);
    }

    public PageRequestDto getAll(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        List<User> users = userPage.getContent();

        List<Long> userIds = users.stream().map(user -> user.getUserId()).collect(Collectors.toList());

        List<UserResponseDto> userResponseDtos = userRepository.findUserResponseDtoByUserIdWithQueryDsl(userIds);

        List<OrderResponseDto> orderResponseDtos = userRepository.findOrderResponseDtoByUserIdsWithQueryDsl(userIds);

        Map<Long, List<OrderResponseDto>> orderResponseDtoMap = orderResponseDtos.stream().collect(Collectors.groupingBy(orderResponseDto -> orderResponseDto.getUserId()));

        userResponseDtos.forEach(
                userResponseDto -> userResponseDto.setOrderResponseDtos(orderResponseDtoMap.get(userResponseDto.getUserId()))
        );



        return PageRequestDto.of(userResponseDtos,
                new PageImpl(userResponseDtos,
                        userPage.getPageable(),
                        userPage.getTotalElements()));
    }

    public List<UserResponseDto> gets(int offset, int limit) {

        List<UserResponseDto> userResponseDtos =
                userRepository.findUserResponseDtoByOffsetAndLimitWithQueryDsl(offset, limit);

        List<Long> userIds =
                userResponseDtos.stream().map(userResponseDto -> userResponseDto.getUserId()).collect(Collectors.toList());

        List<OrderResponseDto> orderResponseDtos = userRepository.findOrderResponseDtoByUserIdsWithQueryDsl(userIds);

        Map<Long, List<OrderResponseDto>> orderMap =
                orderResponseDtos.stream().collect(Collectors.groupingBy(o -> o.getUserId()));

        userResponseDtos.
                forEach(userResponseDto -> userResponseDto.setOrderResponseDtos(orderMap.get(userResponseDto.getUserId())));

        return userResponseDtos;
    }

    public UserResponseDto patch(UserPatchDto userPatchDto, Long userId) {
        User user = verifiedUserWithQueryDsl(userId);

        Optional.ofNullable(userPatchDto.getPassword())
                .ifPresent(password -> user.setPassword(password));

        Optional.ofNullable(userPatchDto.getUsername())
                .ifPresent(username -> user.setUsername(username));

        return toResponseDto(userRepository.save(user));
    }

    public void delete(Long userId) {

        userRepository.deleteById(userId);

    }

    public User verifiedUser(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    public User verifiedUserWithQueryDsl(Long userId){
        User user = userRepository.findByUserIdWithQueryDsl(userId);
        if(user == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        return user;
    }

    public User toEntity(UserPostDto userPostDto){
        User user = new User();
        user.setUsername(userPostDto.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(userPostDto.getPassword()));
        user.setRole("ROLE_USER");
        return user;
    }

    public UserResponseDto toResponseDto(User user){
        return new UserResponseDto(user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                userRepository.findOrderResponseDtoByUserIdWithQueryDsl(user.getUserId()));
    }

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String refresh = null;

        Cookie[] cookies = request.getCookies();

        for(Cookie cookie : cookies){
            if(cookie.getName().equals("refresh")){
                refresh = cookie.getValue();
            }
        }

        if(refresh == null){
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e){
            return new ResponseEntity("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtUtil.getCategory("refresh");

        if(!category.equals("refresh")){
            return new ResponseEntity("refresh token invalid", HttpStatus.BAD_REQUEST);
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if(!isExist){
            return new ResponseEntity("refresh token invalid", HttpStatus.BAD_REQUEST);
        }


        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        refreshRepository.deleteByRefresh(refresh);

        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        addRefresh(newRefresh, username, 86400000L);

        response.setHeader("access", newAccess);
        response.addCookie(addCookie("refresh",newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);

    }

    public void addRefresh(String refresh, String username, Long expiredMs){

        if(!refreshRepository.existsByRefresh(refresh)) return ;

        RefreshEntity refreshEntity = new RefreshEntity();
        refreshEntity.setRefresh(refresh);
        refreshEntity.setUsername(username);
        refreshEntity.setExpiredMs(expiredMs);

        refreshRepository.save(refreshEntity);
    }

    public Cookie addCookie(String key, String value){
        Cookie cookie = new Cookie(key, value);

        cookie.setHttpOnly(true);
        cookie.setMaxAge(24*60*60);

        return cookie;
    }

}
