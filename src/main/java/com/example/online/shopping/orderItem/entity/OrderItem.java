package com.example.online.shopping.orderItem.entity;

import com.example.online.shopping.item.entity.Item;
import com.example.online.shopping.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "orderItem")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @Column
    @Setter
    private int totalPrice;

    @Column
    @Setter
    private String name;

    @Column
    @Setter
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "orderId")
    @Setter
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "itemId")
    @Setter
    private Item item;
}
