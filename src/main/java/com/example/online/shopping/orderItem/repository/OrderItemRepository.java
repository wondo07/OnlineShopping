package com.example.online.shopping.orderItem.repository;

import com.example.online.shopping.orderItem.entity.OrderItem;
import com.example.online.shopping.orderItem.repository.queryDsl.OrderItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, OrderItemRepositoryCustom {
}
