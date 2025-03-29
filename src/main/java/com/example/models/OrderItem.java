package com.example.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItem {

    @NotNull
    private long id;
    @NotNull
    private long orderId;
    @NotNull
    private long productId;
    @NotBlank
    private int quantity;
    @NotBlank
    private float price;
}
