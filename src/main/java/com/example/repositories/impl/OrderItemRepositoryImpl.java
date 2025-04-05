package com.example.repositories.impl;

import com.example.dto.orderItem.OrderItemWithPrice;
import com.example.models.OrderItem;
import com.example.repositories.OrderItemRepository;
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
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<OrderItem> orderItemRowMapper = (rs, rowNum) -> new OrderItem(
            rs.getLong("id"),
            rs.getLong("order_id"),
            rs.getLong("product_id"),
            rs.getInt("quantity"),
            rs.getFloat("price")
    );

    @Override
    public void addItemsToOrder(Long orderId, List<OrderItem> items) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        for (OrderItem item : items) {
            jdbcTemplate.update(sql,
                    orderId,
                    item.getProductId(),
                    item.getQuantity(),
                    item.getPrice()
            );
        }
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        return jdbcTemplate.query(sql, orderItemRowMapper, orderId);
    }

    @Override
    public boolean existsById(long id) {
        String sql = "SELECT COUNT(*) FROM order_items WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public Optional<OrderItem> findById(long id) {
        try {
            String sql = "SELECT * FROM order_items WHERE id = ?";
            OrderItem orderItem = jdbcTemplate.queryForObject(sql, orderItemRowMapper, id);
            return Optional.ofNullable(orderItem);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderItem> findAll() {
        String sql = "SELECT * FROM order_items";
        return jdbcTemplate.query(sql, orderItemRowMapper);
    }

    public long save(OrderItemWithPrice entity) {
        String sql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(2, entity.getProductId());
            ps.setInt(3, entity.getQuantity());
            ps.setFloat(4, entity.getPrice());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void update(OrderItem entity) {
        String sql = "UPDATE order_items SET order_id = ?, product_id = ?, quantity = ?, price = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                entity.getOrderId(),
                entity.getProductId(),
                entity.getQuantity(),
                entity.getPrice(),
                entity.getId());
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM order_items WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}