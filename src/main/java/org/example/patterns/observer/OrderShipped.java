package org.example.patterns.observer;

public record OrderShipped(String orderId, String trackingNumber)
        implements OrderEvent {}
