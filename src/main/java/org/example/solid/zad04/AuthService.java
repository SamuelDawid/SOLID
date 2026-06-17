package org.example.solid.zad04;

import java.util.Optional;

public class AuthService {

    private final UserAuthRepository authRepo;

    public AuthService(UserAuthRepository authRepo) {
        this.authRepo = authRepo;
    }

    public Optional<User> login(String email, String passwordHash) {
        return authRepo.findByEmail(email);
        // AuthService nie widzi banUser ani purgeInactiveUsers
    }
}
