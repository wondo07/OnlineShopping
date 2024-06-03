package com.example.online.shopping.orderItem.repository.queryDsl;

import com.example.online.shopping.orderItem.dto.OrderItemResponseDto;
import com.example.online.shopping.orderItem.entity.OrderItem;

import java.util.List;

public interface OrderItemRepositoryCustom {


    OrderItem findByOrderItemIdWithQueryDsl(Long orderItemId);

    List<OrderItemResponseDto> findOrderItemResponseDtoByOrderItemIdsWithQueryDsl(List<Long> orderItemIds);

    List<OrderItemResponseDto> findOrderItemResponseDtoByOffsetAndLimitWithQueryDsl(int offset, int limit);
}
