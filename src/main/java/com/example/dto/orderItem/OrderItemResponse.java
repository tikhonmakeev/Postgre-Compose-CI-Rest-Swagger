package com.example.dto.orderItem;

import lombok.Data;

@Data
public class OrderItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private float unitPrice;
    private float totalPrice;
}