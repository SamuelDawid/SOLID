package org.example.patterns.proxy;

import java.util.Optional;
import java.util.List;

public interface UserService {
    Optional<String> findById(Long id);
    String save(String name);
    List<String> findAll();
}
