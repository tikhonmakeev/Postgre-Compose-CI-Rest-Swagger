package com.example.repositories.impl;

import com.example.models.Product;
import com.example.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createProduct(String name, String description, float price, String category) {

    }

    @Override
    public void updateProduct(int id, String newName, String newDescription, float newPrice, String newCategory) {

    }

    @Override
    public void deleteProduct(int id) {

    }

    @Override
    public Product getProductById(int id) {
        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        return List.of();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return List.of();
    }

    @Override
    public List<Product> getProductsByPriceRange(float minPrice, float maxPrice) {
        return List.of();
    }
}
