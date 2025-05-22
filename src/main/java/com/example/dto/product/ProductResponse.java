package com.example.dto.product;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private float price;
    private String category;
    private LocalDateTime createdAt;
}