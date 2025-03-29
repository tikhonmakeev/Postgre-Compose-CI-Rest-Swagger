package com.example.repositories;

import com.example.models.OrderItem;

import java.util.List;

public interface OrderItemRepository {
    void addItemToOrder(int orderId, int productId, int quantity, float price);

    void updateOrderItemQuantity(int orderItemId, int newQuantity);

    void removeItemFromOrder(int orderItemId);

    OrderItem getOrderItemById(int orderItemId);

    List<OrderItem> getItemsForOrder(int orderId);
}
