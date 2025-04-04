package com.example.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.models.Order;
import com.example.models.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderRepositoryImplTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void testSaveOrder() {
        Order order = new Order();
        order.setUserId(1L);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PROCESSING);

        long orderId = orderRepository.save(order);

        Optional<Order> savedOrder = orderRepository.findById(orderId);
        assertTrue(savedOrder.isPresent());
        assertEquals(order.getStatus(), savedOrder.get().getStatus());
    }

    @Test
    void testFindByUserId() {
        List<Order> orders = orderRepository.findByUserId(1L);
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
    }
}
