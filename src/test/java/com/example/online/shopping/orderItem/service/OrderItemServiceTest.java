package com.example.online.shopping.orderItem.service;

import com.example.online.shopping.dto.OrderStatus;
import com.example.online.shopping.item.entity.Item;
import com.example.online.shopping.item.repository.ItemRepository;
import com.example.online.shopping.order.entity.Order;
import com.example.online.shopping.order.repository.OrderRepository;
import com.example.online.shopping.orderItem.dto.OrderItemResponseDto;
import com.example.online.shopping.orderItem.entity.OrderItem;
import com.example.online.shopping.orderItem.repository.OrderItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class OrderItemServiceTest {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findByOrderItemIdWithQueryDsl() throws Exception{
        // when
        Order order = new Order();
        order.setOrderStatus(OrderStatus.Processing);
        Order savedOrder = orderRepository.save(order);

        Item item = new Item();
        item.setStock(5);
        item.setName("book");
        item.setPrice(5000);
        Item savedItem = itemRepository.save(item);

        OrderItem orderItem = new OrderItem();
        orderItem.setName("book");
        orderItem.setCount(1);
        orderItem.setTotalPrice(5000);
        savedOrder.addOrderItem(orderItem);
        savedItem.addOrderItem(orderItem);
        OrderItem save = orderItemRepository.save(orderItem);

        // given
        OrderItem verifiedOrderItem = orderItemRepository.findByOrderItemIdWithQueryDsl(save.getOrderItemId());

        //then
        assertThat(verifiedOrderItem.getName()).isEqualTo("book");
        assertThat(verifiedOrderItem.getCount()).isEqualTo(1);
        assertThat(verifiedOrderItem.getTotalPrice()).isEqualTo(5000);
    }

    @Test
    void findOrderItemResponseDtoByOrderItemIdsWithQueryDsl() throws Exception{
        // when
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("book");
        orderItem1.setCount(1);
        orderItem1.setTotalPrice(5000);
        orderItemRepository.save(orderItem1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setName("pen");
        orderItem2.setCount(2);
        orderItem2.setTotalPrice(2000);
        orderItemRepository.save(orderItem2);

        List<Long> orderItemIds =
                orderItemRepository.findAll().stream().map(orderItem -> orderItem.getOrderItemId()).collect(Collectors.toList());
        // given
        List<OrderItemResponseDto> orderItemResponseDtos =
                orderItemRepository.findOrderItemResponseDtoByOrderItemIdsWithQueryDsl(orderItemIds);

        // then
        assertThat(orderItemResponseDtos.get(0).getName()).isEqualTo("book");
        assertThat(orderItemResponseDtos.get(0).getCount()).isEqualTo(1);
        assertThat(orderItemResponseDtos.get(0).getTotalPrice()).isEqualTo(5000);
        assertThat(orderItemResponseDtos.get(1).getName()).isEqualTo("pen");
        assertThat(orderItemResponseDtos.get(1).getCount()).isEqualTo(2);
        assertThat(orderItemResponseDtos.get(1).getTotalPrice()).isEqualTo(2000);
    }

    @Test
    void findOrderItemResponseDtoByOffsetAndLimitWithQueryDsl() throws Exception{
        // when
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setName("book");
        orderItem1.setCount(1);
        orderItem1.setTotalPrice(5000);
        orderItemRepository.save(orderItem1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setName("pen");
        orderItem2.setCount(2);
        orderItem2.setTotalPrice(2000);
        orderItemRepository.save(orderItem2);

        OrderItem orderItem3 = new OrderItem();
        orderItem3.setName("eraser");
        orderItem3.setCount(3);
        orderItem3.setTotalPrice(1500);
        orderItemRepository.save(orderItem3);

        // given
        List<OrderItemResponseDto> orderItemResponseDtos =
                orderItemRepository.findOrderItemResponseDtoByOffsetAndLimitWithQueryDsl(1, 2);

        // then
        assertThat(orderItemResponseDtos.get(0).getName()).isEqualTo("pen");
        assertThat(orderItemResponseDtos.get(0).getCount()).isEqualTo(2);
        assertThat(orderItemResponseDtos.get(0).getTotalPrice()).isEqualTo(2000);
        assertThat(orderItemResponseDtos.get(1).getName()).isEqualTo("eraser");
        assertThat(orderItemResponseDtos.get(1).getCount()).isEqualTo(3);
        assertThat(orderItemResponseDtos.get(1).getTotalPrice()).isEqualTo(1500);
    }
}