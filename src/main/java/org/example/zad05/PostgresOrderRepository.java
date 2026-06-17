package org.example.zad05;

import java.util.Optional;

public class PostgresOrderRepository implements OrderRepository {
    @Override
    public void save(String customer, double amount) {
        System.out.println("POSTGRES INSERT: " + customer + "/" + amount);
    }
    @Override
    public Optional<String> findById(Long id) {
        return Optional.empty();
    }
}
