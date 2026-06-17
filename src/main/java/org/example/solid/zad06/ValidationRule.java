package org.example.solid.zad06;

@FunctionalInterface
public interface ValidationRule {
    void validate(User user);
}
