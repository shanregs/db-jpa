package com.shan.sb.db.dbjpa.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Long id;

    @NotBlank(message = "Order number is required")
    private String orderNumber;

    @NotEmpty(message = "Order must contain at least one item")
    private List<OrderLineItemDTO> items;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
