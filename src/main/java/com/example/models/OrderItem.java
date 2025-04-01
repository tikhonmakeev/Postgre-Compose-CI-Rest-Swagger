package com.example.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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