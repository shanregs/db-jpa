package com.shan.sb.db.dbjpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderLineItem extends AuditableEntity{

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private double price;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    private double discount;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    private double tax;

    @Enumerated(EnumType.STRING)
    private OrderItemStatus status;

    private String notes;
}
