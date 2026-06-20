package org.example.patterns.adapter;

/** Nasz wewnętrzny interfejs — używany w całej aplikacji. */
public interface PaymentProcessor {
    boolean pay(String customerId, double amountPln, String description);
}
