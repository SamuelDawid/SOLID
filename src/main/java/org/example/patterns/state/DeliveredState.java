package org.example.patterns.state;

public class DeliveredState implements OrderState {

    @Override
    public void pay(Order order)     { throw new IllegalStateException("Już dostarczone"); }
    @Override
    public void ship(Order order)    { throw new IllegalStateException("Już dostarczone"); }
    @Override
    public void deliver(Order order) { throw new IllegalStateException("Już dostarczone"); }
    @Override
    public void cancel(Order order)  { throw new IllegalStateException("Nie można anulować po dostawie"); }
    @Override public String name()   { return "DOSTARCZONE"; }
}
