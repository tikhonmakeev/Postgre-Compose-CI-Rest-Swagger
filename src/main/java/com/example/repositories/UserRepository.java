package com.example.repositories;

import com.example.models.User;

import java.util.List;

public interface UserRepository {
    void createUser(String name, String email, String address, String phone);

    void updateUser(int id, String newName, String newEmail, String newAddress, String newPhone);

    void deleteUser(int id);

    User getUserById(int id);

    List<User> getAllUsers();
}