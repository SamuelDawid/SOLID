package org.example.patterns.state;

public class NewState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("Zamówienie opłacone — przejście do POTWIERDZONE");
        order.setState(new ConfirmedState());
    }

    @Override
    public void ship(Order order) {
        throw new IllegalStateException("Nie można wysłać — najpierw zapłać");
    }

    @Override
    public void deliver(Order order) {
        throw new IllegalStateException("Nie można dostarczyć — najpierw zapłać i wyślij");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Anulowano zamówienie NOWE");
        order.setState(new CancelledState());
    }

    @Override public String name() { return "NOWE"; }
}
