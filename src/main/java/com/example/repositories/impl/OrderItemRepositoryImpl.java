package com.example.repositories.impl;

import com.example.models.OrderItem;
import com.example.repositories.OrderItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addItemsToOrder(Long orderId, List<OrderItem> items) {

    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        return List.of();
    }

    @Override
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public Optional<OrderItem> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<OrderItem> findAll() {
        return List.of();
    }

    @Override
    public long save(OrderItem entity) {
        return 0;
    }

    @Override
    public void update(OrderItem entity) {

    }

    @Override
    public void deleteById(long id) {

    }
}
