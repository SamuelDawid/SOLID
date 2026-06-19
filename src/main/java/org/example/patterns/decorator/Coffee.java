package org.example.patterns.decorator;

/** Wspólny interfejs — wszyscy (kawa bazowa + dekoratory) implementują go. */
public interface Coffee {
    String description();
    double cost();
}
