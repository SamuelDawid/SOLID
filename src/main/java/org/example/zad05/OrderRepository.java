package org.example.zad05;

import java.util.Optional;

public interface OrderRepository {
    void save(String customer, double amount);
    Optional<String> findById(Long id);
}
