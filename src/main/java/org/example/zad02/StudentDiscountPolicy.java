package org.example.zad02;

public class StudentDiscountPolicy implements DiscountPolicy {
    private static final double RATE = 0.20;
    @Override public double apply(double total) { return total * (1 - RATE); }
    @Override public String description()       { return "Zniżka studencka 20%"; }
}
