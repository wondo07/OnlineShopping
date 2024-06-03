package com.example.online.shopping.orderItem.controller;

import com.example.online.shopping.dto.OrderStatus;
import com.example.online.shopping.exception.BusinessException;
import com.example.online.shopping.item.entity.Item;
import com.example.online.shopping.item.repository.ItemRepository;
import com.example.online.shopping.order.entity.Order;
import com.example.online.shopping.order.repository.OrderRepository;
import com.example.online.shopping.orderItem.dto.OrderItemPatchDto;
import com.example.online.shopping.orderItem.dto.OrderItemPostDto;
import com.example.online.shopping.orderItem.dto.OrderItemResponseDto;
import com.example.online.shopping.orderItem.entity.OrderItem;
import com.example.online.shopping.orderItem.repository.OrderItemRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class OrderItemControllerTest {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void afterEach(){
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    void post(){
        // when
        User user = new User();
        User savedUser = userRepository.save(user);

        Order order = new Order();
        savedUser.addOrder(order);
        order.setOrderStatus(OrderStatus.Processing);

        Order savedOrder = orderRepository.save(order);

        Item item = new Item();
        item.setStock(5);
        item.setName("book");
        item.setPrice(5000);
        Item savedItem = itemRepository.save(item);

        OrderItemPostDto orderItemPostDto = new OrderItemPostDto();
        orderItemPostDto.setCount(5);
        orderItemPostDto.setName("book");
        orderItemPostDto.setOrderId(savedOrder.getOrderId());
        orderItemPostDto.setItemId(savedItem.getItemId());

        // give
        OrderItemResponseDto orderItemResponseDto = orderItemService.post(orderItemPostDto);

        // then
        assertThat(orderItemResponseDto.getName()).isEqualTo("book");
        assertThat(orderItemResponseDto.getCount()).isEqualTo(5);
    }

    @Test
    void get(){
        // when
        User user = new User();
        User savedUser = userRepository.save(user);

        Order order = new Order();
        savedUser.addOrder(order);
        order.setOrderStatus(OrderStatus.Processing);
        Order savedOrder = orderRepository.save(order);

        Item item = new Item();
        item.setStock(5);
        item.setName("book");
        item.setPrice(5000);
        Item savedItem = itemRepository.save(item);

        OrderItemPostDto orderItemPostDto = new OrderItemPostDto();
        orderItemPostDto.setCount(5);
        orderItemPostDto.setName("book");
        orderItemPostDto.setOrderId(savedOrder.getOrderId());
        orderItemPostDto.setItemId(savedItem.getItemId());

        OrderItemResponseDto orderItemResponseDto = orderItemService.post(orderItemPostDto);


        // give
        OrderItemResponseDto responseDto = orderItemService.get(orderItemResponseDto.getOrderItemId());

        // then
        assertThat(orderItemResponseDto.getName()).isEqualTo("book");
        assertThat(orderItemResponseDto.getCount()).isEqualTo(5);
    }

    @Test
    void patch(){
        // when
        User user = new User();
        User savedUser = userRepository.save(user);

        Order order = new Order();
        savedUser.addOrder(order);
        order.setOrderStatus(OrderStatus.Processing);
        Order savedOrder = orderRepository.save(order);

        Item item1 = new Item();
        item1.setStock(5);
        item1.setName("book");
        item1.setPrice(5000);
        Item savedItem1 = itemRepository.save(item1);

        Item item2 = new Item();
        item2.setStock(8);
        item2.setName("pen");
        item2.setPrice(2000);
        Item savedItem2 = itemRepository.save(item2);

        OrderItemPostDto orderItemPostDto = new OrderItemPostDto();
        orderItemPostDto.setCount(5);
        orderItemPostDto.setName("book");
        orderItemPostDto.setOrderId(savedOrder.getOrderId());
        orderItemPostDto.setItemId(savedItem1.getItemId());

        OrderItemResponseDto orderItemResponseDto = orderItemService.post(orderItemPostDto);


        OrderItemPatchDto orderItemPatchDto = new OrderItemPatchDto();
        orderItemPatchDto.setCount(3);
        orderItemPatchDto.setName("pen");
        orderItemPatchDto.setItemId(savedItem2.getItemId());
        orderItemPatchDto.setOrderId(savedOrder.getOrderId());


        // give
        OrderItemResponseDto responseDto =
                orderItemService.patch(orderItemPatchDto, orderItemResponseDto.getOrderItemId());


        // then
        assertThat(responseDto.getCount()).isEqualTo(3);
        assertThat(responseDto.getName()).isEqualTo("pen");
    }

    @Test
    void delete() throws Exception{
        // when
        User user = new User();
        User savedUser = userRepository.save(user);

        Order order = new Order();
        savedUser.addOrder(order);
        order.setOrderStatus(OrderStatus.Processing);
        Order savedOrder = orderRepository.save(order);

        Item item = new Item();
        item.setStock(5);
        item.setName("book");
        item.setPrice(5000);
        Item savedItem = itemRepository.save(item);

        OrderItemPostDto orderItemPostDto = new OrderItemPostDto();
        orderItemPostDto.setCount(5);
        orderItemPostDto.setName("book");
        orderItemPostDto.setOrderId(savedOrder.getOrderId());
        orderItemPostDto.setItemId(savedItem.getItemId());

        OrderItemResponseDto orderItemResponseDto = orderItemService.post(orderItemPostDto);

        // give
        orderItemService.delete(orderItemResponseDto.getOrderItemId());

//        orderItemRepository.deleteById(orderItemResponseDto.getOrderItemId());
        // then
        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
                () -> orderItemService.verifiedOrderItem(orderItemResponseDto.getOrderItemId()));
    }
}