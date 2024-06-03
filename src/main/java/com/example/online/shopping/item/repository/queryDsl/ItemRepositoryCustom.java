package com.example.online.shopping.item.repository.queryDsl;

import com.example.online.shopping.item.dto.ItemResponseDto;
import com.example.online.shopping.item.entity.Item;

import java.util.List;

public interface ItemRepositoryCustom {


    Item findByItemIdWithQueryDsl(Long itemId);

    List<ItemResponseDto> findResponseDtoByItemIdWithQueryDsl(Long itemId);

    List<ItemResponseDto> findResponsesDtoByItemIdWithQueryDsl(List<Long> itemIds);


    Item findItemByNameWithQueryDsl(String name);

    List<ItemResponseDto> findResponsesDtoByOffsetAndLimitWithQueryDsl(int offset, int limit);
}
