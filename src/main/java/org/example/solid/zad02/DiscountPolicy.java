package org.example.solid.zad02;

public interface DiscountPolicy {
    double apply(double total);
    String description();
}
