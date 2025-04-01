package com.example.services;

import com.example.dto.orderItem.OrderItemRequest;
import com.example.dto.orderItem.OrderItemResponse;
import com.example.models.Order;
import com.example.models.OrderItem;
import com.example.models.Product;
import com.example.repositories.OrderItemRepository;
import com.example.repositories.OrderRepository;
import com.example.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private static final String ORDER_NOT_FOUND = "Order not found with id: ";
    private static final String PRODUCT_NOT_FOUND = "Product not found with id: ";
    private static final String ORDER_ITEM_NOT_FOUND = "Order item not found with id: ";

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<OrderItemResponse> getOrderItems(long orderId) {
        validateOrderExists(orderId);
        return orderItemRepository.findByOrderId(orderId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderItemResponse getOrderItemById(long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new NotFoundException(ORDER_ITEM_NOT_FOUND + orderItemId));
        return mapToResponse(orderItem);
    }

    @Transactional
    public OrderItemResponse addOrderItem(long orderId, OrderItemRequest request) {
        validateOrderExists(orderId);
        Product product = getProductById(request.getProductId());

        OrderItem orderItem = OrderItem.builder()
                .orderId(orderId)
                .productId(product.getId())
                .quantity(request.getQuantity())
                .price(product.getPrice())
                .build();

        long id = orderItemRepository.save(orderItem);
        orderItem.setId(id);

        return mapToResponse(orderItem);
    }

    @Transactional
    public OrderItemResponse updateOrderItemQuantity(long orderItemId, int quantity) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new NotFoundException(ORDER_ITEM_NOT_FOUND + orderItemId));

        orderItem.setQuantity(quantity);
        orderItemRepository.update(orderItem);

        return mapToResponse(orderItem);
    }

    @Transactional
    public void removeOrderItem(long orderItemId) {
        if (!orderItemRepository.existsById(orderItemId)) {
            throw new NotFoundException(ORDER_ITEM_NOT_FOUND + orderItemId);
        }
        orderItemRepository.deleteById(orderItemId);
    }

    @Transactional(readOnly = true)
    public double calculateOrderTotal(long orderId) {
        validateOrderExists(orderId);
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private void validateOrderExists(long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new NotFoundException(ORDER_NOT_FOUND + orderId);
        }
    }

    private Product getProductById(long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(PRODUCT_NOT_FOUND + productId));
    }

    private OrderItemResponse mapToResponse(OrderItem orderItem) {
        Product product = getProductById(orderItem.getProductId());

        OrderItemResponse response = new OrderItemResponse();
        response.setId(orderItem.getId());
        response.setProductId(orderItem.getProductId());
        response.setProductName(product.getName());
        response.setQuantity(orderItem.getQuantity());
        response.setUnitPrice(orderItem.getPrice());
        response.setTotalPrice(orderItem.getPrice() * orderItem.getQuantity());

        return response;
    }
}