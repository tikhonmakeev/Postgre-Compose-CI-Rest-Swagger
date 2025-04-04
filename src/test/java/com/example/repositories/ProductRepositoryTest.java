package com.example.repositories;

import com.example.models.Product;
import com.example.repositories.impl.ProductRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ProductRepositoryImpl productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, "Test Product", "Description", 666.666f, "Electronics");
    }

    @Test
    void existsById_True() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(1L))).thenReturn(1);

        boolean result = productRepository.existsById(1L);

        assertTrue(result);
        verify(jdbcTemplate).queryForObject(eq("SELECT COUNT(*) FROM products WHERE id = ?"), eq(Integer.class), eq(1L));
    }

    @Test
    void existsById_False() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(1L))).thenReturn(0);

        boolean result = productRepository.existsById(1L);

        assertFalse(result);
        verify(jdbcTemplate).queryForObject(eq("SELECT COUNT(*) FROM products WHERE id = ?"), eq(Integer.class), eq(1L));
    }

    @Test
    void findById_Found() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1L))).thenReturn(product);

        Optional<Product> result = productRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(product, result.get());
        verify(jdbcTemplate).queryForObject(eq("SELECT * FROM products WHERE id = ?"), any(RowMapper.class), eq(1L));
    }

    @Test
    void findById_NotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1L)))
                .thenThrow(new EmptyResultDataAccessException(1));

        Optional<Product> result = productRepository.findById(1L);

        assertFalse(result.isPresent());
        verify(jdbcTemplate).queryForObject(eq("SELECT * FROM products WHERE id = ?"), any(RowMapper.class), eq(1L));
    }

    @Test
    void findAll() {
        List<Product> products = Arrays.asList(product);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(products);

        List<Product> result = productRepository.findAll();

        assertEquals(products, result);
        verify(jdbcTemplate).query(eq("SELECT * FROM products"), any(RowMapper.class));
    }

    @Test
    void findByCategory() {
        List<Product> products = Arrays.asList(product);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq("Electronics"))).thenReturn(products);

        List<Product> result = productRepository.findByCategory("Electronics");

        assertEquals(products, result);
        verify(jdbcTemplate).query(eq("SELECT * FROM products WHERE category = ?"), any(RowMapper.class), eq("Electronics"));
    }

    @Test
    void save_ShouldReturnGeneratedId() {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        doAnswer(invocation -> {
            KeyHolder kh = invocation.getArgument(1);
            kh.getKeyList().add(Collections.singletonMap("id", 1L));
            return 1;
        }).when(jdbcTemplate).update(any(), any(KeyHolder.class));

        long id = productRepository.save(product);

        assertEquals(1L, id);
        verify(jdbcTemplate).update(any(), any(KeyHolder.class));
    }

    @Test
    void update() {
        productRepository.update(product);

        verify(jdbcTemplate).update(
                eq("UPDATE products SET name = ?, description = ?, price = ?, category = ? WHERE id = ?"),
                eq(product.getName()),
                eq(product.getDescription()),
                eq(product.getPrice()),
                eq(product.getCategory()),
                eq(product.getId())
        );
    }

    @Test
    void deleteById() {
        productRepository.deleteById(1L);

        verify(jdbcTemplate).update(eq("DELETE FROM products WHERE id = ?"), eq(1L));
    }
}