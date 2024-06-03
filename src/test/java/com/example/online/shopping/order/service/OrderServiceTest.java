package com.example.online.shopping.order.service;

import com.example.online.shopping.dto.OrderStatus;
import com.example.online.shopping.order.dto.OrderResponseDto;
import com.example.online.shopping.order.entity.Order;
import com.example.online.shopping.order.repository.OrderRepository;
import com.example.online.shopping.orderItem.dto.OrderItemResponseDto;
import com.example.online.shopping.orderItem.entity.OrderItem;
import com.example.online.shopping.orderItem.repository.OrderItemRepository;
import com.example.online.shopping.user.entity.User;
import com.example.online.shopping.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class OrderServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @AfterEach
    void afterEach(){
        userRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    public void findResponseDtoByOrderIdWithQueryDsl() throws Exception{
        // when
        Order order = new Order();
        order.setOrderStatus(OrderStatus.Processing);
        Order save = orderRepository.save(order);

        // given
        OrderResponseDto orderResponseDto = orderRepository.findResponseDtoByOrderIdWithQueryDsl(save.getOrderId());

        // then
        assertThat(orderResponseDto.getOrderStatus()).isEqualTo(OrderStatus.Processing);
    }

    @Test
    void findResponseDtosByOrderIdWithQueryDsl() throws Exception{
        // when
        Order order1 = new Order();
        order1.setOrderStatus(OrderStatus.Processing);
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.Ordering);
        orderRepository.save(order2);

        List<Long> orderIds =
                orderRepository.findAll().stream().map(order -> order.getOrderId()).collect(Collectors.toList());

        // given
        List<OrderResponseDto> orderResponseDtos = orderRepository.findResponseDtosByOrderIdWithQueryDsl(orderIds);

        // then
        assertThat(orderResponseDtos.get(0).getOrderStatus()).isEqualTo(OrderStatus.Processing);
        assertThat(orderResponseDtos.get(1).getOrderStatus()).isEqualTo(OrderStatus.Ordering);
    }

    @Test
    void findByOrderIdWithQueryDsl() throws Exception{
        // when
        User user = new User();
        userRepository.save(user);

        Order order = new Order();
        user.addOrder(order);

        order.setOrderStatus(OrderStatus.Processing);
        Order save = orderRepository.save(order);
        System.out.println("save.getOrderStatus() = " + save.getOrderStatus());
        System.out.println("save.getOrderId() = " + save.getOrderId());

        // given
        Order verifiedOrder = orderRepository.findByOrderIdWithQueryDsl(save.getOrderId());

        // then
        assertThat(verifiedOrder.getOrderStatus()).isEqualTo(OrderStatus.Processing);
    }

    @Test
    void findOrderItemResponseDtosByOrderIdWithQueryDsl() throws Exception{
        // when
        Order order1 = new Order();
        order1.setOrderStatus(OrderStatus.Processing);
        Order save1 = orderRepository.save(order1);

        Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.Delivering);
        Order save2 = orderRepository.save(order2);

        OrderItem orderItem1 = new OrderItem();
        orderItem1.setOrder(save1);
        orderItem1.setCount(1);
        orderItem1.setName("book");
        orderItem1.setTotalPrice(5000);

        orderItemRepository.save(orderItem1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrder(save2);
        orderItem2.setCount(3);
        orderItem2.setName("pen");
        orderItem2.setTotalPrice(3000);

        orderItemRepository.save(orderItem2);

        List<Long> orderIds =
                orderRepository.findAll().stream().map(order -> order.getOrderId()).collect(Collectors.toList());

        // given
        List<OrderItemResponseDto> orderItemResponseDtos =
                orderRepository.findOrderItemResponseDtosByOrderIdsWithQueryDsl(orderIds);

        // then
        assertThat(orderItemResponseDtos.get(0).getName()).isEqualTo("book");
        assertThat(orderItemResponseDtos.get(0).getCount()).isEqualTo(1);
        assertThat(orderItemResponseDtos.get(0).getTotalPrice()).isEqualTo(5000);
        assertThat(orderItemResponseDtos.get(1).getName()).isEqualTo("pen");
        assertThat(orderItemResponseDtos.get(1).getCount()).isEqualTo(3);
        assertThat(orderItemResponseDtos.get(1).getTotalPrice()).isEqualTo(3000);
    }

    @Test
    void findOrderResponseDtosByOffsetAndLimitWithQueryDsl() throws Exception{
        // when
        Order order1 = new Order();
        order1.setOrderStatus(OrderStatus.Processing);
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.Delivering);
        orderRepository.save(order2);

        Order order3 = new Order();
        order3.setOrderStatus(OrderStatus.Ordering);
        orderRepository.save(order3);

        // given
        List<OrderResponseDto> orderResponseDtos =
                orderRepository.findOrderResponseDtosByOffsetAndLimitWithQueryDsl(0, 3);

        // then
        assertThat(orderResponseDtos.get(0).getOrderStatus()).isEqualTo(OrderStatus.Processing);
        assertThat(orderResponseDtos.get(1).getOrderStatus()).isEqualTo(OrderStatus.Delivering);
        assertThat(orderResponseDtos.get(2).getOrderStatus()).isEqualTo(OrderStatus.Ordering);
    }
}