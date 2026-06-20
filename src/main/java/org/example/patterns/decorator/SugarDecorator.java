package org.example.patterns.decorator;

public class SugarDecorator extends CoffeeDecorator {

    public SugarDecorator(Coffee delegate) { super(delegate); }

    @Override public String description() { return delegate.description() + " + cukier"; }
    @Override public double cost()        { return delegate.cost() + 0.50; }
}
