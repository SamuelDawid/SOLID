package org.example.solid.zad02;

public class StandardDiscountPolicy implements DiscountPolicy{
    @Override public double apply(double total) { return total; }
    @Override public String description()       { return "Bez rabatu"; }
}

