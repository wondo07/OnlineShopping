package com.example.online.shopping.item.controller;

import com.example.online.shopping.dto.PageRequestDto;
import com.example.online.shopping.item.dto.ItemPatchDto;
import com.example.online.shopping.item.dto.ItemPostDto;
import com.example.online.shopping.item.dto.ItemResponseDto;
import com.example.online.shopping.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    private ResponseEntity post(@RequestBody ItemPostDto itemPostDto){
        ItemResponseDto itemResponseDto = itemService.post(itemPostDto);
        return new ResponseEntity<>(itemResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{itemId}")
    private ResponseEntity get(@PathVariable("itemId") Long itemId){
        ItemResponseDto itemResponseDto = itemService.get(itemId);
        return new ResponseEntity<>(itemResponseDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    private ResponseEntity getAll(Pageable pageable){
        PageRequestDto pageRequestDto = itemService.getAll(pageable);
        return new ResponseEntity<>(pageRequestDto, HttpStatus.OK);
    }

    @GetMapping("/gets")
    private ResponseEntity gets(@RequestParam("offset") int offset,
                                @RequestParam("limit") int limit,
                                Pageable pageable){
        PageRequestDto pageRequestDto = itemService.gets1(offset, limit, pageable);
        return new ResponseEntity<>(pageRequestDto, HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity gets(@RequestParam("offset") int offset,
                                @RequestParam("limit") int limit){
        List<ItemResponseDto> itemResponseDtos = itemService.gets(offset, limit);
        return new ResponseEntity<>(itemResponseDtos, HttpStatus.OK);
    }

    @PatchMapping("/{itemId}")
    private ResponseEntity patch(@RequestBody ItemPatchDto itemPatchDto,
                                 @PathVariable("itemId") Long itemId){
        ItemResponseDto itemResponseDto = itemService.patch(itemPatchDto, itemId);
        return new ResponseEntity<>(itemResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    private ResponseEntity delete(@PathVariable("itemId") Long itemId){
        itemService.delete(itemId);
        return ResponseEntity.noContent().build();
    }
}
