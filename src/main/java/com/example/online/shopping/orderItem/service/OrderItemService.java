package com.example.online.shopping.orderItem.service;

import com.example.online.shopping.dto.PageRequestDto;
import com.example.online.shopping.exception.BusinessException;
import com.example.online.shopping.exception.ErrorCode;
import com.example.online.shopping.item.entity.Item;
import com.example.online.shopping.item.service.ItemService;
import com.example.online.shopping.order.dto.OrderResponseDto;
import com.example.online.shopping.order.entity.Order;
import com.example.online.shopping.order.repository.OrderRepository;
import com.example.online.shopping.order.service.OrderService;
import com.example.online.shopping.orderItem.dto.OrderItemPatchDto;
import com.example.online.shopping.orderItem.dto.OrderItemPostDto;
import com.example.online.shopping.orderItem.dto.OrderItemResponseDto;
import com.example.online.shopping.orderItem.entity.OrderItem;
import com.example.online.shopping.orderItem.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderService orderService;

    private final ItemService itemService;


    public OrderItemResponseDto post(OrderItemPostDto orderItemPostDto) {


        Item item = itemService.verifiedItemWithQueryDsl(orderItemPostDto.getItemId());
        Order order = orderService.verifiedByOrderIdWithQueryDsl(orderItemPostDto.getOrderId());

        if(!item.getName().equals(orderItemPostDto.getName())) {
            throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);
        }

        if(orderItemPostDto.getCount() <= item.getStock()){
            item.setStock(item.getStock() - orderItemPostDto.getCount());
            itemService.save(item);
        } else throw new BusinessException(ErrorCode.EXCEEDING_STOCK);

        OrderItem orderItem = toEntity(orderItemPostDto);

        item.addOrderItem(orderItem);

        order.addOrderItem(orderItem);


        orderItem.setCount(orderItemPostDto.getCount());
        orderItem.setName(orderItemPostDto.getName());
        int price = orderItemPostDto.getCount() * item.getPrice();

        orderItem.setTotalPrice(price);


        return toResponseDto(orderItemRepository.save(orderItem));

    }

    public OrderItemResponseDto get(Long orderItemId) {
        OrderItem orderItem = verifiedOrderItemWithQueryDsl(orderItemId);
        return toResponseDto(orderItem);
    }

    public PageRequestDto getAll(Pageable pageable) {
        Page<OrderItem> orderItemPage = orderItemRepository.findAll(pageable);
        List<OrderItem> orderItems = orderItemPage.getContent();

        List<Long> orderItemIds =
                orderItems.stream().map(orderItem -> orderItem.getOrderItemId()).collect(Collectors.toList());

        List<OrderItemResponseDto> orderItemResponseDtos =
                orderItemRepository.findOrderItemResponseDtoByOrderItemIdsWithQueryDsl(orderItemIds);

        return PageRequestDto.of(orderItemResponseDtos,
                new PageImpl(orderItemResponseDtos,
                        orderItemPage.getPageable(),
                        orderItemPage.getTotalElements()));
    }

    public PageRequestDto gets(int offset, int limit, Pageable pageable) {
        Page<OrderItem> orderItemPage = orderItemRepository.findAll(pageable);
        List<OrderItemResponseDto> orderItemResponseDtos =
                orderItemRepository.findOrderItemResponseDtoByOffsetAndLimitWithQueryDsl(offset, limit);

        int totalSize = orderItemResponseDtos.size();

        return PageRequestDto.of(orderItemResponseDtos,
                new PageImpl(orderItemResponseDtos,
                        orderItemPage.getPageable(),
                        totalSize));
    }

    public OrderItemResponseDto patch(OrderItemPatchDto orderItemPatchDto, Long orderItemId) {
        OrderItem orderItem = verifiedOrderItemWithQueryDsl(orderItemId);
        Item item = itemService.verifiedItem(orderItem.getItem().getItemId());

        List<Long> itemIds = itemService.itemIds();

        if(orderItemPatchDto.getName().equals(orderItem.getItem().getName())) {
            item.setStock(orderItem.getCount() + item.getStock());
            itemService.save(item);

            if(item.getStock() > orderItemPatchDto.getCount()){
                item.setStock(item.getStock() - orderItemPatchDto.getCount());
                itemService.save(item);
                orderItem.setName(orderItemPatchDto.getName());
                orderItem.setCount(orderItemPatchDto.getCount());
                orderItem.setTotalPrice(orderItemPatchDto.getCount() * item.getPrice());
//                orderItemRepository.save(orderItem);
            } else throw new BusinessException(ErrorCode.EXCEEDING_STOCK);
        } else {
            String name = null;
            for(int i = 0; i < itemIds.size(); i++){
                if(itemService.verifiedItem(itemIds.get(i)).getName().equals(orderItemPatchDto.getName())){
                    name = itemService.verifiedItem(itemIds.get(i)).getName();
                }
            }
            if(name == null) throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);

            if(name.equals(orderItemPatchDto.getName())){
                OrderItem verifiedOrderItem = verifiedOrderItem(orderItemId);
                item.setStock(verifiedOrderItem.getCount() + item.getStock());
                itemService.save(item);
                Item newItem = itemService.verifiedItemByName(orderItemPatchDto.getName());
                if(newItem.getStock() >= orderItemPatchDto.getCount()){
                    newItem.setStock(newItem.getStock()-orderItemPatchDto.getCount());
                    itemService.save(newItem);
                    orderItem.setCount(orderItemPatchDto.getCount());
                    orderItem.setTotalPrice(newItem.getPrice() * orderItemPatchDto.getCount());
                    orderItem.setName(orderItemPatchDto.getName());

                } else throw new BusinessException(ErrorCode.EXCEEDING_STOCK);
            }
        }
        return toResponseDto(orderItemRepository.save(orderItem));
    }


    public void delete(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findByOrderItemIdWithQueryDsl(orderItemId);
        Item item = itemService.verifiedItem(orderItem.getItem().getItemId());
        item.setStock(item.getStock() + orderItem.getCount());
        itemService.save(item);
        orderItemRepository.deleteById(orderItemId);
    }

    public OrderItem verifiedOrderItem(Long orderItemId){
        Optional<OrderItem> optionalOrderItem = orderItemRepository.findById(orderItemId);
        return optionalOrderItem.orElseThrow(
                () -> new BusinessException(ErrorCode.ORDER_ITEM_NOT_FOUND));
    }

    public OrderItem verifiedOrderItemWithQueryDsl(Long orderItemId){
        OrderItem orderItem = orderItemRepository.findByOrderItemIdWithQueryDsl(orderItemId);
        if(orderItem == null) throw new BusinessException(ErrorCode.ORDER_ITEM_NOT_FOUND);
        return orderItem;
    }

    public OrderItemResponseDto toResponseDto(OrderItem orderItem){
        OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
        orderItemResponseDto.setOrderItemId(orderItem.getOrderItemId());
        orderItemResponseDto.setOrderId(orderItem.getOrder().getOrderId());
        orderItemResponseDto.setItemId(orderItem.getItem().getItemId());
        orderItemResponseDto.setCount(orderItem.getCount());
        orderItemResponseDto.setTotalPrice(orderItem.getTotalPrice());
        orderItemResponseDto.setName(orderItem.getName());
        return orderItemResponseDto;

    }

    public OrderItem toEntity(OrderItemPostDto orderItemPostDto){
        OrderItem orderItem = new OrderItem();
        orderItem.setName(orderItemPostDto.getName());
        return orderItem;
    }



}
