package org.example.patterns.state;

public class ShippedState implements OrderState {

    @Override
    public void pay(Order order) {
        throw new IllegalStateException("Już opłacone");
    }

    @Override
    public void ship(Order order) {
        throw new IllegalStateException("Już wysłane");
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Zamówienie dostarczone do klienta");
        order.setState(new DeliveredState());
    }

    @Override
    public void cancel(Order order) {
        throw new IllegalStateException("Nie można anulować — już wysłane");
    }

    @Override public String name() { return "WYSŁANE"; }
}
