package com.example.repositories.impl;

import com.example.dto.order.OrderCreate;
import com.example.models.Order;
import com.example.models.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderRepositoryImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private OrderRepositoryImpl orderRepository;

    private final LocalDateTime testDateTime = LocalDateTime.now();
    private final Order testOrder = new Order(1L, 1L, testDateTime, OrderStatus.NEW);
    private final OrderCreate testOrderCreate = OrderCreate.builder()
            .userId(1L)
            .orderDate(testDateTime)
            .build();

    @Test
    void updateStatus() {
        Long orderId = 1L;
        OrderStatus newStatus = OrderStatus.COMPLETED;

        orderRepository.updateStatus(orderId, newStatus);

        verify(jdbcTemplate).update(
                eq("UPDATE orders SET status = ? WHERE id = ?"),
                eq(newStatus.name()),
                eq(orderId)
        );
    }

    @Test
    void findByUserId() {
        Long userId = 1L;
        when(jdbcTemplate.query(
                eq("SELECT * FROM orders WHERE user_id = ?"),
                any(RowMapper.class),
                eq(userId)
        )).thenReturn(Collections.singletonList(testOrder));

        List<Order> result = orderRepository.findByUserId(userId);

        assertEquals(1, result.size());
        assertEquals(testOrder, result.get(0));
    }

    @Test
    void existsById_WhenExists() {
        Long orderId = 1L;
        when(jdbcTemplate.queryForObject(
                eq("SELECT COUNT(*) FROM orders WHERE id = ?"),
                eq(Integer.class),
                eq(orderId)
        )).thenReturn(1);

        boolean result = orderRepository.existsById(orderId);

        assertTrue(result);
    }

    @Test
    void existsById_WhenNotExists() {
        Long orderId = 1L;
        when(jdbcTemplate.queryForObject(
                eq("SELECT COUNT(*) FROM orders WHERE id = ?"),
                eq(Integer.class),
                eq(orderId)
        )).thenReturn(0);

        boolean result = orderRepository.existsById(orderId);

        assertFalse(result);
    }

    @Test
    void findById_WhenExists() {
        Long orderId = 1L;
        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM orders WHERE id = ?"),
                any(RowMapper.class),
                eq(orderId)
        )).thenReturn(testOrder);

        Optional<Order> result = orderRepository.findById(orderId);

        assertTrue(result.isPresent());
        assertEquals(testOrder, result.get());
    }

    @Test
    void findById_WhenNotExists() {
        Long orderId = 1L;
        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM orders WHERE id = ?"),
                any(RowMapper.class),
                eq(orderId)
        )).thenThrow(new EmptyResultDataAccessException(1));

        Optional<Order> result = orderRepository.findById(orderId);

        assertFalse(result.isPresent());
    }

    @Test
    void findAll() {
        when(jdbcTemplate.query(
                eq("SELECT * FROM orders"),
                any(RowMapper.class)
        )).thenReturn(Collections.singletonList(testOrder));

        List<Order> result = orderRepository.findAll();

        assertEquals(1, result.size());
        assertEquals(testOrder, result.get(0));
    }

    @Test
    void save() {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        keyHolder.getKeyList().add(Collections.singletonMap("id", 1L));

        when(jdbcTemplate.update(
                any(org.springframework.jdbc.core.PreparedStatementCreator.class),
                any(GeneratedKeyHolder.class)
        )).thenAnswer(invocation -> {
            GeneratedKeyHolder holder = invocation.getArgument(1);
            holder.getKeyList().add(Collections.singletonMap("id", 1L));
            return 1;
        });

        Long resultId = orderRepository.save(testOrderCreate);

        assertEquals(1L, resultId);
    }

    @Test
    void update() {
        Order updatedOrder = new Order(1L, 2L, testDateTime, OrderStatus.PROCESSING);

        orderRepository.update(updatedOrder);

        verify(jdbcTemplate).update(
                eq("UPDATE orders SET user_id = ?, order_date = ?, status = ? WHERE id = ?"),
                eq(updatedOrder.getUserId()),
                eq(Timestamp.valueOf(updatedOrder.getOrderDate())),
                eq(updatedOrder.getStatus().name()),
                eq(updatedOrder.getId())
        );
    }

    @Test
    void deleteById() {
        Long orderId = 1L;

        orderRepository.deleteById(orderId);

        verify(jdbcTemplate).update(
                eq("DELETE FROM orders WHERE id = ?"),
                eq(orderId)
        );
    }
}