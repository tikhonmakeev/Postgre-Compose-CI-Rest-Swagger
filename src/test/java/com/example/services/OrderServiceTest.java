package com.example.services;

import com.example.dto.orderItem.OrderItemRequest;
import com.example.models.*;
import com.example.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_ShouldSuccess() {
        long  userId = 1L;
        long  productId = 10L;
        long  orderId = 100L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);
        product.setPrice(100.0F);

        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductId(productId);
        orderItemRequest.setQuantity(2);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenReturn(orderId);

        Order result = orderService.createOrder(userId, List.of(orderItemRequest));

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(OrderStatus.NEW, result.getStatus());
        assertNotNull(result.getOrderDate());
        assertEquals(orderId, result.getId());

        verify(userRepository).existsById(userId);
        verify(productRepository).findById(productId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void createOrder_ShouldThrowWhenUserNotFound() {
        long  userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> orderService.createOrder(userId, Collections.emptyList()));

        verify(userRepository).existsById(userId);
        verifyNoInteractions(productRepository, orderRepository, orderItemRepository);
    }

    @Test
    void updateOrderStatus_ShouldSuccess() {
        long  orderId = 1L;
        OrderStatus newStatus = OrderStatus.PROCESSING;

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setStatus(OrderStatus.NEW);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(existingOrder)).thenReturn(orderId);

        Order result = orderService.updateOrderStatus(orderId, newStatus);

        assertEquals(newStatus, result.getStatus());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(existingOrder);
    }

    @Test
    void updateOrderStatus_ShouldThrowWhenOrderNotFound() {
        long  orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.updateOrderStatus(orderId, OrderStatus.PROCESSING));

        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void getUserOrders_ShouldReturnOrders() {
        long  userId = 1L;
        List<Order> expectedOrders = List.of(new Order(), new Order());

        when(userRepository.existsById(userId)).thenReturn(true);
        when(orderRepository.findByUserId(userId)).thenReturn(expectedOrders);

        List<Order> result = orderService.getUserOrders(userId);

        assertEquals(expectedOrders.size(), result.size());
        verify(userRepository).existsById(userId);
        verify(orderRepository).findByUserId(userId);
    }

    @Test
    void getUserOrders_ShouldThrowWhenUserNotFound() {
        long  userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> orderService.getUserOrders(userId));

        verify(userRepository).existsById(userId);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void createOrder_ShouldSuccessWithoutItems() {
        long  userId = 1L;
        long  orderId = 100L;

        User user = new User();
        user.setId(userId);

        when(userRepository.existsById(userId)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(orderId);

        Order result = orderService.createOrder(userId, Collections.emptyList());

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(OrderStatus.NEW, result.getStatus());
        assertNotNull(result.getOrderDate());

        verify(userRepository).existsById(userId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void deleteOrder_ShouldSuccess() {
        long  orderId = 1L;

        when(orderRepository.existsById(orderId)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(orderId);

        orderService.deleteOrderById(orderId);

        verify(orderRepository).existsById(orderId);
        verify(orderRepository).deleteById(orderId);
    }

    @Test
    void deleteOrder_ShouldThrowWhenOrderNotFound() {
        long  orderId = 1L;
        when(orderRepository.existsById(orderId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> orderService.deleteOrderById(orderId));

        verify(orderRepository).existsById(orderId);
    }

    @Test
    void addItemsToOrder_ShouldThrowWhenOrderDoesNotExist() {
        long  orderId = 1L;
        long  productId = 10L;
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setProductId(productId);
        orderItemRequest.setQuantity(1);

        when(orderRepository.existsById(orderId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> orderService.addItemsToOrder(orderId, List.of(orderItemRequest)));

        verify(orderRepository).existsById(orderId);
        verifyNoInteractions(productRepository, orderItemRepository);
    }

}