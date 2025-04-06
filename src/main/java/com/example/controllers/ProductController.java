package com.example.controllers;

import com.example.dto.product.ProductRequest;
import com.example.models.Product;
import com.example.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(description = "Get all products with optional filters")
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) long minPrice,
            @RequestParam(required = false) long maxPrice) {
        List<Product> products = productService.getAllProducts(category, minPrice, maxPrice);
        return ResponseEntity.ok(products);
    }

    @Operation(description = "Get product by id")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(description = "Create new product")
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        Product savedProduct = productService.createProduct(productRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    @Operation(description = "Update existing product by id")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {
        return productService.updateProduct(id, product)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(description = "Delete product by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}