package com.example.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @NotNull
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotNull
    private float price;
    @NotBlank
    private String category;
}
