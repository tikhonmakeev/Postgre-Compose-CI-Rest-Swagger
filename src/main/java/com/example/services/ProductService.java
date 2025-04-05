package com.example.services;

import com.example.models.Product;
import com.example.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> getAllProducts(String category, Float minPrice, Float maxPrice) {
        List<Product> products = productRepository.findAll();
        if (category != null && !category.isEmpty()) {
            products = products.stream()
                    .filter(product -> category.equalsIgnoreCase(product.getCategory()))
                    .toList();
        }
        if (minPrice != null) {
            products = products.stream()
                    .filter(product -> product.getPrice() >= minPrice)
                    .toList();
        }
        if (maxPrice != null) {
            products = products.stream()
                    .filter(product -> product.getPrice() <= maxPrice)
                    .toList();
        }

        return products;
    }

    @Transactional(readOnly = true)
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Transactional
    public Product createProduct(Product product) {
        long id = productRepository.save(product);
        product.setId(id);
        return product;
    }
    @Transactional
    public Optional<Product> updateProduct(Long id, Product productDetails) {
        if (!productRepository.existsById(id)) {
            return Optional.empty();
        }
        productDetails.setId(id);
        productRepository.update(productDetails);

        return Optional.of(productDetails);
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }
}