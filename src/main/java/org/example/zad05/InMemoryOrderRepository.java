package org.example.zad05;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<Long, String> store = new HashMap<>();
    private long sequence = 0;

    @Override
    public void save(String customer, double amount) {
        store.put(++sequence, customer + "/" + amount);
    }

    @Override
    public Optional<String> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
