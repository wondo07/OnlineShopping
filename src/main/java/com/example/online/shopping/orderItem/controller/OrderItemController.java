package com.example.online.shopping.orderItem.controller;

import com.example.online.shopping.dto.PageRequestDto;
import com.example.online.shopping.orderItem.dto.OrderItemPatchDto;
import com.example.online.shopping.orderItem.dto.OrderItemPostDto;
import com.example.online.shopping.orderItem.dto.OrderItemResponseDto;
import com.example.online.shopping.orderItem.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orderitem")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping
    private ResponseEntity post(@RequestBody OrderItemPostDto orderItemPostDto){
        OrderItemResponseDto orderItemResponseDto = orderItemService.post(orderItemPostDto);
        return new ResponseEntity<>(orderItemResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{orderItemId}")
    private ResponseEntity get(@PathVariable("orderItemId") Long orderItemId){
        OrderItemResponseDto orderItemResponseDto = orderItemService.get(orderItemId);
        return new ResponseEntity<>(orderItemResponseDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    private ResponseEntity getAll(Pageable pageable){
        PageRequestDto pageRequestDto = orderItemService.getAll(pageable);
        return new ResponseEntity<>(pageRequestDto, HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity gets(@RequestParam("offset")int offset,
                                @RequestParam("limit")int limit,
                                Pageable pageable){
        PageRequestDto pageRequestDto = orderItemService.gets(offset, limit, pageable);
        return new ResponseEntity<>(pageRequestDto, HttpStatus.OK);
    }

    @PatchMapping("/{orderItemId}")
    private ResponseEntity patch(@RequestBody OrderItemPatchDto orderItemPatchDto,
                                 @PathVariable("orderItemId") Long orderItemId){
        OrderItemResponseDto orderItemResponseDto = orderItemService.patch(orderItemPatchDto, orderItemId);
        return new ResponseEntity<>(orderItemResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{orderItemId}")
    private ResponseEntity delete(@PathVariable("orderItemId") Long orderItemId){
        orderItemService.delete(orderItemId);
        return ResponseEntity.noContent().build();
    }
}
