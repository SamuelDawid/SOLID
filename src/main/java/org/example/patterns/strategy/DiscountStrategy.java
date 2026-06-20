package org.example.patterns.strategy;

/**
 * Interfejs funkcyjny — strategia jako lambda.
 * apply(price, quantity) → cena finalna.
 */
@FunctionalInterface
public interface DiscountStrategy {
    double apply(double price, int quantity);
}
