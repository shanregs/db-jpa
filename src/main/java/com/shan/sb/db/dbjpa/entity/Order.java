package com.shan.sb.db.dbjpa.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends AuditableEntity {

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @OneToMany(mappedBy = "order",  cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<OrderLineItem> items = new ArrayList<>();
}
