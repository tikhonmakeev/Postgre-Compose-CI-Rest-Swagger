package com.example.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
