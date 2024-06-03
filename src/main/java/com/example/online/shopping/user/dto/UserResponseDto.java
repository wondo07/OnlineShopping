package com.example.online.shopping.user.dto;

import com.example.online.shopping.order.dto.OrderResponseDto;
import com.example.online.shopping.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {

    @JsonIgnore
    private Long userId;

    private String username;

    private String password;

    private String role;

    private List<OrderResponseDto> orderResponseDtos = new ArrayList<>();
}
