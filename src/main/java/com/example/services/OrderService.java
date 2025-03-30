package com.example.services;

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
    public Order createOrder(Long userId, List<OrderItemRequest> items) {
        validateUserExists(userId);

        Order order = buildNewOrder(userId);
        Order savedOrder = saveOrder(order);
        addItemsToOrder(savedOrder.getId(), items);

        return savedOrder;
    }

    @Transactional(readOnly = true)
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(ORDER_NOT_FOUND + orderId));
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        orderRepository.save(order);
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> getUserOrders(Long userId) {
        validateUserExists(userId);
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public void addItemsToOrder(Long orderId, List<OrderItemRequest> items) {
        validateOrderExists(orderId);
        items.forEach(item -> addOrderItem(orderId, item));
    }

    private Order buildNewOrder(Long userId) {
        return Order.builder()
                .userId(userId)
                .status(OrderStatus.NEW)
                .orderDate(LocalDateTime.now())
                .build();
    }

    private Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    private void addOrderItem(Long orderId, OrderItemRequest item) {
        Product product = getProduct(item.productId());
        OrderItem orderItem = buildOrderItem(orderId, item, product);
        orderItemRepository.save(orderItem);
    }

    private OrderItem buildOrderItem(Long orderId, OrderItemRequest item, Product product) {
        return OrderItem.builder()
                .orderId(orderId)
                .productId(product.getId())
                .quantity(item.quantity())
                .price(product.getPrice())
                .build();
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND + productId));
    }

    private void validateUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND + userId);
        }
    }

    private void validateOrderExists(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new NotFoundException(ORDER_NOT_FOUND + orderId);
        }
    }
}