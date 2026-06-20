package org.example.patterns.decorator;

public class SyrupDecorator extends CoffeeDecorator {

    private final String flavor;
    private static final double PRICE = 2.00;

    public SyrupDecorator(Coffee delegate, String flavor) {
        super(delegate);
        this.flavor = flavor;
    }

    @Override public String description() {
        return delegate.description() + " + syrop " + flavor;
    }
    @Override public double cost() {
        return delegate.cost() + PRICE;
    }
}
