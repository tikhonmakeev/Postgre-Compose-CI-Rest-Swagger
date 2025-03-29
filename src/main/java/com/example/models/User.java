package com.example.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {

    @NotNull
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    private String address;
    private String phone;
}
