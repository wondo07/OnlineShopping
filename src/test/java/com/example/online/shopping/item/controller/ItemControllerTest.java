package com.example.online.shopping.item.controller;

import com.example.online.shopping.exception.BusinessException;
import com.example.online.shopping.item.dto.ItemPatchDto;
import com.example.online.shopping.item.dto.ItemPostDto;
import com.example.online.shopping.item.dto.ItemResponseDto;
import com.example.online.shopping.item.entity.Item;
import com.example.online.shopping.item.repository.ItemRepository;
import com.example.online.shopping.item.service.ItemService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class ItemControllerTest {


    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void post(){
        // when
        ItemPostDto itemPostDto = new ItemPostDto();
        itemPostDto.setStock(5);
        itemPostDto.setName("book");
        itemPostDto.setPrice(5000);

        // given
        ItemResponseDto itemResponseDto = itemService.post(itemPostDto);

        // then
        Assertions.assertThat(itemResponseDto.getName()).isEqualTo("book");
        Assertions.assertThat(itemResponseDto.getStock()).isEqualTo(5);
        Assertions.assertThat(itemResponseDto.getPrice()).isEqualTo(5000);
    }

    @Test
    void get(){
        // when
        Item item = new Item();
        item.setStock(5);
        item.setName("book");
        item.setPrice(5000);
        Item savedItem = itemRepository.save(item);

        // given
        ItemResponseDto itemResponseDto = itemService.get(savedItem.getItemId());

        // then
        Assertions.assertThat(itemResponseDto.getName()).isEqualTo("book");
        Assertions.assertThat(itemResponseDto.getStock()).isEqualTo(5);
        Assertions.assertThat(itemResponseDto.getPrice()).isEqualTo(5000);
    }

    @Test
    void patch(){
        // when
        Item item = new Item();
        item.setStock(5);
        item.setName("book");
        item.setPrice(5000);
        Item savedItem = itemRepository.save(item);

        ItemPatchDto itemPatchDto = new ItemPatchDto();
        itemPatchDto.setName("pen");
        itemPatchDto.setStock(3);
        itemPatchDto.setPrice(1000);

        // given
        ItemResponseDto itemResponseDto = itemService.patch(itemPatchDto, savedItem.getItemId());

        // then
        Assertions.assertThat(itemResponseDto.getName()).isEqualTo("pen");
        Assertions.assertThat(itemResponseDto.getStock()).isEqualTo(3);
        Assertions.assertThat(itemResponseDto.getPrice()).isEqualTo(1000);
    }

    @Test
    void delete(){
        // when
        Item item = new Item();
        item.setStock(5);
        item.setName("book");
        item.setPrice(5000);
        Item savedItem = itemRepository.save(item);

        // given
        itemService.delete(savedItem.getItemId());

        // then
        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class,
                () -> itemService.verifiedItem(savedItem.getItemId()));
    }
}