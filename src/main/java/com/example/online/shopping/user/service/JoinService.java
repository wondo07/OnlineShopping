package com.example.online.shopping.user.service;

import com.example.online.shopping.user.dto.UserLoginPost;
import com.example.online.shopping.user.dto.UserPostDto;
import com.example.online.shopping.user.dto.UserResponseDto;
import com.example.online.shopping.user.entity.User;
import com.example.online.shopping.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JoinService {

    private final UserRepository userRepository;


    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserResponseDto LoginProcess(UserLoginPost userLoginPost) {


        //db에 이미 동일한 username을 가진 회원이 존재하는지?
        if(!userRepository.existsByUsername(userLoginPost.getUsername())) return null;

        User data = userRepository.findByUsername(userLoginPost.getUsername());

        data.setUsername(userLoginPost.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(userLoginPost.getPassword()));
        if (data.getRole() == null){
            data.setRole("ROLE_USER");
        }


        return toResponseDto(userRepository.save(data));
    }

    public UserResponseDto toResponseDto(User user){
        return new UserResponseDto(user.getUserId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                userRepository.findOrderResponseDtoByUserIdWithQueryDsl(user.getUserId()));
    }

}
