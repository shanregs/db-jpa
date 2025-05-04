package com.shan.sb.db.dbjpa.service;


import com.shan.sb.db.dbjpa.dto.OrderLineItemDTO;
import com.shan.sb.db.dbjpa.entity.OrderLineItem;
import com.shan.sb.db.dbjpa.repository.OrderItemRepository;
import com.shan.sb.db.dbjpa.repository.OrderRepository;
import com.shan.sb.db.dbjpa.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderLineItemService {

    private final OrderItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    public List<OrderLineItem> getOrderLineItemByOrderId(Long orderId) {
        
    }


    public OrderLineItemDTO toDTO(OrderLineItem item) {
        OrderLineItemDTO itemDTO = new OrderLineItemDTO();
        itemDTO.setId(item.getId());
        itemDTO.setQuantity(item.getQuantity());
        itemDTO.setProductId(item.getProduct().getId());
        itemDTO.setPrice(item.getPrice());
        itemDTO.setDiscount(item.getDiscount());
        itemDTO.setTax(item.getTax());
        itemDTO.setStatus(item.getStatus());
        itemDTO.setNotes(item.getNotes());
        itemDTO.setCreatedAt(item.getCreatedAt());
        itemDTO.setUpdatedAt(item.getUpdatedAt());
        return itemDTO;
    }
}
