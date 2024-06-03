package com.example.online.shopping.user.controller;

import com.example.online.shopping.dto.PageRequestDto;
import com.example.online.shopping.exception.BusinessException;
import com.example.online.shopping.user.dto.UserPatchDto;
import com.example.online.shopping.user.dto.UserPostDto;
import com.example.online.shopping.user.dto.UserResponseDto;
import com.example.online.shopping.user.entity.User;
import com.example.online.shopping.user.repository.UserRepository;
import com.example.online.shopping.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class UserControllerTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired(required = false)
    private Pageable pageable;



    @AfterEach
    public void afterEach(){
        userRepository.deleteAll();
    }


    @Test
    void post() throws Exception{
        // when
        UserPostDto userPostDto = new UserPostDto();
        userPostDto.setUsername("hong gil dong");
        userPostDto.setPassword("1234");

        // given
        UserResponseDto userResponseDto = userService.post(userPostDto);

        // then
        assertThat(userResponseDto.getPassword()).isEqualTo("1234");
        assertThat(userResponseDto.getUsername()).isEqualTo("hong gil dong");
    }

    @Test
    void get() throws Exception{
        // when
        UserPostDto userPostDto = new UserPostDto();
        userPostDto.setUsername("kim");
        userPostDto.setPassword("12345");
        UserResponseDto userResponseDto = userService.post(userPostDto);

        // given
        UserResponseDto responseDto = userService.get(userResponseDto.getUserId());

        // then
        assertThat(responseDto.getUsername()).isEqualTo("kim");
        assertThat(responseDto.getPassword()).isEqualTo("12345");

    }

//    @Test
//    void getAll() {
//        // when
//        UserPostDto userPostDto1 = new UserPostDto();
//        userPostDto1.setUsername("hong gil dong");
//        userPostDto1.setPassword("1234");
//
//        UserResponseDto userResponseDto1 = userService.post(userPostDto1);
//
//        UserPostDto userPostDto2 = new UserPostDto();
//        userPostDto2.setUsername("kim");
//        userPostDto2.setPassword("12345");
//        UserResponseDto userResponseDto2 = userService.post(userPostDto2);
//
//        // given
//        PageRequestDto pageRequestDto = userService.getAll(pageable);
//
//
//        // then
//
//        System.out.println("pageRequestDto = " +pageRequestDto.getData().getClass().getName());

//    }

    @Test
    void gets() throws Exception{
        // when
        User user1 = new User();
        user1.setUsername("james");
        user1.setPassword("12345");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("daniel");
        user2.setPassword("1212");
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("kim");
        user3.setPassword("123123");
        userRepository.save(user3);

        // given
        List<UserResponseDto> userResponseDtos = userService.gets(0, 3);

        // then
        Assertions.assertThat(userResponseDtos.get(0).getUsername()).isEqualTo("james");
        Assertions.assertThat(userResponseDtos.get(0).getPassword()).isEqualTo("12345");
        Assertions.assertThat(userResponseDtos.get(1).getUsername()).isEqualTo("daniel");
        Assertions.assertThat(userResponseDtos.get(1).getPassword()).isEqualTo("1212");
        Assertions.assertThat(userResponseDtos.get(2).getUsername()).isEqualTo("kim");
        Assertions.assertThat(userResponseDtos.get(2).getPassword()).isEqualTo("123123");
    }

    @Test
    void patch() throws Exception{
        // when
        User user = new User();
        user.setUsername("james");
        user.setPassword("1234");

        User save = userRepository.save(user);

        UserPatchDto userPatchDto = new UserPatchDto();
        userPatchDto.setUsername("hong gildong");
        userPatchDto.setPassword("123123");

        // given
        UserResponseDto userResponseDto = userService.patch(userPatchDto, save.getUserId());

        // then
        Assertions.assertThat(userResponseDto.getUsername()).isEqualTo("hong gildong");
        Assertions.assertThat(userResponseDto.getPassword()).isEqualTo("123123");
    }

    @Test
    void delete() throws Exception{
        // when
        User user = new User();
        user.setUsername("james");
        user.setPassword("1234");

        User save = userRepository.save(user);
        // given
        userService.delete(save.getUserId());


        // then
        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
                () -> userService.verifiedUser(save.getUserId()));
    }

}