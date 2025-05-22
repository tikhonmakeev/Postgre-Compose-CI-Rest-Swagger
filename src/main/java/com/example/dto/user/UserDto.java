package com.example.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    @NotNull
    private long id;
    private String name;
    private String email;
    private String address;
    private String phone;
}
