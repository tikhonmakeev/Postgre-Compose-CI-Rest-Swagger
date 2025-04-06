package com.example.repositories;

import com.example.models.User;
import com.example.repositories.impl.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Tester Test", "test@test.com", "7 Gashek St", "88005553535");
    }

    @Test
    void existsById_True() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(1L))).thenReturn(1);
        boolean result = userRepository.existsById(1L);
        assertTrue(result);
        verify(jdbcTemplate).queryForObject(eq("SELECT COUNT(*) FROM users WHERE id = ?"), eq(Integer.class), eq(1L));
    }

    @Test
    void existsById_False() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq(1L))).thenReturn(0);
        boolean result = userRepository.existsById(1L);
        assertFalse(result);
        verify(jdbcTemplate).queryForObject(eq("SELECT COUNT(*) FROM users WHERE id = ?"), eq(Integer.class), eq(1L));
    }

    @Test
    void existsByEmail_True() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq("test@test.com"))).thenReturn(1);
        boolean result = userRepository.existsByEmail("test@test.com");
        assertTrue(result);
        verify(jdbcTemplate).queryForObject(eq("SELECT COUNT(*) FROM users WHERE email = ?"), eq(Integer.class), eq("test@test.com"));
    }

    @Test
    void existsByEmail_False() {
        when(jdbcTemplate.queryForObject(anyString(), eq(Integer.class), eq("test@test.com"))).thenReturn(0);
        boolean result = userRepository.existsByEmail("test@test.com");
        assertFalse(result);
        verify(jdbcTemplate).queryForObject(eq("SELECT COUNT(*) FROM users WHERE email = ?"), eq(Integer.class), eq("test@test.com"));
    }

    @Test
    void findById_Found() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1L))).thenReturn(user);
        Optional<User> result = userRepository.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(jdbcTemplate).queryForObject(eq("SELECT * FROM users WHERE id = ?"), any(RowMapper.class), eq(1L));
    }

    @Test
    void findById_NotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq(1L)))
                .thenThrow(new EmptyResultDataAccessException(1));
        Optional<User> result = userRepository.findById(1L);
        assertFalse(result.isPresent());
        verify(jdbcTemplate).queryForObject(eq("SELECT * FROM users WHERE id = ?"), any(RowMapper.class), eq(1L));
    }

    @Test
    void findByEmail_Found() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq("test@test.com"))).thenReturn(user);
        Optional<User> result = userRepository.findByEmail("test@test.com");
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(jdbcTemplate).queryForObject(eq("SELECT * FROM users WHERE email = ?"), any(RowMapper.class), eq("test@test.com"));
    }

    @Test
    void findByEmail_NotFound() {
        when(jdbcTemplate.queryForObject(anyString(), any(RowMapper.class), eq("test@test.com")))
                .thenThrow(new EmptyResultDataAccessException(1));
        Optional<User> result = userRepository.findByEmail("test@test.com");
        assertFalse(result.isPresent());
        verify(jdbcTemplate).queryForObject(eq("SELECT * FROM users WHERE email = ?"), any(RowMapper.class), eq("test@test.com"));
    }

    @Test
    void findAll() {
        List<User> users = Arrays.asList(user);
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(users);
        List<User> result = userRepository.findAll();
        assertEquals(users, result);
        verify(jdbcTemplate).query(eq("SELECT * FROM users"), any(RowMapper.class));
    }

    @Test
    void update() {
        userRepository.update(user);
        verify(jdbcTemplate).update(
                eq("UPDATE users SET name = ?, email = ?, address = ?, phone = ? WHERE id = ?"),
                eq(user.getName()),
                eq(user.getEmail()),
                eq(user.getAddress()),
                eq(user.getPhone()),
                eq(user.getId())
        );
    }

    @Test
    void deleteById() {
        userRepository.deleteById(1L);
        verify(jdbcTemplate).update(eq("DELETE FROM users WHERE id = ?"), eq(1L));
    }
}