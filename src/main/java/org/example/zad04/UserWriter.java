package org.example.zad04;

public interface UserWriter {
    User save(User user);
    void delete(Long id);
}
