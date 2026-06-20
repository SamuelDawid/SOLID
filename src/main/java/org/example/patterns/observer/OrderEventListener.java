package org.example.patterns.observer;

@FunctionalInterface
public interface OrderEventListener {
    void onEvent(OrderEvent event);
}
