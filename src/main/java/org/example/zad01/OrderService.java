package org.example.zad01;

import java.util.Objects;

public class OrderService {

    private final OrderRepository repository;
    private final EmailService email;
    private final ReportService report;

    public OrderService(OrderRepository repository,
                        EmailService email,
                        ReportService report) {
        this.repository = Objects.requireNonNull(repository);
        this.email      = Objects.requireNonNull(email);
        this.report     = Objects.requireNonNull(report);
    }

    public OrderManagerBefore.Order createOrder(OrderManagerBefore.Cart cart) {
        if (cart.isEmpty()) {
            throw new IllegalStateException("Pusty koszyk");
        }
        OrderManagerBefore.Order order = new OrderManagerBefore.Order();
        order.setTotal(calculateTotal(cart));
        repository.save(order);
        email.sendConfirmation(order);
        return order;
    }

    private double calculateTotal(OrderManagerBefore.Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }
}
