package com.example.repositories;

import com.example.dto.product.ProductRequest;
import com.example.models.Product;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product> {
    List<Product> findByCategory(String category);
    List<Product> findByPriceBetween(float minPrice, float maxPrice);
    public long save(ProductRequest entity);
    void update(Product productRequest);
}
