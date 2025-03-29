package com.example.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Order {

    @NotNull
    private long id;
    @NotNull
    private long userId;
    @NotNull
    private LocalDateTime orderDate;
    @NotBlank
    private String status;
}