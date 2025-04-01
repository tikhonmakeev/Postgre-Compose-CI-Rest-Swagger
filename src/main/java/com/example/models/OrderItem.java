package com.example.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @NotNull
    private long id;
    @NotNull
    private long orderId;
    @NotNull
    private long productId;
    @Positive
    private int quantity;
    @Positive
    private float price;
}