package com.example.repositories.impl;

import com.example.models.OrderItem;
import com.example.repositories.OrderItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addItemToOrder(int orderId, int productId, int quantity, float price) {

    }

    @Override
    public void updateOrderItemQuantity(int orderItemId, int newQuantity) {

    }

    @Override
    public void removeItemFromOrder(int orderItemId) {

    }

    @Override
    public OrderItem getOrderItemById(int orderItemId) {
        return null;
    }

    @Override
    public List<OrderItem> getItemsForOrder(int orderId) {
        return List.of();
    }
}
