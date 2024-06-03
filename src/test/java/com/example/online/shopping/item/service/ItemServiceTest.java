package com.example.online.shopping.item.service;

import com.example.online.shopping.item.dto.ItemResponseDto;
import com.example.online.shopping.item.entity.Item;
import com.example.online.shopping.item.repository.ItemRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @AfterEach
    void afterEach(){
        itemRepository.deleteAll();
    }

    @Test
    void findByItemIdWithQueryDsl() throws Exception{
        // when
        Item item = new Item();
        item.setStock(5);
        item.setName("book");
        item.setPrice(5000);

        itemRepository.save(item);

        // given
        Item verifiedItem = itemRepository.findByItemIdWithQueryDsl(item.getItemId());

        // then
        assertThat(verifiedItem.getStock()).isEqualTo(5);
        assertThat(verifiedItem.getName()).isEqualTo("book");
        assertThat(verifiedItem.getPrice()).isEqualTo(5000);
    }

    @Test
    void findResponsesDtoByItemIdWithQueryDsl() throws Exception{
        // when
        Item item1 = new Item();
        item1.setStock(5);
        item1.setName("book");
        item1.setPrice(5000);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setStock(10);
        item2.setName("pen");
        item2.setPrice(1000);
        itemRepository.save(item2);

        List<Long> itemIds =
                itemRepository.findAll().stream().map(item -> item.getItemId()).collect(Collectors.toList());

        // given
        List<ItemResponseDto> itemResponseDtos = itemRepository.findResponsesDtoByItemIdWithQueryDsl(itemIds);

        // then
        Assertions.assertThat(itemResponseDtos.get(0).getName()).isEqualTo("book");
        Assertions.assertThat(itemResponseDtos.get(0).getPrice()).isEqualTo(5000);
        Assertions.assertThat(itemResponseDtos.get(0).getStock()).isEqualTo(5);
        Assertions.assertThat(itemResponseDtos.get(1).getName()).isEqualTo("pen");
        Assertions.assertThat(itemResponseDtos.get(1).getPrice()).isEqualTo(1000);
        Assertions.assertThat(itemResponseDtos.get(1).getStock()).isEqualTo(10);
    }

    @Test
    void findItemByNameWithQueryDsl() throws Exception{
        // when
        Item item = new Item();
        item.setStock(5);
        item.setName("book");
        item.setPrice(5000);

        itemRepository.save(item);

        // given
        Item verifiedItem = itemRepository.findItemByNameWithQueryDsl(item.getName());

        // then
        Assertions.assertThat(verifiedItem.getPrice()).isEqualTo(5000);
        Assertions.assertThat(verifiedItem.getStock()).isEqualTo(5);
        Assertions.assertThat(verifiedItem.getName()).isEqualTo("book");
    }

    @Test
    void findResponsesDtoByOffsetAndLimitWithQueryDsl() throws Exception{
        // when
        Item item1 = new Item();
        item1.setStock(5);
        item1.setName("book");
        item1.setPrice(5000);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setStock(10);
        item2.setName("pen");
        item2.setPrice(1000);
        itemRepository.save(item2);

        Item item3 = new Item();
        item3.setStock(5);
        item3.setName("eraser");
        item3.setPrice(500);
        itemRepository.save(item3);

        // given
        List<ItemResponseDto> itemResponseDtos = itemRepository.findResponsesDtoByOffsetAndLimitWithQueryDsl(1, 2);

        // then
        Assertions.assertThat(itemResponseDtos.get(0).getName()).isEqualTo("pen");
        Assertions.assertThat(itemResponseDtos.get(0).getPrice()).isEqualTo(1000);
        Assertions.assertThat(itemResponseDtos.get(0).getStock()).isEqualTo(10);
        Assertions.assertThat(itemResponseDtos.get(1).getName()).isEqualTo("eraser");
        Assertions.assertThat(itemResponseDtos.get(1).getPrice()).isEqualTo(500);
        Assertions.assertThat(itemResponseDtos.get(1).getStock()).isEqualTo(5);
    }


}