package com.example.controllers;

import com.example.models.Product;
import com.example.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("666.666"));
        product.setCategory("Electronics");
        product.setDescription("Test");
    }

    @Test
    void getAllProducts_Products() {
        List<Product> products = Arrays.asList(product);
        String category = "Electronics";
        BigDecimal minPrice = new BigDecimal("50.0");
        BigDecimal maxPrice = new BigDecimal("150.0");

        when(productService.getAllProducts(category, minPrice, maxPrice)).thenReturn(products);
        ResponseEntity<List<Product>> response = productController.getAllProducts(category, minPrice.floatValue(), maxPrice.floatValue());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
        verify(productService).getAllProducts(category, minPrice, maxPrice);
    }

    @Test
    void getProductById_Product() {
        Long productId = 1L;
        when(productService.getProductById(productId)).thenReturn(Optional.of(product));
        ResponseEntity<Product> response = productController.getProductById(productId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService).getProductById(productId);
    }

    @Test
    void getProductById_NotFound() {
        Long productId = 1L;
        when(productService.getProductById(productId)).thenReturn(Optional.empty());
        ResponseEntity<Product> response = productController.getProductById(productId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).getProductById(productId);
    }

    @Test
    void createProduct_CreatedProduct() {
        when(productService.createProduct(any(Product.class))).thenReturn(product);
        ResponseEntity<Product> response = productController.createProduct(product);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService).createProduct(product);
    }

    @Test
    void updateProduct_Product() {
        Long productId = 1L;
        when(productService.updateProduct(eq(productId), any(Product.class))).thenReturn(Optional.of(product));
        ResponseEntity<Product> response = productController.updateProduct(productId, product);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
        verify(productService).updateProduct(productId, product);
    }

    @Test
    void updateProduct_NotFound() {
        Long productId = 1L;
        when(productService.updateProduct(eq(productId), any(Product.class))).thenReturn(Optional.empty());
        ResponseEntity<Product> response = productController.updateProduct(productId, product);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).updateProduct(productId, product);
    }

    @Test
    void deleteProduct_NoContent() {
        Long productId = 1L;
        when(productService.deleteProduct(productId)).thenReturn(true);
        ResponseEntity<Void> response = productController.deleteProduct(productId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).deleteProduct(productId);
    }

    @Test
    void deleteProduct_NotFound() {
        Long productId = 1L;
        when(productService.deleteProduct(productId)).thenReturn(false);
        ResponseEntity<Void> response = productController.deleteProduct(productId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productService).deleteProduct(productId);
    }
}