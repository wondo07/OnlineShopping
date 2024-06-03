package com.example.online.shopping.order.controller;

import com.example.online.shopping.dto.OrderStatus;
import com.example.online.shopping.dto.PageRequestDto;
import com.example.online.shopping.order.dto.OrderPatchDto;
import com.example.online.shopping.order.dto.OrderPostDto;
import com.example.online.shopping.order.dto.OrderResponseDto;
import com.example.online.shopping.order.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    private ResponseEntity post(@RequestBody OrderPostDto orderPostDto){
        OrderResponseDto orderResponseDto = orderService.post(orderPostDto);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    private ResponseEntity get(@PathVariable("orderId") Long orderId){
        OrderResponseDto orderResponseDto = orderService.get(orderId);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
    }

    @GetMapping("/all")
    private ResponseEntity getAll(Pageable pageable){
        PageRequestDto pageRequestDto = orderService.getAll(pageable);
        return new ResponseEntity<>(pageRequestDto, HttpStatus.OK);
    }

    @GetMapping
    private ResponseEntity gets(@RequestParam("offset")int offset,
                                @RequestParam("limit")int limit,
                                Pageable pageable){
        PageRequestDto pageRequestDto = orderService.gets(offset, limit, pageable);
        return new ResponseEntity<>(pageRequestDto, HttpStatus.OK);
    }

    @PatchMapping("/{orderId}")
    private ResponseEntity patch(@RequestBody OrderPatchDto orderPatchDto,
                                 @PathVariable("orderId") Long orderId){
        OrderResponseDto orderResponseDto = orderService.patch(orderPatchDto, orderId);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    private ResponseEntity delete(@PathVariable("orderId") Long orderId){
        orderService.delete(orderId);
        return ResponseEntity.noContent().build();
    }
}
