package com.example.models;

import com.example.dto.orderItem.OrderItemResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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