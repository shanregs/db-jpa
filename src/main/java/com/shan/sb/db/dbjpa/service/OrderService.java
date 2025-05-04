package com.shan.sb.db.dbjpa.service;

import com.shan.sb.db.dbjpa.dto.OrderDTO;
import com.shan.sb.db.dbjpa.dto.PagedResponse;
import com.shan.sb.db.dbjpa.entity.Order;
import com.shan.sb.db.dbjpa.entity.Item;
import com.shan.sb.db.dbjpa.entity.Product;
import com.shan.sb.db.dbjpa.exception.InsufficientStockException;
import com.shan.sb.db.dbjpa.exception.ResourceNotFoundException;
import com.shan.sb.db.dbjpa.mapper.ItemMapper;
import com.shan.sb.db.dbjpa.mapper.OrderMapper;
import com.shan.sb.db.dbjpa.notification.MailSender;
import com.shan.sb.db.dbjpa.repository.OrderRepository;
import com.shan.sb.db.dbjpa.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ItemService itemService;
    private final MailSender mailSender;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public OrderDTO  createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setOrderNumber(orderDTO.getOrderNumber());

        List<Item> items = getOrderItems(orderDTO, order);

        order.setItems(items);
        orderRepository.save(order);
        List<Product> productList = items.stream().map(Item::getProduct).collect(Collectors.toList());
        if(!CollectionUtils.isEmpty(productList)) {
            productRepository.saveAll(productList);
        }
        orderDTO.setId(order.getId());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setUpdatedAt(order.getUpdatedAt());
        mailSender.sendOrderConfirmationEmail(order.getId());
        return orderDTO;
    }
    public PagedResponse<OrderDTO> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderDTO> orderDTOs = orderPage.getContent().stream()
                .map(OrderMapper::toDto)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                orderDTOs,
                orderPage.getNumber(),
                orderPage.getSize(),
                orderPage.getTotalElements(),
                orderPage.getTotalPages(),
                orderPage.isLast()
        );
    }
    public OrderDTO getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + id));
        OrderDTO orderDTO = OrderMapper.toDto(order);
        return orderDTO;
    }

    private List<Item> getOrderItems(OrderDTO orderDTO, Order order) {
        List<Item> items = orderDTO.getItems().stream().map(itemDTO -> {
            Long id = itemDTO.getProductId();
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));
            if(product.getStock() < itemDTO.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }
            product.setStock(product.getStock() - itemDTO.getQuantity());
            Item item = ItemMapper.toItem(itemDTO, product, order);
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
        List<Item> items = getOrderItems(orderDTO, order);
        order.setItems(items);
        orderRepository.save(order);
        productRepository.saveAll(items.stream().map(Item::getProduct).collect(Collectors.toList()));

        orderDTO.setId(order.getId());
        orderDTO.setCreatedAt(order.getCreatedAt());
        orderDTO.setUpdatedAt(order.getUpdatedAt());
        return orderDTO;
    }
}

