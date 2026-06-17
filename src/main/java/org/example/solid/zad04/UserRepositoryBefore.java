package org.example.solid.zad04;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryBefore {

    // Operacje CRUD (wszyscy potrzebują)
    Optional<User> findById(Long id);
    List<User> findAll();
    User save(User user);
    void delete(Long id);

    // Operacje używane TYLKO przez moduł autoryzacji
    Optional<User> findByEmail(String email);
    void updatePassword(Long id, String hash);
    void recordFailedLogin(Long id);

    // Operacje używane TYLKO przez moduł administracyjny
    List<User> findByRole(String role);
    void grantAdmin(Long id);
    void banUser(Long id);

    // Operacje używane TYLKO przez cron usuwający martwe sesje
    List<User> findWithExpiredSessions();
    void purgeInactiveUsers();
}
