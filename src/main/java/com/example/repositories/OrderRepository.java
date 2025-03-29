package com.example.repositories;

import com.example.models.Order;

import java.util.List;

public interface OrderRepository {
    void createOrder(int userId, String orderDate, String status);

    void updateOrderStatus(int orderId, String newStatus);

    void deleteOrder(int orderId);

    Order getOrderById(int orderId);

    List<Order> getOrdersByUserId(int userId);

    List<Order> getOrdersByStatus(String status);
}
