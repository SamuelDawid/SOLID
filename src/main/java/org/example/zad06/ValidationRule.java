package org.example.zad06;

@FunctionalInterface
public interface ValidationRule {
    void validate(User user);
}
