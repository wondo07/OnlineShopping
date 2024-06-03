package com.example.online.shopping.order.repository.queryDsl;

import com.example.online.shopping.order.dto.OrderResponseDto;
import com.example.online.shopping.order.entity.Order;
import com.example.online.shopping.orderItem.dto.OrderItemResponseDto;

import java.util.List;

public interface OrderRepositoryCustom {

    OrderResponseDto findResponseDtoByOrderIdWithQueryDsl(Long orderId);

    List<OrderResponseDto> findResponseDtosByOrderIdWithQueryDsl(List<Long> orderIds);

    Order findByOrderIdWithQueryDsl(Long orderId);

    List<OrderItemResponseDto> findOrderItemResponseDtosByOrderIdsWithQueryDsl(List<Long> orderIds);

    List<OrderResponseDto> findOrderResponseDtosByOffsetAndLimitWithQueryDsl(int offset, int limit);
}
