package org.example.patterns.proxy;

import java.util.Optional;
import java.util.List;

/**
 * STATYCZNY PROXY z logowaniem + pomiarem czasu.
 * Każda metoda: log → delegate → log z czasem.
 */
public class LoggingProxy implements UserService {

    private final UserService delegate;

    public LoggingProxy(UserService delegate) { this.delegate = delegate; }

    @Override
    public Optional<String> findById(Long id) {
        long start = System.nanoTime();
        System.out.println("[LOG] findById(" + id + ") — start");
        try {
            Optional<String> result = delegate.findById(id);
            System.out.printf("[LOG] findById(%d) — %dms — %s%n",
                    id, (System.nanoTime() - start) / 1_000_000,
                    result.isPresent() ? "found" : "empty");
            return result;
        } catch (Exception e) {
            System.out.println("[LOG] findById ERROR: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String save(String name) {
        long start = System.nanoTime();
        System.out.println("[LOG] save(" + name + ") — start");
        String r = delegate.save(name);
        System.out.printf("[LOG] save(%s) — %dms%n", name,
                (System.nanoTime() - start) / 1_000_000);
        return r;
    }

    @Override
    public List<String> findAll() { return delegate.findAll(); }
}
