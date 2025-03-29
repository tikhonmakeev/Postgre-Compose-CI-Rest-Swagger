package com.example.repositories.impl;

import com.example.models.Order;
import com.example.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public void updateStatus(Long orderId, String status) {

    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return List.of();
    }

    @Override
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public Optional<Order> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<Order> findAll() {
        return List.of();
    }

    @Override
    public long save(Order entity) {
        return 0;
    }

    @Override
    public void update(Order entity) {

    }

    @Override
    public void deleteById(long id) {

    }
}
