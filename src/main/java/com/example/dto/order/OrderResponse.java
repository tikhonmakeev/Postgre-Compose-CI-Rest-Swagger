package com.example.dto.order;

import com.example.dto.orderItem.OrderItemResponse;
import com.example.models.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private Long userId;
    private String userName;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
}