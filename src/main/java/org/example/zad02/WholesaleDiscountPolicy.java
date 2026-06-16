package org.example.zad02;

public class WholesaleDiscountPolicy implements DiscountPolicy {
    private static final double RATE = 0.30;
    @Override public double apply(double total) { return total * (1 - RATE); }
    @Override public String description()       { return "Hurtownia 30%"; }
}
