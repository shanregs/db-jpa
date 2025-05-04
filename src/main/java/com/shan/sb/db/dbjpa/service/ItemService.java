package com.shan.sb.db.dbjpa.service;


import com.shan.sb.db.dbjpa.dto.ItemDTO;
import com.shan.sb.db.dbjpa.entity.Item;
import com.shan.sb.db.dbjpa.entity.Order;
import com.shan.sb.db.dbjpa.entity.Product;
import com.shan.sb.db.dbjpa.exception.InsufficientStockException;
import com.shan.sb.db.dbjpa.exception.ResourceNotFoundException;
import com.shan.sb.db.dbjpa.mapper.ItemMapper;
import com.shan.sb.db.dbjpa.repository.ItemRepository;
import com.shan.sb.db.dbjpa.repository.OrderRepository;
import com.shan.sb.db.dbjpa.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    @Transactional
    public ItemDTO addItemToOrder(Long orderId, ItemDTO itemDTO) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        Product product = productRepository.findById(itemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemDTO.getProductId()));

        if (product.getStock() < itemDTO.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
        }
        Item item = ItemMapper.toItem(itemDTO, product, order);
        product.setStock(product.getStock() - itemDTO.getQuantity());
        itemRepository.save(item);
        productRepository.save(product);

        itemDTO.setId(item.getId());
        itemDTO.setCreatedAt(item.getCreatedAt());
        itemDTO.setUpdatedAt(item.getUpdatedAt());
        return itemDTO;

    }


    public List<ItemDTO> getItemsByOrderId(Long orderId) {
        return itemRepository.findByOrderId(orderId).stream()
                .map(ItemMapper::toDTO)
                .collect(Collectors.toList());
    }
}
