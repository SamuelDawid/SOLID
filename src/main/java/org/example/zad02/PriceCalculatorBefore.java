package org.example.zad02;

import org.example.zad01.OrderManagerBefore;

public class PriceCalculatorBefore {

    public record Order(double total) {
    }

    public double calculate(OrderManagerBefore.Order order, String customerType) {
        return switch (customerType) {
            case "STANDARD"  -> order.total();
            case "VIP"       -> order.total() * 0.85;
            case "WHOLESALE" -> order.total() * 0.70;
            case "EMPLOYEE"  -> order.total() * 0.50;
            default -> throw new IllegalArgumentException("Nieznany typ: " + customerType);
        };
    }
}
