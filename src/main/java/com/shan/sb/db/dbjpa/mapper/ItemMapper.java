package com.shan.sb.db.dbjpa.mapper;

import com.shan.sb.db.dbjpa.dto.ItemDTO;
import com.shan.sb.db.dbjpa.entity.Order;
import com.shan.sb.db.dbjpa.entity.Item;
import com.shan.sb.db.dbjpa.entity.Product;

public class ItemMapper {

    public static Item toItem(ItemDTO itemDTO, Product product, Order order) {
        Item item = new Item();
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

    public static ItemDTO toDTO(Item item) {
        ItemDTO itemDTO = new ItemDTO();
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
