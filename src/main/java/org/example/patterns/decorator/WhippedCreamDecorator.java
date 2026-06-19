package org.example.patterns.decorator;

public class WhippedCreamDecorator extends CoffeeDecorator {

    public WhippedCreamDecorator(Coffee delegate) { super(delegate); }

    @Override public String description() { return delegate.description() + " + bita śmietana"; }
    @Override public double cost()        { return delegate.cost() + 3.00; }
}
