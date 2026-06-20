package org.example.patterns.decorator;

public class MilkDecorator extends CoffeeDecorator {

    public MilkDecorator(Coffee delegate) { super(delegate); }

    @Override public String description() { return delegate.description() + " + mleko"; }
    @Override public double cost()        { return delegate.cost() + 1.50; }
}
