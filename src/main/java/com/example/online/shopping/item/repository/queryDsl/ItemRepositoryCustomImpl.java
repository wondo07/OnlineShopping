package com.example.online.shopping.item.repository.queryDsl;

import com.example.online.shopping.item.dto.ItemResponseDto;
import com.example.online.shopping.item.entity.Item;
import com.example.online.shopping.item.entity.QItem;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

import static com.example.online.shopping.item.entity.QItem.item;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Item findByItemIdWithQueryDsl(Long itemId) {
        return queryFactory
                .select(item)
                .from(item)
                .where(item.itemId.eq(itemId))
                .fetchOne();
    }

    @Override
    public List<ItemResponseDto> findResponseDtoByItemIdWithQueryDsl(Long itemId) {
        return queryFactory
                .select(Projections.fields(ItemResponseDto.class,
                        item.itemId,
                        item.name,
                        item.price,
                        item.stock))
                .from(item)
                .where(item.itemId.eq(itemId))
                .fetch();
    }

    @Override
    public List<ItemResponseDto> findResponsesDtoByItemIdWithQueryDsl(List<Long> itemIds) {
        return queryFactory
                .select(Projections.fields(ItemResponseDto.class,
                        item.itemId,
                        item.name,
                        item.price,
                        item.stock))
                .from(item)
                .where(item.itemId.in(itemIds))
                .fetch();
    }

    @Override
    public Item findItemByNameWithQueryDsl(String name) {
        return queryFactory
                .select(item)
                .from(item)
                .where(item.name.eq(name))
                .fetchOne();
    }

    @Override
    public List<ItemResponseDto> findResponsesDtoByOffsetAndLimitWithQueryDsl(int offset, int limit) {
        return queryFactory
                .select(Projections.fields(ItemResponseDto.class,
                        item.itemId,
                        item.name,
                        item.price,
                        item.stock))
                .from(item)
                .orderBy(item.itemId.asc())
                .offset(offset)
                .limit(limit)
                .fetch();

    }
}
