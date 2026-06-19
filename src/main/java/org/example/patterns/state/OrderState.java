package org.example.patterns.state;

/**
 * Wspólny interfejs dla wszystkich stanów zamówienia.
 * Każda metoda dostaje context (Order), by móc zmienić stan.
 */
public interface OrderState {
    void pay(Order order);
    void ship(Order order);
    void deliver(Order order);
    void cancel(Order order);
    String name();
}
