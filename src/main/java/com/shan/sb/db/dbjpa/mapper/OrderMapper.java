package com.shan.sb.db.dbjpa.mapper;

import com.shan.sb.db.dbjpa.dto.OrderDTO;
import com.shan.sb.db.dbjpa.entity.Order;

import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderDTO toDto(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderNumber(order.getOrderNumber());
        orderDTO.setItems(order.getItems().stream().map(OrderLineItemMapper::toDto).collect(Collectors.toList()));
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setUpdatedAt(order.getUpdatedAt());
        return orderDTO;
    }
}
