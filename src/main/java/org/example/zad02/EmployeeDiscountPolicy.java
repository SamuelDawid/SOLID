package org.example.zad02;

public class EmployeeDiscountPolicy implements DiscountPolicy {
    private static final double RATE = 0.50;
    @Override public double apply(double total) { return total * (1 - RATE); }
    @Override public String description()       { return "Pracownik 50%"; }
}
