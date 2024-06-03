package com.example.online.shopping.item.entity;

import com.example.online.shopping.orderItem.entity.OrderItem;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column
    @Setter
    private String name;

    @Column
    @Setter
    private int price;

    @Column
    @Setter
    private int stock;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "item")
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setItem(this);
    }

}
