package com.example.online.shopping.user.repository.queryDsl;

import com.example.online.shopping.order.dto.OrderResponseDto;
import com.example.online.shopping.user.dto.UserResponseDto;
import com.example.online.shopping.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {

    User findByUserIdWithQueryDsl(Long userId);

    List<OrderResponseDto> findOrderResponseDtoByUserIdWithQueryDsl(Long userId);

    List<UserResponseDto> findUserResponseDtoByUserIdWithQueryDsl(List<Long> userIds);

    List<OrderResponseDto> findOrderResponseDtoByUserIdsWithQueryDsl(List<Long> userIds);

    List<UserResponseDto> findUserResponseDtoByOffsetAndLimitWithQueryDsl(int offset, int limit);

//    User findByUsername(String username);

}
