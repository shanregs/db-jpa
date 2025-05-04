package com.shan.sb.db.dbjpa.repository;

import com.shan.sb.db.dbjpa.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}