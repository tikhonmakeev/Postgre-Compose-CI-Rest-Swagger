package com.example.services;

import com.example.dto.order.OrderCreate;
import com.example.dto.order.OrderRequest;
import com.example.dto.orderItem.OrderItemRequest;
import com.example.models.*;
import com.example.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private static final String USER_NOT_FOUND = "User not found with id: ";
    private static final String PRODUCT_NOT_FOUND = "Product not found with id: ";
    private static final String ORDER_NOT_FOUND = "Order not found with id: ";

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;

    @Transactional
    public Order createOrder(OrderCreate orderCreate) {
        long savedOrderId = orderRepository.save(orderCreate);
        Order order = Order.builder()
                .userId(savedOrderId)
                .id(savedOrderId)
                .status(OrderStatus.NEW)
                .orderDate(LocalDateTime.now())
                .build();

        return order;
    }

    @Transactional
    public void deleteOrderById(long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new NotFoundException(ORDER_NOT_FOUND + orderId);
        }
        orderRepository.deleteById(orderId);
    }

    @Transactional(readOnly = true)
    public Order getOrderById(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND + orderId));
    }

    @Transactional
    public Order updateOrderStatus(long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        orderRepository.save(order);
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> getUserOrders(long userId) {
        validateUserExists(userId);
        return orderRepository.findByUserId(userId);
    }

    private Order buildNewOrder(long userId) {
        return Order.builder()
                .userId(userId)
                .status(OrderStatus.NEW)
                .orderDate(LocalDateTime.now())
                .build();
    }


    private Product getProduct(long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND + productId));
    }

    private void validateUserExists(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND + userId);
        }
    }

    private void validateOrderExists(long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new NotFoundException(ORDER_NOT_FOUND + orderId);
        }
    }
}