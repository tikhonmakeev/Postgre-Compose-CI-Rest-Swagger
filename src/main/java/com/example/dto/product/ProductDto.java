package com.example.dto.product;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductDto {

    @NotNull
    private long id;
    private String name;
    private String description;
    private float price;
    private String category;
}
