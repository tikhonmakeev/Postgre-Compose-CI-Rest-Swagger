package com.example.dto.orderItem;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderItemResponse {
    private Long id;
    private Long orderId;
    private Long productId;
    private String productName;
    private int quantity;
    private float unitPrice;
    private float totalPrice;
}