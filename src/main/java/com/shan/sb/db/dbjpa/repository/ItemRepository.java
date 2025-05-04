package com.shan.sb.db.dbjpa.repository;

import com.shan.sb.db.dbjpa.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOrderId(Long orderId);
}