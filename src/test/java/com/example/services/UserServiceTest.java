// File: src/test/java/com/example/services/UserServiceTest.java
package com.example.services;

import com.example.dto.user.UserRequest;
import com.example.dto.user.UserResponse;
import com.example.models.User;
import com.example.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .address("123 Main St")
                .phone("123456789")
                .build();

        userRequest = UserRequest.builder()
                .name("John Doe")
                .email("john.doe@example.com")
                .address("123 Main St")
                .phone("123456789")
                .build();
    }

    @Test
    void getAllUsers_shouldReturnMappedUserResponses() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        List<UserResponse> responses = userService.getAllUsers();
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(user.getId(), responses.get(0).getId());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_found_shouldReturnUserResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<UserResponse> response = userService.getUserById(1L);
        assertTrue(response.isPresent());
        assertEquals(user.getId(), response.get().getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_notFound_shouldReturnEmptyOptional() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<UserResponse> response = userService.getUserById(1L);
        assertFalse(response.isPresent());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserByEmail_found_shouldReturnUserResponse() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        Optional<UserResponse> response = userService.getUserByEmail("john.doe@example.com");
        assertTrue(response.isPresent());
        assertEquals(user.getEmail(), response.get().getEmail());
        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void getUserByEmail_notFound_shouldReturnEmptyOptional() {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        Optional<UserResponse> response = userService.getUserByEmail("john.doe@example.com");
        assertFalse(response.isPresent());
        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    void existsByEmail_shouldReturnTrue() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);
        boolean result = userService.existsByEmail("john.doe@example.com");
        assertTrue(result);
        verify(userRepository).existsByEmail("john.doe@example.com");
    }

    @Test
    void createUser_whenEmailExists_shouldThrowException() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.createUser(userRequest));
        assertEquals("User with this email already exists", exception.getMessage());

        verify(userRepository).existsByEmail("john.doe@example.com");
        verify(userRepository, never()).save(any(UserRequest.class));
    }

    @Test
    void updateUser_shouldUpdateAndReturnUserResponse() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userService.updateUser(1L, userRequest);
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("john.doe@example.com", response.getEmail());
        assertNotNull(response.getCreatedAt());

        verify(userRepository).existsById(1L);
        verify(userRepository).findById(1L);
        verify(userRepository).update(any(User.class));
    }

    @Test
    void updateUser_whenUserNotFound_shouldThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(1L, userRequest));
        assertEquals("User not found with id: 1", exception.getMessage());

        verify(userRepository).existsById(1L);
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    void updateUser_whenEmailAlreadyExists_shouldThrowException() {
        User anotherUser = User.builder()
                .id(2L)
                .name("Jane Doe")
                .email("jane.doe@example.com")
                .build();
        userRequest.setEmail("jane.doe@example.com");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail("jane.doe@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(1L, userRequest));
        assertEquals("Email is already in use", exception.getMessage());

        verify(userRepository).existsById(1L);
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("jane.doe@example.com");
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    void deleteUser_shouldCallDelete() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);
        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_whenUserNotFound_shouldThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(1L));
        assertEquals("User not found with id: 1", exception.getMessage());

        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}