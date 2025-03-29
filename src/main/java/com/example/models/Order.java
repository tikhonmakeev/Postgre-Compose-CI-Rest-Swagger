package com.example.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Order {

    @NotNull
    private long id;
    @NotNull
    private long userId;
    @NotBlank
    private String order_date;
    @NotBlank
    private OrderStatus status;
}