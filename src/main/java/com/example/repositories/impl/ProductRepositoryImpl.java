package com.example.repositories.impl;

import com.example.models.Product;
import com.example.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Product> productRowMapper = (rs, rowNum) -> new Product(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getBigDecimal("price"),
            rs.getString("category")
    );

    @Override
    public List<Product> findByCategory(String category) {
        String sql = "SELECT * FROM products WHERE category = ?";
        return jdbcTemplate.query(sql, productRowMapper, category);
    }

    @Override
    public List<Product> findByPriceBetween(float minPrice, float maxPrice) {
        String sql = "SELECT * FROM products WHERE price BETWEEN ? AND ?";
        return jdbcTemplate.query(sql, productRowMapper, minPrice, maxPrice);
    }

    @Override
    public boolean existsById(long id) {
        String sql = "SELECT COUNT(*) FROM products WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public Optional<Product> findById(long id) {
        try {
            String sql = "SELECT * FROM products WHERE id = ?";
            Product product = jdbcTemplate.queryForObject(sql, productRowMapper, id);
            return Optional.ofNullable(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Product> findAll() {
        String sql = "SELECT * FROM products";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    @Override
    public long save(Product entity) {
        String sql = "INSERT INTO products (name, description, price, category) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getName());
            ps.setString(2, entity.getDescription());
            ps.setBigDecimal(3, entity.getPrice());
            ps.setString(4, entity.getCategory());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void update(Product entity) {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, category = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCategory(),
                entity.getId());
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}