package org.example.solid.zad06;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> store = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public Optional<User> findByEmail(String email) {
        return store.values().stream()
                .filter(u -> u.email().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public User save(User user) {
        long id = sequence.incrementAndGet();
        User saved = new User(id, user.name(), user.email(),
                user.role(), user.createdAt());
        store.put(id, saved);
        return saved;
    }
}
