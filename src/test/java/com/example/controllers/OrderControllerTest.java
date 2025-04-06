package com.example.controllers;

import com.example.dto.order.OrderRequest;
import com.example.dto.order.OrderResponse;
import com.example.dto.orderItem.OrderItemRequest;
import com.example.dto.orderItem.OrderItemResponse;
import com.example.models.Order;
import com.example.models.OrderStatus;
import com.example.services.NotFoundException;
import com.example.services.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OrderControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private Order testOrder;
    private OrderRequest testOrderRequest;
    private OrderResponse testOrderResponse;
    private OrderItemResponse testOrderItemResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper.registerModule(new JavaTimeModule());

        LocalDateTime now = LocalDateTime.now();

        testOrder = Order.builder()
                .id(1L)
                .userId(1L)
                .orderDate(now)
                .status(OrderStatus.NEW)
                .build();

        testOrderItemResponse = OrderItemResponse.builder()
                .id(1L)
                .productId(1L)
                .quantity(2)
                .unitPrice(100.0f)
                .build();

        testOrderRequest = new OrderRequest();
        testOrderRequest.setUserId(1L);
        testOrderRequest.setOrderDate(now);
        testOrderRequest.setItems(Collections.singletonList(
                OrderItemRequest.builder()
                        .productId(1L)
                        .quantity(2)
                        .build()));

        testOrderResponse = OrderResponse.builder()
                .id(1L)
                .userId(1L)
                .orderDate(now)
                .status(OrderStatus.NEW)
                .items(Collections.singletonList(testOrderItemResponse))
                .totalAmount(200.0f)
                .build();
    }

    @Test
    void createOrder_ShouldReturnCreatedOrder() throws Exception {
        when(orderService.createOrderWithItems(any(OrderRequest.class)))
                .thenReturn(testOrderResponse);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("NEW"))
                .andExpect(jsonPath("$.totalAmount").value(200.0))
                .andExpect(jsonPath("$.items[0].productId").value(1L));
    }

    @Test
    void createOrder_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        OrderRequest invalidRequest = new OrderRequest();
        invalidRequest.setUserId(null);
        invalidRequest.setOrderDate(null);
        invalidRequest.setItems(Collections.emptyList());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrderById_ShouldReturnOrder() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(testOrder);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    void getOrderById_WhenNotFound_ShouldReturnNotFound() throws Exception {
        when(orderService.getOrderById(1L)).thenThrow(new NotFoundException("Order not found"));

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserOrders_ShouldReturnOrdersList() throws Exception {
        when(orderService.getUserOrders(1L)).thenReturn(Collections.singletonList(testOrder));

        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].userId").value(1L));
    }

    @Test
    void getUserOrders_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        when(orderService.getUserOrders(1L)).thenThrow(new NotFoundException("User not found"));

        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateOrderStatus_ShouldReturnUpdatedOrder() throws Exception {
        Order updatedOrder = Order.builder()
                .id(1L)
                .status(OrderStatus.COMPLETED)
                .build();

        when(orderService.updateOrderStatus(1L, OrderStatus.COMPLETED))
                .thenReturn(updatedOrder);

        mockMvc.perform(patch("/api/orders/1/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void updateOrderStatus_WhenOrderNotFound_ShouldReturnNotFound() throws Exception {
        when(orderService.updateOrderStatus(1L, OrderStatus.COMPLETED))
                .thenThrow(new NotFoundException("Order not found"));

        mockMvc.perform(patch("/api/orders/1/status")
                        .param("status", "COMPLETED"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateOrderStatus_WithInvalidStatus_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(patch("/api/orders/1/status")
                        .param("status", "INVALID_STATUS"))
                .andExpect(status().isBadRequest());
    }
}