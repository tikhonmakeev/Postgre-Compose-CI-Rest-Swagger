package com.example.dto;

import com.example.models.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderDto {

    @NotNull
    private long id;
    private long userId;
    private LocalDateTime orderDate;
    private OrderStatus status;
}