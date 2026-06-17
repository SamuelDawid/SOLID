package org.example.zad04;

import java.util.List;

public interface UserCleanupRepository {
    List<User> findWithExpiredSessions();
    void purgeInactiveUsers();
}
