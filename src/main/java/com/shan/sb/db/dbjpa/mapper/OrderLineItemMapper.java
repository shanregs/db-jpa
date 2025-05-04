package com.shan.sb.db.dbjpa.mapper;

import com.shan.sb.db.dbjpa.dto.OrderLineItemDTO;
import com.shan.sb.db.dbjpa.entity.Order;
import com.shan.sb.db.dbjpa.entity.OrderLineItem;
import com.shan.sb.db.dbjpa.entity.Product;

public class OrderLineItemMapper {

    public static OrderLineItem to(OrderLineItemDTO itemDTO, Product product, Order order) {
        OrderLineItem item = new OrderLineItem();
        item.setQuantity(itemDTO.getQuantity());
        item.setProduct(product);
        item.setOrder(order);
        item.setPrice(itemDTO.getPrice());
        item.setDiscount(itemDTO.getDiscount());
        item.setTax(itemDTO.getTax());
        item.setStatus(itemDTO.getStatus());
        item.setNotes(itemDTO.getNotes());
        return item;
    }

    public static OrderLineItemDTO toDto(OrderLineItem i) {

        return null;
    }
}
