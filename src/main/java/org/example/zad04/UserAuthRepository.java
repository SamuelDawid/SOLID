package org.example.zad04;

import java.util.Optional;

public interface UserAuthRepository {
    Optional<User> findByEmail(String email);
    void updatePassword(Long id, String hash);
    void recordFailedLogin(Long id);
}
