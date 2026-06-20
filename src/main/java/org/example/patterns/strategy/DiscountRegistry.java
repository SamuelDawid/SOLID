package org.example.patterns.strategy;

import java.util.Map;
import java.util.HashMap;

/**
 * Dispatch table — klucz to typ klienta, wartość to strategia.
 * Dodanie nowej strategii = jedna linia w register().
 */
public class DiscountRegistry {

    private final Map<String, DiscountStrategy> strategies = new HashMap<>();
    private final DiscountStrategy fallback;

    public DiscountRegistry(DiscountStrategy fallback) {
        this.fallback = fallback;
    }

    public DiscountRegistry register(String key, DiscountStrategy strategy) {
        strategies.put(key, strategy);
        return this;
    }

    public DiscountStrategy get(String key) {
        return strategies.getOrDefault(key, fallback);
    }
}
