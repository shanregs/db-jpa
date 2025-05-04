package com.shan.sb.db.dbjpa.repository;

import com.shan.sb.db.dbjpa.entity.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findByOrderId(Long orderId);
}