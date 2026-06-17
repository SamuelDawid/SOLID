package org.example.zad05;

import java.util.Optional;

public class MySqlOrderRepository implements OrderRepository {
    @Override
    public void save(String customer, double amount) {
        System.out.println("MySQL INSERT: " + customer + "/" + amount);
    }
    @Override
    public Optional<String> findById(Long id) {
        return Optional.empty();
    }
}
