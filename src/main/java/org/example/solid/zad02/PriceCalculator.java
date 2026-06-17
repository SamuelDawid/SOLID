package org.example.solid.zad02;

public class PriceCalculator {
    public record Order(double total) {}

    // Nie wie o konkretnych politykach — tylko o interfejsie
    public double calculate(Order order, DiscountPolicy policy) {
        return policy.apply(order.total());
    }
}
