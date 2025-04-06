package com.example.repositories;

import com.example.dto.user.UserRequest;
import com.example.models.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User> {
    Optional<User> findById(long id);
    Optional<User> findByEmail(String email);
    boolean existsById(long id);
    boolean existsByEmail(String email);
    void deleteById(long id);
    void update(User user);
    long save(UserRequest user);
}