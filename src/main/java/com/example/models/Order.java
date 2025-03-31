package com.example.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @NotNull
    private long id;
    @NotNull
    private long userId;
    @NotNull
    private LocalDateTime orderDate;
    @NotBlank
    private OrderStatus status;
}