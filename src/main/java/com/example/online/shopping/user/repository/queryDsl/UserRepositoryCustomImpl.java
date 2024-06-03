package com.example.online.shopping.user.repository.queryDsl;

import com.example.online.shopping.order.dto.OrderResponseDto;
import com.example.online.shopping.user.dto.UserResponseDto;
import com.example.online.shopping.user.entity.QUser;
import com.example.online.shopping.user.entity.User;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.example.online.shopping.order.entity.QOrder.order;
import static com.example.online.shopping.user.entity.QUser.user;

public class UserRepositoryCustomImpl implements UserRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public User findByUserIdWithQueryDsl(Long userId) {
        return queryFactory
                .select(user)
                .from(user)
                .where(user.userId.eq(userId))
                .fetchOne();
    }


    @Override
    public List<OrderResponseDto> findOrderResponseDtoByUserIdWithQueryDsl(Long userId) {
        return queryFactory
                .select(Projections.fields(OrderResponseDto.class,
                        order.orderId,
                        order.orderStatus,
                        order.createdTime,
                        order.modifiedTime))
                .from(order)
                .where(order.user.userId.eq(userId))
                .fetch();
    }

    @Override
    public List<UserResponseDto> findUserResponseDtoByUserIdWithQueryDsl(List<Long> userIds) {
        return queryFactory
                .select(Projections.fields(UserResponseDto.class,
                        user.username,
                        user.password,
                        user.role))
                .from(user)
                .where(user.userId.in(userIds))
                .fetch();
    }

    @Override
    public List<OrderResponseDto> findOrderResponseDtoByUserIdsWithQueryDsl(List<Long> userIds) {
        return queryFactory
                .select(Projections.fields(OrderResponseDto.class,
                        order.orderId,
                        order.orderStatus,
                        order.createdTime,
                        order.modifiedTime,
                        order.user.userId))
                .from(order)
                .where(order.user.userId.in(userIds))
                .fetch();
    }

    @Override
    public List<UserResponseDto> findUserResponseDtoByOffsetAndLimitWithQueryDsl(int offset, int limit) {
        return queryFactory
                .select(Projections.fields(UserResponseDto.class,
                        user.userId,
                        user.username,
                        user.password,
                        user.role))
                .from(user)
                .orderBy(user.userId.asc())
                .offset(offset)
                .limit(limit)
                .fetch();
    }

//    @Override
//    public User findByUsername(String username) {
//        return queryFactory
//                .select(user)
//                .from(user)
//                .where(user.username.eq(username))
//                .fetchOne();
//    }


}
