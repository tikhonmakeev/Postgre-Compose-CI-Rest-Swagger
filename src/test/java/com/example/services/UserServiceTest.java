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
import static org.mockito.ArgumentMatchers.anyLong;
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
        user = new User(1L, "Tester Test", "test@test.com", "7 Gashek St", "88005553535");
        userRequest = new UserRequest();
        userRequest.setName("Tester Test");
        userRequest.setEmail("test@test.com");
        userRequest.setAddress("7 Gashek Stt");
        userRequest.setPhone("88005553535");
    }

    @Test
    void getAllUsers_AllUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(user.getId(), result.get(0).getId());
        assertEquals(user.getName(), result.get(0).getName());
        assertEquals(user.getEmail(), result.get(0).getEmail());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_User() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserResponse> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
        assertEquals(user.getName(), result.get().getName());
        assertEquals(user.getEmail(), result.get().getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_Empty() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserResponse> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserByEmail_User() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        Optional<UserResponse> result = userService.getUserByEmail("test@test.com");

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
        assertEquals(user.getEmail(), result.get().getEmail());
        verify(userRepository).findByEmail("test@test.com");
    }

    @Test
    void getUserByEmail_Empty() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        Optional<UserResponse> result = userService.getUserByEmail("test@test.com");

        assertFalse(result.isPresent());
        verify(userRepository).findByEmail("test@test.com");
    }

    @Test
    void existsByEmail_True() {
        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

        boolean result = userService.existsByEmail("test@test.com");

        assertTrue(result);
        verify(userRepository).existsByEmail("test@test.com");
    }

    @Test
    void existsByEmail_False() {
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);

        boolean result = userService.existsByEmail("test@test.com");

        assertFalse(result);
        verify(userRepository).existsByEmail("test@test.com");
    }

    @Test
    void createUser_UserResponse() {
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(1L);

        UserResponse result = userService.createUser(userRequest);

        assertNotNull(result);
        assertEquals(userRequest.getName(), result.getName());
        assertEquals(userRequest.getEmail(), result.getEmail());
        verify(userRepository).existsByEmail("test@test.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_ThrowException() {
        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userRequest));
        verify(userRepository).existsByEmail("test@test.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUser_UserResponse() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse result = userService.updateUser(1L, userRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(userRequest.getName(), result.getName());
        assertEquals(userRequest.getEmail(), result.getEmail());
        verify(userRepository).existsById(1L);
        verify(userRepository).update(any(User.class));
    }

    @Test
    void updateUser_ThrowException() {
        User existingUser = new User(1L, "Tester Test", "old@test.com", "7 Gashek St", "88005553535");
        userRequest.setEmail("test@test.com");

        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(1L, userRequest));
        verify(userRepository).existsById(1L);
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    void deleteUser_DeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_ThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> userService.deleteUser(1L));
        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}