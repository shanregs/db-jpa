package com.shan.sb.db.dbjpa.service;

import com.shan.sb.db.dbjpa.dto.OrderDTO;
import com.shan.sb.db.dbjpa.entity.Order;
import com.shan.sb.db.dbjpa.entity.OrderLineItem;
import com.shan.sb.db.dbjpa.entity.Product;
import com.shan.sb.db.dbjpa.exception.InsufficientStockException;
import com.shan.sb.db.dbjpa.exception.ResourceNotFoundException;
import com.shan.sb.db.dbjpa.mapper.OrderLineItemMapper;
import com.shan.sb.db.dbjpa.mapper.OrderMapper;
import com.shan.sb.db.dbjpa.repository.OrderItemRepository;
import com.shan.sb.db.dbjpa.repository.OrderRepository;
import com.shan.sb.db.dbjpa.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderLineItemService orderLineItemService;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public OrderDTO  createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderNumber(orderDTO.getOrderNumber());

        List<OrderLineItem> items = getOrderLineItems(orderDTO, order);

        order.setItems(items);
        orderRepository.save(order);
        List<Product> productList = items.stream().map(OrderLineItem::getProduct).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(productList)) {
            productRepository.saveAll(productList);
        }
        orderDTO.setId(order.getId());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setUpdatedAt(order.getUpdatedAt());
        sendOrderConfirmationEmail(order.getId());
        return orderDTO;
    }
    public OrderDTO getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        OrderDTO orderDTO = OrderMapper.toDto(order);
        return orderDTO;
    }



    private List<OrderLineItem> getOrderLineItems(OrderDTO orderDTO, Order order) {
        List<OrderLineItem> items = orderDTO.getItems().stream().map(itemDTO -> {
            Long id = itemDTO.getProductId();
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
            if(product.getStock() < itemDTO.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - itemDTO.getQuantity());
            OrderLineItem item = OrderLineItemMapper.to(itemDTO, product, order);
            return item;
        }).collect(Collectors.toList());
        return items;
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        });
        orderRepository.delete(order);
    }
    @Async
    public void sendOrderConfirmationEmail(Long orderId) {
        System.out.println("Sending confirmation email for order ID: " + orderId);
        try {
            Thread.sleep(1000); // Simulate I/O delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));

        order.setOrderNumber(orderDTO.getOrderNumber());

        // Remove existing items and restore stock
        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
        });
        order.getItems().clear();
        List<OrderLineItem> items = getOrderLineItems(orderDTO, order);
        order.setItems(items);
        orderRepository.save(order);
        productRepository.saveAll(items.stream().map(OrderLineItem::getProduct).collect(Collectors.toList()));

        orderDTO.setId(order.getId());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setUpdatedAt(order.getUpdatedAt());
        return orderDTO;
    }
}

