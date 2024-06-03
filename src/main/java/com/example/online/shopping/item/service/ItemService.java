package com.example.online.shopping.item.service;

import com.example.online.shopping.dto.PageRequestDto;
import com.example.online.shopping.exception.BusinessException;
import com.example.online.shopping.exception.ErrorCode;
import com.example.online.shopping.item.dto.ItemPatchDto;
import com.example.online.shopping.item.dto.ItemPostDto;
import com.example.online.shopping.item.dto.ItemResponseDto;
import com.example.online.shopping.item.entity.Item;
import com.example.online.shopping.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemResponseDto post(ItemPostDto itemPostDto) {

        if(itemRepository.findByName(itemPostDto.getName()) == null){
            Item item = toEntity(itemPostDto);
            Item save = itemRepository.save(item);
            return toResponseDto(save);
        } else {
            Item item = itemRepository.findByName(itemPostDto.getName());
            item.setStock(item.getStock() + itemPostDto.getStock());
            Item save = itemRepository.save(item);
            return toResponseDto(save);
        }

    }

    public ItemResponseDto get(Long itemId) {
        Item item = verifiedItemWithQueryDsl(itemId);
        return toResponseDto(item);
    }

    public PageRequestDto getAll(Pageable pageable) {
        Page<Item> itemPage = itemRepository.findAll(pageable);
        List<Item> items = itemPage.getContent();

        List<Long> itemIds = items.stream().map(item -> item.getItemId()).collect(Collectors.toList());

        List<ItemResponseDto> itemResponseDtos = itemRepository.findResponsesDtoByItemIdWithQueryDsl(itemIds);

        return PageRequestDto.of(itemResponseDtos,
                new PageImpl(itemResponseDtos,
                        itemPage.getPageable(),
                        itemPage.getTotalElements()));
    }

    public List<ItemResponseDto> gets(int offset, int limit) {
        List<ItemResponseDto> itemResponseDtos = itemRepository.findResponsesDtoByOffsetAndLimitWithQueryDsl(offset, limit);
        if(itemResponseDtos == null) throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);

        return itemResponseDtos;

    }

    public ItemResponseDto patch(ItemPatchDto itemPatchDto, Long itemId) {
        Item item = verifiedItemWithQueryDsl(itemId);

        Optional.ofNullable(itemPatchDto.getName())
                .ifPresent(name -> item.setName(name));

        Optional.ofNullable(itemPatchDto.getPrice())
                .ifPresent(price -> item.setPrice(price));

        Optional.ofNullable(itemPatchDto.getStock())
                .ifPresent(stock -> item.setStock(stock));

        return toResponseDto(itemRepository.save(item));
    }

    public void delete(Long itemId) {

        itemRepository.deleteById(itemId);

    }

    public Item verifiedItem(Long itemId){
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        return optionalItem.orElseThrow(
                () -> new BusinessException(ErrorCode.ITEM_NOT_FOUND));
    }

    public Item verifiedItemWithQueryDsl(Long itemId){
        Item item = itemRepository.findByItemIdWithQueryDsl(itemId);
        if(item == null) throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);
        return item;
    }

    public Item verifiedItemByName(String name){
        Item item = itemRepository.findByName(name);
        if(item == null) throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);
        return item;
    }

    public Item verifiedItemByNameWithQueryDsl(String name){
        Item item = itemRepository.findItemByNameWithQueryDsl(name);
        if(item == null) throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);
        return item;
    }



    public Item toEntity(ItemPostDto itemPostDto){
        return new Item(null, itemPostDto.getName(), itemPostDto.getPrice(), itemPostDto.getStock(), null);
    }

    public ItemResponseDto toResponseDto(Item item){
        return new ItemResponseDto(item.getItemId(), item.getName(), item.getPrice(), item.getStock());
    }

    public void save(Item item) {
        itemRepository.save(item);
    }

    public List<Long> itemIds(){
        return itemRepository.findAll().stream().map(item -> item.getItemId()).collect(Collectors.toList());
    }


    public PageRequestDto gets1(int offset, int limit, Pageable pageable) {
        Page<Item> itemPage = itemRepository.findAll(pageable);

        List<ItemResponseDto> itemResponseDtos =
                itemRepository.findResponsesDtoByOffsetAndLimitWithQueryDsl(offset, limit);

        return PageRequestDto.of(itemResponseDtos,
                new PageImpl(itemResponseDtos,
                        itemPage.getPageable(),
                        itemResponseDtos.size()));
    }
}
