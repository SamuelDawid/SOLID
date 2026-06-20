package org.example.solid.zad06;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManagerBefore {

    private final List<Map<String, Object>> users = new ArrayList<>();

    public Map<String, Object> registerUser(String name, String email, String role) {

        // 1. WALIDACJA (powinna być w UserValidator)
        if (name == null || name.isBlank()) {
            throw new RuntimeException("Brak imienia");
        }
        if (email == null || !email.contains("@")) {
            throw new RuntimeException("Zły email");
        }
        if (email.length() > 100) {
            throw new RuntimeException("Email za długi");
        }

        // 2. DOSTĘP DO DANYCH (powinna być w UserRepository)
        for (Map<String, Object> u : users) {
            if (email.equalsIgnoreCase((String) u.get("email"))) {
                throw new RuntimeException("Email już istnieje: " + email);
            }
        }

        // 3. LOGIKA TWORZENIA (właściwa odpowiedzialność serwisu)
        Map<String, Object> user = new HashMap<>();
        user.put("id", System.nanoTime());
        user.put("name", name);
        user.put("email", email.toLowerCase());
        user.put("role", role != null ? role : "USER");
        user.put("createdAt", LocalDateTime.now());
        users.add(user);

        // 4. POWIADOMIENIE EMAIL (powinna być w EmailService)
        System.out.println("Wysyłam email do: " + email);
        System.out.println("Treść: Witamy w serwisie, " + name + "!");

        return user;
    }
}