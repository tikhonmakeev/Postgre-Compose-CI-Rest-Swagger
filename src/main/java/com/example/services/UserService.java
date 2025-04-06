package com.example.services;

import com.example.dto.user.UserRequest;
import com.example.dto.user.UserResponse;
import com.example.models.User;
import com.example.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapUserToResponse);
    }

    @Transactional(readOnly = true)
    public Optional<UserResponse> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapUserToResponse);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        long id = userRepository.save(userRequest);

        return mapUserRequestToResponse(id, userRequest);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }

        User existingUser = userRepository.findById(id).orElseThrow();
        if (!existingUser.getEmail().equals(userRequest.getEmail()) &&
                userRepository.existsByEmail(userRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use");
        }

        User user = User.builder()
                .address(userRequest.getAddress())
                .phone(userRequest.getPhone())
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .id(id)
                .build();

        userRepository.update(user);

        return mapUserRequestToResponse(id, userRequest);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserResponse mapUserRequestToResponse(long userId, UserRequest userRequest) {
        return UserResponse.builder()
                .id(userId)
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .address(userRequest.getAddress())
                .phone(userRequest.getPhone())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private UserResponse mapUserToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .phone(user.getPhone())
                .createdAt(LocalDateTime.now())
                .build();
    }
}