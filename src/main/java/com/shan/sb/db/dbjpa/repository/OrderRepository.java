package com.shan.sb.db.dbjpa.repository;

import com.shan.sb.db.dbjpa.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}