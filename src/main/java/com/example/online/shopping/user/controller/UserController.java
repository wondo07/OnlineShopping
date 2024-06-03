package com.example.online.shopping.user.controller;

import com.example.online.shopping.dto.PageRequestDto;
import com.example.online.shopping.user.dto.UserLoginPost;
import com.example.online.shopping.user.dto.UserPatchDto;
import com.example.online.shopping.user.dto.UserPostDto;
import com.example.online.shopping.user.dto.UserResponseDto;
import com.example.online.shopping.user.entity.User;
import com.example.online.shopping.user.service.JoinService;
import com.example.online.shopping.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final JoinService joinService;

//    @PostMapping("/login")
//    private ResponseEntity login(@RequestBody UserLoginPost userLoginPost){
//        UserResponseDto userResponseDto = joinService.LoginProcess(userLoginPost);
//        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
//
//    }

    @PostMapping
    private ResponseEntity post(@RequestBody UserPostDto userPostDto){

        UserResponseDto userResponseDto = userService.post(userPostDto);

        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    private ResponseEntity get(@PathVariable("userId") Long userId){
        UserResponseDto userResponseDto = userService.get(userId);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    private ResponseEntity getAll(Pageable pageable){
        PageRequestDto pageRequestDto = userService.getAll(pageable);

        System.out.println("pageRequestDto.getData() = " + pageRequestDto.getData());
        System.out.println("length() = " + pageRequestDto.getData().toString().length());
        return new ResponseEntity<>(pageRequestDto, HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity gets(@RequestParam("offset") int offset,
                                @RequestParam("limit") int limit){
       List<UserResponseDto> userResponseDtos= userService.gets(offset, limit);
        return new ResponseEntity<>(userResponseDtos, HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    private ResponseEntity patch(@RequestBody UserPatchDto userPatchDto,
                                 @PathVariable("userId") Long userId){

        UserResponseDto userResponseDto = userService.patch(userPatchDto, userId);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    private ResponseEntity delete(@PathVariable("userId") Long userId){
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        return userService.reissue(request, response);

    }
}
