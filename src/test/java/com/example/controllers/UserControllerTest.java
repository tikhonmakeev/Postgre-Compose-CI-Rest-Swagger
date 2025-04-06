package com.example.controllers;

import com.example.dto.user.UserRequest;
import com.example.dto.user.UserResponse;
import com.example.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        UserRequest userRequest = UserRequest.builder()
                .name("Tester Test")
                .email("test@test.com")
                .address("7 Gashek St")
                .phone("88005553535")
                .build();

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("Tester Test");
        userResponse.setEmail("test@test.com");
        userResponse.setAddress("7 Gashek St");
        userResponse.setPhone("88005553535");
        userResponse.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void getAllUsers_AllUsers() {
        List<UserResponse> users = Arrays.asList(userResponse);
        when(userService.getAllUsers()).thenReturn(users);
        ResponseEntity<List<UserResponse>> response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService).getAllUsers();
    }

    @Test
    void getUserById_User() {
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(Optional.of(userResponse));
        ResponseEntity<UserResponse> response = userController.getUserById(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
        verify(userService).getUserById(userId);
    }

    @Test
    void getUserById_NotFound() {
        Long userId = 1L;
        when(userService.getUserById(userId)).thenReturn(Optional.empty());
        ResponseEntity<UserResponse> response = userController.getUserById(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).getUserById(userId);
    }

    @Test
    void createUser_CreateUser() {
        when(userService.existsByEmail(userRequest.getEmail())).thenReturn(false);
        when(userService.createUser(userRequest)).thenReturn(userResponse);
        ResponseEntity<UserResponse> response = userController.createUser(userRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
        verify(userService).existsByEmail(userRequest.getEmail());
        verify(userService).createUser(userRequest);
    }

    @Test
    void createUser_Conflict() {
        when(userService.existsByEmail(userRequest.getEmail())).thenReturn(true);
        ResponseEntity<UserResponse> response = userController.createUser(userRequest);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).existsByEmail(userRequest.getEmail());
        verify(userService, never()).createUser(any());
    }

    @Test
    void updateUser_UpdateUser() {
        Long userId = 1L;
        when(userService.updateUser(eq(userId), any(UserRequest.class))).thenReturn(userResponse);
        ResponseEntity<UserResponse> response = userController.updateUser(userId, userRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponse, response.getBody());
        verify(userService).updateUser(userId, userRequest);
    }

    @Test
    void updateUser_NotFound() {
        Long userId = 1L;
        when(userService.updateUser(eq(userId), any(UserRequest.class)))
                .thenThrow(new IllegalArgumentException("User not found with id: " + userId));
        ResponseEntity<UserResponse> response = userController.updateUser(userId, userRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).updateUser(userId, userRequest);
    }

    @Test
    void updateUser_Conflict() {
        Long userId = 1L;
        when(userService.updateUser(eq(userId), any(UserRequest.class)))
                .thenThrow(new IllegalArgumentException("Email is already in use"));
        ResponseEntity<UserResponse> response = userController.updateUser(userId, userRequest);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).updateUser(userId, userRequest);
    }

    @Test
    void deleteUser_DeleteUser() {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);
        ResponseEntity<Void> response = userController.deleteUser(userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).deleteUser(userId);
    }

    @Test
    void deleteUser_NotFound() {
        Long userId = 1L;
        doThrow(new IllegalArgumentException("User not found with id: " + userId))
                .when(userService).deleteUser(userId);
        ResponseEntity<Void> response = userController.deleteUser(userId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService).deleteUser(userId);
    }
}