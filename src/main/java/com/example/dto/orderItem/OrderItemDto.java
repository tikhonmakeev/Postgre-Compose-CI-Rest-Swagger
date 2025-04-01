package com.example.dto.orderItem;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderItemDto {

    @NotNull
    private long id;
    private long orderId;
    private long productId;
    private int quantity;
    private float price;
}