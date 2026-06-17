package org.example.solid.zad04;

import java.util.List;

public interface UserAdminRepository {
    List<User> findByRole(String role);
    void grantAdmin(Long id);
    void banUser(Long id);
}
