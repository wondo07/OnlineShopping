package com.example.online.shopping.order.controller;

import com.example.online.shopping.dto.OrderStatus;
import com.example.online.shopping.exception.BusinessException;
import com.example.online.shopping.order.dto.OrderPatchDto;
import com.example.online.shopping.order.dto.OrderPostDto;
import com.example.online.shopping.order.dto.OrderResponseDto;
import com.example.online.shopping.order.entity.Order;
import com.example.online.shopping.order.repository.OrderRepository;
import com.example.online.shopping.order.service.OrderService;
import com.example.online.shopping.orderItem.service.OrderItemService;
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


@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class OrderControllerTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemService orderItemService;

//    @Autowired
//    private Pageable pageable;


    @AfterEach
    void afterEach(){
        orderRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void post() throws Exception{
        // when
        User user = new User();
        user.setUsername("daniel");
        user.setPassword("1234");
        User save = userRepository.save(user);

        OrderPostDto orderPostDto = new OrderPostDto();
        orderPostDto.setUserId(save.getUserId());

        // given
        OrderResponseDto orderResponseDto = orderService.post(orderPostDto);

        // then
        Assertions.assertThat(orderResponseDto.getOrderStatus()).isEqualTo(OrderStatus.Processing);

    }


    @Test
    void get() throws Exception{
        // when
        User user = new User();
        user.setUsername("daniel");
        user.setPassword("1234");
        userRepository.save(user);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.Ordering);
        user.addOrder(order);
        Order save = orderRepository.save(order);

        // given
        OrderResponseDto orderResponseDto = orderService.get(save.getOrderId());

        // then
        Assertions.assertThat(orderResponseDto.getOrderStatus()).isEqualTo(OrderStatus.Ordering);
    }


    @Test
    void getAll() throws Exception{
        // when

        // given

        // then
    }

//    @Test
//    void gets(){
//        // when
//        User user1 = new User();
//        user1.setUsername("daniel");
//        user1.setPassword("1234");
//        userRepository.save(user1);
//
//        User user2 = new User();
//        user2.setUsername("kim");
//        user2.setPassword("1212");
//        userRepository.save(user2);
//
//        Order order1 = new Order();
//        order1.setOrderStatus(OrderStatus.Ordering);
//        user1.addOrder(order1);
//        Order save = orderRepository.save(order1);
//
//        Order order2 = new Order();
//        order2.setOrderStatus(OrderStatus.Delivering);
//        user2.addOrder(order2);
//        orderRepository.save(order2);
//
//        // given
//        PageRequestDto pageRequestDto = orderService.gets(0, 2, pageable);
//
//        // then
//
//    }

    @Test
    void patch() throws Exception{
        // when
        User user = new User();
        user.setUsername("daniel");
        user.setPassword("1234");
        userRepository.save(user);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.Ordering);
        user.addOrder(order);
        Order save = orderRepository.save(order);

        OrderPatchDto orderPatchDto = new OrderPatchDto();
        orderPatchDto.setOrderStatus(OrderStatus.Delivering);

        // given
        OrderResponseDto orderResponseDto = orderService.patch(orderPatchDto, save.getOrderId());

        // then
        Assertions.assertThat(orderResponseDto.getOrderStatus()).isEqualTo(OrderStatus.Delivering);
    }

    @Test
    void delete() throws Exception{
        // when
        User user = new User();
        user.setUsername("daniel");
        user.setPassword("1234");
        userRepository.save(user);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.Ordering);
        user.addOrder(order);
        Order save = orderRepository.save(order);

        // given
        orderService.delete(save.getOrderId());

        // then
        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
                () -> orderService.verifiedOrder(save.getOrderId()));
    }
}