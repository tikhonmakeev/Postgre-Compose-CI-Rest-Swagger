package com.example.repositories;

import com.example.models.Order;
import com.example.models.Product;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order>  {
    void updateStatus(Long orderId, String status);
    List<Order> findByUserId(Long userId);
}
