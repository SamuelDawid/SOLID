package org.example.patterns.decorator;

/** Konkretna kawa bazowa — najprostszy wariant. */
public class Espresso implements Coffee {
    @Override public String description() { return "Espresso"; }
    @Override public double cost()        { return 8.00; }
}
