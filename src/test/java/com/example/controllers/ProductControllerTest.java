package com.example.controllers;

import com.example.dto.product.ProductRequest;
import com.example.models.Product;
import com.example.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Product testProduct;
    private ProductRequest testProductRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();

        testProduct = Product.builder()
                .id(1L)
                .name("prod test name")
                .description("some test in descr")
                .price(100.0f)
                .category("category")
                .build();

        testProductRequest = new ProductRequest(
                "prod test name",
                "some test in descr",
                100.0f,
                "category");
    }

    @Test
    void getAllProducts_ShouldReturnProducts() throws Exception {
        when(productService.getAllProducts(null, null, null))
                .thenReturn(Collections.singletonList(testProduct));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getAllProducts_WithCategoryFilter_ShouldReturnFilteredProducts() throws Exception {
        when(productService.getAllProducts("Electronics", null, null))
                .thenReturn(Collections.singletonList(testProduct));

        mockMvc.perform(get("/api/products")
                        .param("category", "Electronics"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllProducts_WithPriceFilters_ShouldReturnFilteredProducts() throws Exception {
        when(productService.getAllProducts(null, 100L, 200L))
                .thenReturn(Collections.singletonList(testProduct));

        mockMvc.perform(get("/api/products")
                        .param("minPrice", "100")
                        .param("maxPrice", "200"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllProducts_WithAllFilters_ShouldReturnFilteredProducts() throws Exception {
        when(productService.getAllProducts("Electronics", 100L, 200L))
                .thenReturn(Collections.singletonList(testProduct));

        mockMvc.perform(get("/api/products")
                        .param("category", "Electronics")
                        .param("minPrice", "100")
                        .param("maxPrice", "200"))
                .andExpect(status().isOk());
    }
    @Test
    void getAllProducts_WithFilters_ShouldReturnFilteredProducts() throws Exception {
        Product filteredProduct = Product.builder()
                .id(2L)
                .name("Filtered Product")
                .price(150.0f)
                .category("Electronics")
                .build();

        when(productService.getAllProducts("Electronics", 100L, 200L))
                .thenReturn(Collections.singletonList(filteredProduct));

        mockMvc.perform(get("/api/products")
                        .param("category", "Electronics")
                        .param("minPrice", "100")
                        .param("maxPrice", "200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Filtered Product"))
                .andExpect(jsonPath("$[0].price").value(150.0));

        verify(productService, times(1)).getAllProducts("Electronics", 100L, 200L);
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        when(productService.createProduct(any(ProductRequest.class))).thenReturn(testProduct);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProductRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("prod test name"))
                .andExpect(jsonPath("$.price").value(100.0));

        verify(productService, times(1)).createProduct(any(ProductRequest.class));
    }

    @Test
    void createProduct_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        ProductRequest invalidRequest = new ProductRequest("", "", -100.0f, "");

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).createProduct(any());
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.of(testProduct));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("prod test name"));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void getProductById_WhenProductNotExists_ShouldReturnNotFound() throws Exception {
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() throws Exception {
        Product updatedProduct = Product.builder()
                .id(1L)
                .name("some new prod name")
                .description("new descr")
                .price(150.0f)
                .category("new category")
                .build();

        when(productService.updateProduct(eq(1L), any(Product.class)))
                .thenReturn(Optional.of(updatedProduct));

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("some new prod name"))
                .andExpect(jsonPath("$.description").value("new descr"))
                .andExpect(jsonPath("$.price").value(150.0))
                .andExpect(jsonPath("$.category").value("new category"));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void updateProduct_WhenProductNotExists_ShouldReturnNotFound() throws Exception {
        when(productService.updateProduct(eq(1L), any(Product.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldReturnNoContent() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }

    @Test
    void deleteProduct_WhenProductNotExists_ShouldReturnNotFound() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).deleteProduct(1L);
    }
}