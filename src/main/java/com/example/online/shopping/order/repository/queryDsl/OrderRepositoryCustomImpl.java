package com.example.online.shopping.order.repository.queryDsl;

import com.example.online.shopping.order.dto.OrderResponseDto;
import com.example.online.shopping.order.entity.Order;
import com.example.online.shopping.order.entity.QOrder;
import com.example.online.shopping.orderItem.dto.OrderItemResponseDto;
import com.example.online.shopping.orderItem.entity.QOrderItem;
import com.example.online.shopping.user.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.example.online.shopping.order.entity.QOrder.order;
import static com.example.online.shopping.orderItem.entity.QOrderItem.orderItem;
import static com.example.online.shopping.user.entity.QUser.user;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{


    private JPAQueryFactory queryFactory;

    public OrderRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public OrderResponseDto findResponseDtoByOrderIdWithQueryDsl(Long orderId) {
        return queryFactory
                .select(Projections.fields(OrderResponseDto.class,
                        order.orderId,
                        order.orderStatus,
                        order.createdTime,
                        order.modifiedTime))
                .from(order)
                .where(order.orderId.eq(orderId))
                .fetchOne();
    }

    @Override
    public List<OrderResponseDto> findResponseDtosByOrderIdWithQueryDsl(List<Long> orderIds) {
        return queryFactory
                .select(Projections.fields(OrderResponseDto.class,
                        order.orderId,
                        order.orderStatus,
                        order.createdTime,
                        order.modifiedTime,
                        order.user.userId))
                .from(order)
                .where(order.orderId.in(orderIds))
                .fetch();
    }

    @Override
    public Order findByOrderIdWithQueryDsl(Long orderId) {
        return queryFactory
                .select(order)
                .from(order)
                .join(order.user, user).fetchJoin()
                .where(order.orderId.eq(orderId))
                .fetchOne();
    }

    @Override
    public List<OrderItemResponseDto> findOrderItemResponseDtosByOrderIdsWithQueryDsl(List<Long> orderIds) {
        return queryFactory
                .select(Projections.fields(OrderItemResponseDto.class,
                        orderItem.order.orderId,
                        orderItem.item.itemId,
                        orderItem.name,
                        orderItem.count,
                        orderItem.totalPrice))
                .from(orderItem)
                .where(orderItem.order.orderId.in(orderIds))
                .fetch();
    }

    @Override
    public List<OrderResponseDto> findOrderResponseDtosByOffsetAndLimitWithQueryDsl(int offset, int limit) {
        return queryFactory
                .select(Projections.fields(OrderResponseDto.class,
                        order.orderId,
                        order.orderStatus,
                        order.createdTime,
                        order.modifiedTime,
                        order.user.userId))
                .from(order)
                .orderBy(order.orderId.asc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }


}
