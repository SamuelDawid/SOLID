package org.example.solid.zad04;

public interface UserWriter {
    User save(User user);
    void delete(Long id);
}
