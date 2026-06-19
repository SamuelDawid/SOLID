package org.example.patterns.strategy;

/**
 * CONTEXT — trzyma referencję do strategii, deleguje obliczenia.
 * Nie wie, jaka jest konkretna strategia — używa tylko interfejsu.
 */
public class PriceCalculator {

    private DiscountStrategy strategy;

    public PriceCalculator(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(double price, int quantity) {
        return strategy.apply(price, quantity);
    }
}
