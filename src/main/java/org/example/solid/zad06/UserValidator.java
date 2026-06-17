package org.example.solid.zad06;

import java.util.List;

public class UserValidator {

    private final List<ValidationRule> rules;

    public UserValidator(List<ValidationRule> rules) {
        this.rules = List.copyOf(rules);
    }

    public void validate(User user) {
        for (ValidationRule rule : rules) {
            rule.validate(user);
        }
    }
}
