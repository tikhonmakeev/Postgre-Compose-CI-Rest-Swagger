package com.example.repositories;

import com.example.models.User;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}