package com.example.online.shopping.user.service;

import com.example.online.shopping.dto.OrderStatus;
import com.example.online.shopping.order.dto.OrderPostDto;
import com.example.online.shopping.order.dto.OrderResponseDto;
import com.example.online.shopping.order.entity.Order;
import com.example.online.shopping.order.repository.OrderRepository;
import com.example.online.shopping.order.repository.queryDsl.OrderRepositoryCustom;
import com.example.online.shopping.order.service.OrderService;
import com.example.online.shopping.user.dto.UserResponseDto;
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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;


    @AfterEach
    public void afterEach(){
        userRepository.deleteAll();
    }

    @Test
    void findByUserIdWithQueryDsl(){
        // when
        User user = new User();
        user.setUsername("hong gildong");
        user.setPassword("1234");

        User save = userRepository.save(user);

        // given
        User answer = userRepository.findByUserIdWithQueryDsl(save.getUserId());

        // then
        Assertions.assertThat(answer.getUsername()).isEqualTo("hong gildong");
        Assertions.assertThat(answer.getPassword()).isEqualTo("1234");
    }


    @Test
    void findOrderResponseDtoByUserIdWithQueryDsl(){
        // when
        User user = new User();
        user.setUsername("hong gildong");
        user.setPassword("1234");
        User save = userRepository.save(user);

        OrderPostDto orderPostDto = new OrderPostDto();
        orderPostDto.setUserId(user.getUserId());
        OrderResponseDto responseDto = orderService.post(orderPostDto);

        // given
        List<OrderResponseDto> orderResponseDtos
                = userRepository.findOrderResponseDtoByUserIdWithQueryDsl(save.getUserId());

        // then
        Assertions.assertThat(orderResponseDtos.get(0).getOrderStatus()).isEqualTo(OrderStatus.Processing);

    };

    @Test
    void findUserResponseDtoByUserIdWithQueryDsl(){
        // when
        User user1 = new User();
        user1.setUsername("daniel");
        user1.setPassword("123123");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("james");
        user2.setPassword("1234");
        userRepository.save(user2);

        List<Long> userIds =
                userRepository.findAll().stream().map(user -> user.getUserId()).collect(Collectors.toList());

        // given
        List<UserResponseDto> userResponseDtos = userRepository.findUserResponseDtoByUserIdWithQueryDsl(userIds);

        // then
        Assertions.assertThat(userResponseDtos.get(0).getUsername()).isEqualTo("daniel");
        Assertions.assertThat(userResponseDtos.get(0).getPassword()).isEqualTo("123123");
        Assertions.assertThat(userResponseDtos.get(1).getUsername()).isEqualTo("james");
        Assertions.assertThat(userResponseDtos.get(1).getPassword()).isEqualTo("1234");
    }


    @Test
    void findOrderResponseDtoByUserIdsWithQueryDsl(){
        // when
        User user1 = new User();
        user1.setUsername("james");
        user1.setPassword("12345");
        User save1 = userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("daniel");
        user2.setPassword("1212");
        User save2 = userRepository.save(user2);

        Order order1 = new Order();
        order1.setOrderStatus(OrderStatus.Ordering);
        order1.setUser(save1);
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setOrderStatus(OrderStatus.Delivering);
        order2.setUser(save2);
        orderRepository.save(order2);

        List<Long> userIds =
                userRepository.findAll().stream().map(user -> user.getUserId()).collect(Collectors.toList());

        // given
        List<OrderResponseDto> orderResponseDtos =
                userRepository.findOrderResponseDtoByUserIdsWithQueryDsl(userIds);

        // when
        Assertions.assertThat(orderResponseDtos.get(0).getOrderStatus()).isEqualTo(OrderStatus.Ordering);
        Assertions.assertThat(orderResponseDtos.get(1).getOrderStatus()).isEqualTo(OrderStatus.Delivering);

    }

    @Test
    void findUserResponseDtoByOffsetAndLimitWithQueryDsl(){
        // when
        User user1 = new User();
        user1.setUsername("james");
        user1.setPassword("12345");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUsername("daniel");
        user2.setPassword("1212");
        userRepository.save(user2);

        User user3 = new User();
        user3.setUsername("kim");
        user3.setPassword("123123");
        userRepository.save(user3);

        // given
        List<UserResponseDto> userResponseDtos =
                userRepository.findUserResponseDtoByOffsetAndLimitWithQueryDsl(0, 3);

        // then
        Assertions.assertThat(userResponseDtos.get(0).getUsername()).isEqualTo("james");
        Assertions.assertThat(userResponseDtos.get(0).getPassword()).isEqualTo("12345");
        Assertions.assertThat(userResponseDtos.get(1).getUsername()).isEqualTo("daniel");
        Assertions.assertThat(userResponseDtos.get(1).getPassword()).isEqualTo("1212");
        Assertions.assertThat(userResponseDtos.get(2).getUsername()).isEqualTo("kim");
        Assertions.assertThat(userResponseDtos.get(2).getPassword()).isEqualTo("123123");

    }
}