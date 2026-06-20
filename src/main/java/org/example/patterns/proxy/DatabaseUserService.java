package org.example.patterns.proxy;

import java.util.Optional;
import java.util.List;

/** Realna implementacja — TYLKO logika biznesowa (SRP). */
public class DatabaseUserService implements UserService {

    @Override
    public Optional<String> findById(Long id) {
        System.out.println("[DB] SELECT * FROM users WHERE id = " + id);
        try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        return id > 0 ? Optional.of("User#" + id) : Optional.empty();
    }

    @Override
    public String save(String name) {
        System.out.println("[DB] INSERT INTO users(name) VALUES('" + name + "')");
        return name + "-saved";
    }

    @Override
    public List<String> findAll() {
        System.out.println("[DB] SELECT * FROM users");
        return List.of("User#1", "User#2", "User#3");
    }
}
