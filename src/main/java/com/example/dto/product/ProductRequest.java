package com.example.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Name is required")
    private String description;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0.01")
    private float price;

    @NotBlank(message = "Category is required")
    private String category;
}