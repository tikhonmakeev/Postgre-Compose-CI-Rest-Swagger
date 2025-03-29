package com.example.config;

import com.example.repositories.OrderItemRepository;
import com.example.repositories.OrderRepository;
import com.example.repositories.ProductRepository;
import com.example.repositories.UserRepository;
import com.example.repositories.impl.ProductRepositoryImpl;
import com.example.repositories.impl.UserRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class RepositoryConfig {

    @Bean
    public UserRepository userRepository(JdbcTemplate jdbcTemplate) {
        return new UserRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public ProductRepository productRepository(JdbcTemplate jdbcTemplate) {
        return new ProductRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public OrderRepository orderRepository(JdbcTemplate jdbcTemplate) {
        return new ProductRepositoryImpl(jdbcTemplate);
    }

    @Bean
    public OrderItemRepository orderItemRepositoryx(JdbcTemplate jdbcTemplate) {
        return new ProductRepositoryImpl(jdbcTemplate);
    }
}