package org.example.patterns.observer;

/** Sealed interface — zamknięty zestaw eventów. */
public sealed interface OrderEvent permits OrderPlaced, OrderCancelled, OrderShipped {
    String orderId();
}
