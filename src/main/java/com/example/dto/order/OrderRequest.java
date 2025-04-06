package com.example.dto.order;

import com.example.dto.orderItem.OrderItemRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Order date cannot be null")
    private LocalDateTime orderDate;

    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequest> items;
}