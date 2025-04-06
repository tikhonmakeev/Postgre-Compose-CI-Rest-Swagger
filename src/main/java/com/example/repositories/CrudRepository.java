package com.example.repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    boolean existsById(long id);
    Optional<T> findById(long id);
    List<T> findAll();
    void deleteById(long id);
}