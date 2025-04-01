package com.example.services;

import com.example.models.Product;
import com.example.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> getAllProducts(String category, BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> products = productRepository.findAll();
        List<Product> filteredProducts = new ArrayList<>(products);

        if (category != null && !category.isEmpty()) {
            List<Product> categoryFiltered = new ArrayList<>();
            for (Product product : filteredProducts) {
                if (category.equalsIgnoreCase(product.getCategory())) {
                    categoryFiltered.add(product);
                }
            }
            filteredProducts = categoryFiltered;
        }

        if (minPrice != null) {
            List<Product> minPriceFiltered = new ArrayList<>();
            for (Product product : filteredProducts) {
                if (product.getPrice().compareTo(minPrice) >= 0) {
                    minPriceFiltered.add(product);
                }
            }
            filteredProducts = minPriceFiltered;
        }

        if (maxPrice != null) {
            List<Product> maxPriceFiltered = new ArrayList<>();
            for (Product product : filteredProducts) {
                if (product.getPrice().compareTo(maxPrice) <= 0) {
                    maxPriceFiltered.add(product);
                }
            }
            filteredProducts = maxPriceFiltered;
        }

        return filteredProducts;
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