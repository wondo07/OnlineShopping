package com.example.online.shopping.orderItem.repository.queryDsl;

import com.example.online.shopping.item.entity.QItem;
import com.example.online.shopping.order.entity.QOrder;
import com.example.online.shopping.orderItem.dto.OrderItemResponseDto;
import com.example.online.shopping.orderItem.entity.OrderItem;
import com.example.online.shopping.orderItem.entity.QOrderItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.example.online.shopping.orderItem.entity.QOrderItem.orderItem;

public class OrderItemRepositoryCustomImpl implements OrderItemRepositoryCustom{


    private JPAQueryFactory queryFactory;

    public OrderItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public OrderItem findByOrderItemIdWithQueryDsl(Long orderItemId) {
        return queryFactory
                .select(orderItem)
                .from(orderItem)
                .join(orderItem.order, QOrder.order).fetchJoin()
                .join(orderItem.item, QItem.item).fetchJoin()
                .where(orderItem.orderItemId.eq(orderItemId))
                .fetchOne();
    }

    @Override
    public List<OrderItemResponseDto> findOrderItemResponseDtoByOrderItemIdsWithQueryDsl(List<Long> orderItemIds) {
        return queryFactory
                .select(Projections.fields(OrderItemResponseDto.class,
                        orderItem.item.itemId,
                        orderItem.name,
                        orderItem.count,
                        orderItem.totalPrice))
                .from(orderItem)
                .where(orderItem.orderItemId.in(orderItemIds))
                .fetch();
    }

    @Override
    public List<OrderItemResponseDto> findOrderItemResponseDtoByOffsetAndLimitWithQueryDsl(int offset, int limit) {
        return queryFactory
                .select(Projections.fields(OrderItemResponseDto.class,
                        orderItem.item.itemId,
                        orderItem.name,
                        orderItem.count,
                        orderItem.totalPrice))
                .from(orderItem)
                .orderBy(orderItem.orderItemId.asc())
                .offset(offset)
                .limit(limit)
                .fetch();

    }
}
