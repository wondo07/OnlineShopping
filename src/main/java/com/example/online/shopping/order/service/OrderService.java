package com.example.online.shopping.order.service;

import com.example.online.shopping.dto.OrderStatus;
import com.example.online.shopping.dto.PageRequestDto;
import com.example.online.shopping.exception.BusinessException;
import com.example.online.shopping.exception.ErrorCode;
import com.example.online.shopping.order.dto.OrderPatchDto;
import com.example.online.shopping.order.dto.OrderPostDto;
import com.example.online.shopping.order.dto.OrderResponseDto;
import com.example.online.shopping.order.entity.Order;
import com.example.online.shopping.order.repository.OrderRepository;
import com.example.online.shopping.orderItem.dto.OrderItemResponseDto;
import com.example.online.shopping.user.entity.User;
import com.example.online.shopping.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;


    public OrderResponseDto post(OrderPostDto orderPostDto) {
        User user = userService.verifiedUser(orderPostDto.getUserId());
        Order order = toEntity(orderPostDto);
        order.setOrderStatus(OrderStatus.Processing);
        user.addOrder(order);

        Order save = orderRepository.save(order);
        return toResponseDto(save);
    }

    public OrderResponseDto get(Long orderId) {
        Order order = verifiedByOrderIdWithQueryDsl(orderId);
        return toResponseDto(order);
    }

    public PageRequestDto getAll(Pageable pageable) {

        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<Order> orders = orderPage.getContent();

        List<Long> orderIds =
                orders.stream().map(order -> order.getOrderId()).collect(Collectors.toList());

        List<OrderResponseDto> orderResponseDtos = orderRepository.findResponseDtosByOrderIdWithQueryDsl(orderIds);

        List<OrderItemResponseDto> orderItemResponseDtos =
                orderRepository.findOrderItemResponseDtosByOrderIdsWithQueryDsl(orderIds);

        Map<Long, List<OrderItemResponseDto>> orderItemMap =
                orderItemResponseDtos.stream().collect(
                        Collectors.groupingBy(orderItemResponseDto -> orderItemResponseDto.getOrderId()));

        orderResponseDtos.forEach(
                orderResponseDto ->
                        orderResponseDto.setOrderItemResponseDtos(orderItemMap.get(orderResponseDto.getOrderId())));

        return PageRequestDto.of(orderResponseDtos,
                new PageImpl(orderResponseDtos,
                        orderPage.getPageable(),
                        orderPage.getTotalElements()));
    }

    public PageRequestDto gets(int offset, int limit, Pageable pageable) {
        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderResponseDto> orderResponseDtos =
                orderRepository.findOrderResponseDtosByOffsetAndLimitWithQueryDsl(offset, limit);

        List<Long> orderIds =
                orderResponseDtos.stream().map(orderResponseDto -> orderResponseDto.getOrderId()).collect(Collectors.toList());

        List<OrderItemResponseDto> orderItemResponseDtos =
                orderRepository.findOrderItemResponseDtosByOrderIdsWithQueryDsl(orderIds);

        Map<Long, List<OrderItemResponseDto>> orderItemMap =
                orderItemResponseDtos.stream().collect(Collectors.groupingBy(orderItemResponseDto -> orderItemResponseDto.getOrderId()));

        orderResponseDtos.forEach(
                orderResponseDto ->
                        orderResponseDto.setOrderItemResponseDtos(orderItemMap.get(orderResponseDto.getOrderId())));

        int totalSize = orderResponseDtos.size();

        return PageRequestDto.of(orderResponseDtos,
                new PageImpl(orderResponseDtos,
                        orderPage.getPageable(),
                        totalSize));

    }

    public OrderResponseDto patch(OrderPatchDto orderPatchDto, Long orderId) {
        Order order = verifiedByOrderIdWithQueryDsl(orderId);

        Optional.ofNullable(orderPatchDto.getOrderStatus())
                .ifPresent(orderStatus -> order.setOrderStatus(orderStatus));

        return toResponseDto(orderRepository.save(order));
    }

    public void delete(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public Order verifiedOrder(Long orderId){
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.orElseThrow(
                () -> new BusinessException(ErrorCode.ORDER_NOT_FOUND));
    }

    public Order verifiedByOrderIdWithQueryDsl(Long orderId){
        Order order = orderRepository.findByOrderIdWithQueryDsl(orderId);
        if(order == null) throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        return order;
    }

    public Order toEntity(OrderPostDto orderPostDto){
        Order order = new Order();

        return order;
    }

    public OrderResponseDto toResponseDto(Order order){
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(order.getOrderId());
        orderResponseDto.setOrderStatus(order.getOrderStatus());
        orderResponseDto.setCreatedTime(order.getCreatedTime());
        orderResponseDto.setModifiedTime(order.getModifiedTime());
        orderResponseDto.setUserId(order.getUser().getUserId());
        return orderResponseDto;
    }



}
