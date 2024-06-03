package com.example.online.shopping.jwt;

import com.example.online.shopping.refresh.RefreshEntity;
import com.example.online.shopping.refresh.RefreshRepository;
import com.example.online.shopping.user.dto.UserLoginPost;
import com.example.online.shopping.user.dto.UserPostDto;
import com.example.online.shopping.user.entity.User;
import com.example.online.shopping.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final RefreshRepository refreshRepository;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException{

        UserPostDto userPostDto;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            userPostDto = objectMapper.readValue(messageBody, UserPostDto.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!userRepository.existsByUsername(userPostDto.getUsername())) return null;

        User user = userRepository.findByUsername(userPostDto.getUsername());

        String username = user.getUsername();
        String password = user.getPassword();


//        String username = obtainUsername(request);
//        String password = obtainPassword(request);

        System.out.println("username = " + username);
        System.out.println("password = " + password);


        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);

    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                         FilterChain filterChain, Authentication authentication){

        String username = authentication.getName();
        System.out.println("username1 = " + username);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();

        String access = jwtUtil.createJwt(username, role, "access", 86000L);
        String refresh = jwtUtil.createJwt(username, role, "refresh", 100000L);

        addRefresh(refresh, username, 8600000L);

        response.addCookie(addCookie("refresh", refresh));
        response.setHeader("access", access);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed){
        response.setStatus(401);
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

//    @Override
//    public String toString() {
//        return ToStringBuilder
//                .reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
//    }
}
