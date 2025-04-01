package com.example.dto.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private LocalDateTime createdAt;
}