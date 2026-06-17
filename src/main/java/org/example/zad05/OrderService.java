package org.example.zad05;

import java.util.Objects;

public class OrderService {

    private final OrderRepository repository;
    private final EmailService email;

    // Konstruktor wstrzykuje zależności (Dependency Injection — DI)
    public OrderService(OrderRepository repository, EmailService email) {
        this.repository = Objects.requireNonNull(repository);
        this.email      = Objects.requireNonNull(email);
    }

    public void placeOrder(String customer, double amount) {
        repository.save(customer, amount);
        email.send(customer, "Zamówienie przyjęte na " + amount + " PLN");
    }
}
