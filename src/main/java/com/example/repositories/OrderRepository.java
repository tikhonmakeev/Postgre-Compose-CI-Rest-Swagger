package com.example.repositories;

import com.example.dto.order.OrderCreate;
import com.example.dto.order.OrderRequest;
import com.example.models.Order;
import com.example.models.OrderStatus;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order>  {
    void updateStatus(Long orderId, OrderStatus status);
    List<Order> findByUserId(Long userId);
    long save(OrderCreate orderCreate);
}
