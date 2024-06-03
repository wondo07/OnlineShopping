package com.example.online.shopping.orderItem.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemResponseDto {

    @JsonIgnore
    private Long orderId;

    @JsonIgnore
    private Long orderItemId;

    private Long itemId;

    private String name;

    private int count;

    private int totalPrice;
}
