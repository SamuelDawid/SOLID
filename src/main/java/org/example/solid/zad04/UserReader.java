package org.example.solid.zad04;

import java.util.List;
import java.util.Optional;

public interface UserReader {
    Optional<User> findById(Long id);
    List<User> findAll();
}
