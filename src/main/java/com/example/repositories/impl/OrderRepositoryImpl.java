package com.example.repositories.impl;

import com.example.models.Order;
import com.example.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createOrder(int userId, String orderDate, String status) {

    }

    @Override
    public void updateOrderStatus(int orderId, String newStatus) {

    }

    @Override
    public void deleteOrder(int orderId) {

    }

    @Override
    public Order getOrderById(int orderId) {
        return null;
    }

    @Override
    public List<Order> getOrdersByUserId(int userId) {
        return List.of();
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return List.of();
    }
}
