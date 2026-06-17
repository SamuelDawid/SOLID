package org.example.zad06;

import java.time.LocalDateTime;

public record User(long id,
                   String name,
                   String email,
                   String role,
                   LocalDateTime createdAt) {
}
