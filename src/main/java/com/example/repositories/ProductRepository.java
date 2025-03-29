package com.example.repositories;

import com.example.models.Product;

import java.util.List;

public interface ProductRepository{
    void createProduct(String name, String description, float price, String category);

    void updateProduct(int id, String newName, String newDescription, float newPrice, String newCategory);

    void deleteProduct(int id);

    Product getProductById(int id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByPriceRange(float minPrice, float maxPrice);
}
