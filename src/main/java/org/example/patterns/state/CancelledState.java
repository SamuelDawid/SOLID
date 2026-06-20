package org.example.patterns.state;

public class CancelledState implements OrderState {

    @Override
    public void pay(Order order)     { throw new IllegalStateException("Zamówienie anulowane"); }
    @Override
    public void ship(Order order)    { throw new IllegalStateException("Zamówienie anulowane"); }
    @Override
    public void deliver(Order order) { throw new IllegalStateException("Zamówienie anulowane"); }
    @Override
    public void cancel(Order order)  { System.out.println("Już anulowane — bez zmian"); }
    @Override public String name()   { return "ANULOWANE"; }
}
