package com.example.online.shopping.order.repository;

import com.example.online.shopping.order.entity.Order;
import com.example.online.shopping.order.repository.queryDsl.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> , OrderRepositoryCustom {


}
