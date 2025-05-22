package com.example.services;

import com.example.dto.order.OrderCreate;
import com.example.dto.order.OrderRequest;
import com.example.dto.order.OrderResponse;
import com.example.dto.orderItem.OrderItemRequest;
import com.example.dto.orderItem.OrderItemResponse;
import com.example.models.*;
import com.example.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final OrderItemService orderItemService;

    @Transactional
    public List<OrderItemResponse> addItemsToOrder(long orderId, List<OrderItemRequest> items) {
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();

        for (OrderItemRequest item : items) {
            orderItemResponses.add(orderItemService.addOrderItem(orderId, item));
        }

        return orderItemResponses;
    }

    @Transactional
    public OrderResponse createOrderWithItems(OrderRequest orderRequest) {
        OrderCreate orderCreate = OrderCreate.builder()
                .userId(orderRequest.getUserId())
                .orderDate(orderRequest.getOrderDate())
                .build();

        long savedOrderId = orderRepository.save(orderCreate);

        List<OrderItemResponse> orderItemResponses = addItemsToOrder(savedOrderId, orderRequest.getItems());

        OrderResponse orderResponse = OrderResponse.builder()
                .userId(orderRequest.getUserId())
                .id(savedOrderId)
                .status(OrderStatus.NEW)
                .orderDate(LocalDateTime.now())
                .items(orderItemResponses)
                .build();

        return orderResponse;
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
        orderRepository.update(order);
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