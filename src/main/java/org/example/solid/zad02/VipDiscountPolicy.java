package org.example.solid.zad02;

public class VipDiscountPolicy implements DiscountPolicy{
    private static final double RATE = 0.15;

    @Override
    public double apply(double total) {
        return total * (1 - RATE);
    }

    @Override
    public String description() {
        return "VIP 15%";
    }
}
