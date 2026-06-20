package org.example.patterns.observer;

public record OrderCancelled(String orderId, String reason)
        implements OrderEvent {}
