package com.example.services;

import com.example.dto.orderItem.OrderItemDto;
import com.example.models.*;
import com.example.repositories.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
        Long userId = 1L;
        Long productId = 10L;
        Long orderId = 100L;

        User user = new User();
        user.setId(userId);

        Product product = new Product();
        product.setId(productId);
        product.setPrice(new BigDecimal("100.0"));

        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setProductId(productId);
        itemDto.setQuantity(2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(orderId);
            return order;
        });

        Order result = orderService.createOrder(userId, List.of(itemDto));

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(OrderStatus.NEW, result.getStatus());
        assertNotNull(result.getOrderDate());

        verify(userRepository).findById(userId);
        verify(productRepository).findById(productId);
        verify(orderRepository).save(any(Order.class));
        verify(orderItemRepository).addItemsToOrder(eq(orderId), anyList());
    }

    @Test
    void createOrder_ShouldThrowWhenUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(userId, Collections.emptyList());
        });

        verify(userRepository).findById(userId);
        verifyNoInteractions(productRepository, orderRepository, orderItemRepository);
    }

    @Test
    void createOrder_ShouldThrowWhenProductNotFound() {
        Long userId = 1L;
        Long productId = 10L;

        User user = new User();
        user.setId(userId);

        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setProductId(productId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(userId, List.of(itemDto));
        });

        verify(userRepository).findById(userId);
        verify(productRepository).findById(productId);
        verifyNoInteractions(orderRepository, orderItemRepository);
    }

    @Test
    void updateOrderStatus_ShouldSuccess() {
        Long orderId = 1L;
        OrderStatus newStatus = OrderStatus.PROCESSING;

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setStatus(OrderStatus.NEW);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        doNothing().when(orderRepository).update(any(Order.class));

        Order result = orderService.updateOrderStatus(orderId, newStatus);

        assertEquals(newStatus, result.getStatus());
        verify(orderRepository).findById(orderId);
        verify(orderRepository).update(existingOrder);
    }

    @Test
    void updateOrderStatus_ShouldThrowWhenOrderNotFound() {
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.updateOrderStatus(orderId, OrderStatus.PROCESSING);
        });

        verify(orderRepository).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void getUserOrders_ShouldReturnOrders() {
        Long userId = 1L;
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
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.getUserOrders(userId);
        });

        verify(userRepository).existsById(userId);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void addItemsToOrder_ShouldSaveAllItems() {
        Long orderId = 1L;
        Long productId = 10L;

        Product product = new Product();
        product.setId(productId);
        product.setPrice(50.0F);

        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setProductId(productId);
        itemDto.setQuantity(3);

        when(orderRepository.existsById(orderId)).thenReturn(true);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        orderService.addItemsToOrder(orderId, List.of(itemDto));

        verify(orderRepository).existsById(orderId);
        verify(productRepository).findById(productId);
        verify(orderItemRepository).addItemsToOrder(eq(orderId), anyList());
    }

    @Test
    void addItemsToOrder_ShouldThrowWhenOrderNotFound() {
        Long orderId = 1L;

        when(orderRepository.existsById(orderId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.addItemsToOrder(orderId, Collections.emptyList());
        });

        verify(orderRepository).existsById(orderId);
        verifyNoInteractions(productRepository, orderItemRepository);
    }

    @Test
    void addItemsToOrder_ShouldThrowWhenProductNotFound() {
        Long orderId = 1L;
        Long productId = 10L;

        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setProductId(productId);

        when(orderRepository.existsById(orderId)).thenReturn(true);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.addItemsToOrder(orderId, List.of(itemDto));
        });

        verify(orderRepository).existsById(orderId);
        verify(productRepository).findById(productId);
        verifyNoInteractions(orderItemRepository);
    }
}