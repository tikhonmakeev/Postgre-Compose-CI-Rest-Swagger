package com.example.repositories.impl;

import com.example.models.Product;
import com.example.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<Product> findByCategory(String category) {
        return List.of();
    }

    @Override
    public List<Product> findByPriceBetween(float minPrice, float maxPrice) {
        return List.of();
    }

    @Override
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public Optional<Product> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public long save(Product entity) {
        return 0;
    }

    @Override
    public void update(Product entity) {

    }

    @Override
    public void deleteById(long id) {

    }
}
