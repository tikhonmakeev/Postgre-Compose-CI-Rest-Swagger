package com.example.repositories;

import com.example.dto.orderItem.OrderItemWithPrice;
import com.example.models.OrderItem;

import java.util.List;

public interface OrderItemRepository extends CrudRepository<OrderItem> {
    void addItemsToOrder(Long orderId, List<OrderItem> items);
    List<OrderItem> findByOrderId(Long orderId);
    public long save(OrderItemWithPrice entity);
}
