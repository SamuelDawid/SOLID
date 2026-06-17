package org.example;

import org.example.solid.zad06.*;

import java.util.List;

public class App {

    public static void main(String[] args) {

        UserRepository repository = new InMemoryUserRepository();
        EmailService email = new ConsoleEmailService();

        // Reguły walidacji jako lambdy
        ValidationRule notBlankName = u -> {
            if (u.name() == null || u.name().isBlank())
                throw new IllegalArgumentException("Imię jest wymagane");
        };
        ValidationRule validEmail = u -> {
            if (u.email() == null || !u.email().contains("@"))
                throw new IllegalArgumentException("Niepoprawny email: " + u.email());
        };
        ValidationRule emailLength = u -> {
            if (u.email() != null && u.email().length() > 100)
                throw new IllegalArgumentException("Email za długi (max 100): " + u.email());
        };

        UserValidator validator = new UserValidator(List.of(
                notBlankName, validEmail, emailLength));

        UserService service = new UserService(repository, email, validator);

        User u1 = service.registerUser("Anna Nowak", "anna@test.pl", null);
        System.out.println("Zarejestrowano: " + u1);

        // Dodanie nowej reguły = nowa lambda, BEZ ZMIANY UserService (OCP)
        ValidationRule noAdminEmail = u -> {
            if (u.email() != null && u.email().startsWith("admin@"))
                throw new IllegalArgumentException("Email admin@* zabroniony");
        };

        UserValidator strictValidator = new UserValidator(List.of(
                notBlankName, validEmail, emailLength, noAdminEmail));
        UserService strictService = new UserService(repository, email, strictValidator);

        try {
            strictService.registerUser("Hakier", "admin@xyz.pl", "ADMIN");
        } catch (IllegalArgumentException ex) {
            System.out.println("Zablokowano: " + ex.getMessage());
        }
    }
}
