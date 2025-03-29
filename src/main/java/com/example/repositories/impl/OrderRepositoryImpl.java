package com.example.repositories.impl;

import com.example.models.Order;
import com.example.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Order> orderRowMapper = (rs, rowNum) -> new Order(
            rs.getLong("id"),
            rs.getLong("user_id"),
            rs.getTimestamp("order_date").toLocalDateTime(),
            rs.getString("status")
    );

    @Override
    public void updateStatus(Long orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        jdbcTemplate.update(sql, status, orderId);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ?";
        return jdbcTemplate.query(sql, orderRowMapper, userId);
    }

    @Override
    public boolean existsById(long id) {
        String sql = "SELECT COUNT(*) FROM orders WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public Optional<Order> findById(long id) {
        try {
            String sql = "SELECT * FROM orders WHERE id = ?";
            Order order = jdbcTemplate.queryForObject(sql, orderRowMapper, id);
            return Optional.ofNullable(order);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT * FROM orders";
        return jdbcTemplate.query(sql, orderRowMapper);
    }

    @Override
    public long save(Order entity) {
        String sql = "INSERT INTO orders (user_id, order_date, status) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, entity.getUserId());
            ps.setTimestamp(2, Timestamp.valueOf(entity.getOrderDate()));
            ps.setString(3, entity.getStatus());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void update(Order entity) {
        String sql = "UPDATE orders SET user_id = ?, order_date = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                entity.getUserId(),
                Timestamp.valueOf(entity.getOrderDate()),
                entity.getStatus(),
                entity.getId());
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}