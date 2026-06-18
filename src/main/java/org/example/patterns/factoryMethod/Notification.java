package org.example.patterns.factoryMethod;

/** Wspólny interfejs — klient zna TYLKO ten typ. */
public interface Notification {
    void send(String to, String message);
    String channel();
}
