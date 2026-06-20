package org.example.patterns.state;

public class ConfirmedState implements OrderState {

    @Override
    public void pay(Order order) {
        throw new IllegalStateException("Już opłacone");
    }

    @Override
    public void ship(Order order) {
        System.out.println("Zamówienie wysłane");
        order.setState(new ShippedState());
    }

    @Override
    public void deliver(Order order) {
        throw new IllegalStateException("Najpierw wyślij");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Anulowano POTWIERDZONE — uruchamiam refund");
        order.setState(new CancelledState());
    }

    @Override public String name() { return "POTWIERDZONE"; }
}
