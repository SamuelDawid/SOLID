package org.example.zad06;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserService {

    private final UserRepository repository;
    private final EmailService email;
    private final UserValidator validator;

    public UserService(UserRepository repository,
                       EmailService email,
                       UserValidator validator) {
        this.repository = Objects.requireNonNull(repository);
        this.email      = Objects.requireNonNull(email);
        this.validator  = Objects.requireNonNull(validator);
    }

    public User registerUser(String name, String emailAddress, String role) {

        // 1. Tworzymy obiekt domenowy (id nadany przez repo)
        User candidate = new User(
                0L,
                name,
                emailAddress != null ? emailAddress.toLowerCase() : null,
                role != null ? role : "USER",
                LocalDateTime.now()
        );

        // 2. Walidacja przez UserValidator (Strategy)
        validator.validate(candidate);

        // 3. Sprawdzenie unikalności
        if (repository.findByEmail(candidate.email()).isPresent()) {
            throw new IllegalStateException("Email już zajęty: " + candidate.email());
        }

        // 4. Zapis
        User saved = repository.save(candidate);

        // 5. Powiadomienie
        email.sendWelcome(saved);

        return saved;
    }
}
