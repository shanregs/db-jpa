package com.shan.sb.db.dbjpa.dto;

import com.shan.sb.db.dbjpa.entity.OrderItemStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class ItemDTO {
    private Long id;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @Positive(message = "Price must be positive")
    private double price;

    @Min(value = 0, message = "Discount cannot be negative")
    private double discount;

    @Min(value = 0, message = "Tax cannot be negative")
    private double tax;

    @NotNull(message = "Status is required")
    private OrderItemStatus status;

    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
