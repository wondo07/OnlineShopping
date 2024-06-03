package com.example.online.shopping.user.service;

import com.example.online.shopping.user.dto.CustomUserDetail;
import com.example.online.shopping.user.entity.User;
import com.example.online.shopping.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);

        if(user == null) return null;

        return new CustomUserDetail(user);
    }
}
