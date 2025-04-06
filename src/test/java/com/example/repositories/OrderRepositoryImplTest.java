package com.example.repositories;

import com.example.dto.order.OrderCreate;
import com.example.dto.order.OrderRequest;
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
        OrderCreate orderCreate = OrderCreate.builder()
                .userId(1L)
                .orderDate(LocalDateTime.now())
                .build();

        long orderId = orderRepository.save(orderCreate);

        Optional<Order> savedOrder = orderRepository.findById(orderId);
        assertTrue(savedOrder.isPresent());
    }

    @Test
    void testFindByUserId() {
        List<Order> orders = orderRepository.findByUserId(1L);
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
    }
}