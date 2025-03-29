package com.example.repositories.impl;

import com.example.models.User;
import com.example.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createUser(String name, String email, String address, String phone) {

    }

    @Override
    public void updateUser(int id, String newName, String newEmail, String newAddress, String newPhone) {

    }

    @Override
    public void deleteUser(int id) {

    }

    @Override
    public User getUserById(int id) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public boolean existsById(long id) {
        return false;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public long save(User entity) {
        return 0;
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void deleteById(long id) {

    }
}
