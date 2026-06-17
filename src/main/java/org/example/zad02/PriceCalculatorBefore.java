package org.example.zad02;

import org.example.zad01.OrderManagerBefore;

public class PriceCalculatorBefore {

    public record Order(double total) {
    }

    public double calculate(OrderManagerBefore.Order order, String customerType) {
        return switch (customerType) {
            case "STANDARD"  -> order.getTotal();
            case "VIP"       -> order.getTotal() * 0.85;
            case "WHOLESALE" -> order.getTotal() * 0.70;
            case "EMPLOYEE"  -> order.getTotal() * 0.50;
            default -> throw new IllegalArgumentException("Nieznany typ: " + customerType);
        };
    }
}
