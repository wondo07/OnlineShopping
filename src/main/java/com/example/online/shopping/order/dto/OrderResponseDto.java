package com.example.online.shopping.order.dto;

import com.example.online.shopping.dto.OrderStatus;
import com.example.online.shopping.orderItem.dto.OrderItemResponseDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderResponseDto {

    private Long orderId;

    private OrderStatus orderStatus;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    @JsonIgnore
    private Long userId;

    private List<OrderItemResponseDto> orderItemResponseDtos = new ArrayList<>();

}
