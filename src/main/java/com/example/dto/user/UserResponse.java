package com.example.dto.user;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private LocalDateTime createdAt;
}