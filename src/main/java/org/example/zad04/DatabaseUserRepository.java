package org.example.zad04;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

// Jedna implementacja konkretna — implementuje TYLE interfejsów, ile potrzeba
public class DatabaseUserRepository
        implements UserReader, UserWriter, UserAuthRepository,
        UserAdminRepository, UserCleanupRepository {

    @Override public Optional<User> findById(Long id) { return Optional.empty(); }
    @Override public List<User> findAll() { return Collections.emptyList(); }
    @Override public User save(User user) { return user; }
    @Override public void delete(Long id) { }
    @Override public Optional<User> findByEmail(String email) { return Optional.empty(); }
    @Override public void updatePassword(Long id, String hash) { }
    @Override public void recordFailedLogin(Long id) { }
    @Override public List<User> findByRole(String role) { return Collections.emptyList(); }
    @Override public void grantAdmin(Long id) { }
    @Override public void banUser(Long id) { }
    @Override public List<User> findWithExpiredSessions() { return Collections.emptyList(); }
    @Override public void purgeInactiveUsers() { }
}
