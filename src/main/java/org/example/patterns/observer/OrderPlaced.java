package org.example.patterns.observer;

public record OrderPlaced(String orderId, String customerId, double total)
        implements OrderEvent {}
