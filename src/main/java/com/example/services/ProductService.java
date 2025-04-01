package com.example.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.repositories.ProductRepository;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

}
