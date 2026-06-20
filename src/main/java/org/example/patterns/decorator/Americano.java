package org.example.patterns.decorator;

public class Americano implements Coffee {
    @Override public String description() { return "Americano"; }
    @Override public double cost()        { return 10.00; }
}
