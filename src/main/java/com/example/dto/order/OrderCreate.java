package com.example.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderCreate {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotEmpty(message = "Order date cannot be empty")
    private LocalDateTime orderDate;
}