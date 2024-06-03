package com.example.online.shopping.orderItem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemPostDto {

    private Long orderId;

    private Long itemId;

    private String name;

    private int count;

}
