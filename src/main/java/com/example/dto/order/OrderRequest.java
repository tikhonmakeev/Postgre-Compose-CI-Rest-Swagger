package com.example.dto.order;

import com.example.dto.orderItem.OrderItemRequest;
import com.example.models.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotEmpty(message = "Order date cannot be empty")
    private LocalDateTime orderDate;

    @NotEmpty(message = "Order status cannot be empty")
    private OrderStatus status;
}
