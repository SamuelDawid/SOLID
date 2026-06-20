package org.example.patterns.state;

/**
 * CONTEXT — trzyma stan, deleguje wywołania do stanu.
 * Klient woła order.pay(), order.ship() itd. — order nie ma logiki,
 * tylko deleguje do currentState.
 */
public class Order {

    private final String id;
    private OrderState state;

    public Order(String id) {
        this.id = id;
        this.state = new NewState();
    }

    public void pay()     { state.pay(this); }
    public void ship()    { state.ship(this); }
    public void deliver() { state.deliver(this); }
    public void cancel()  { state.cancel(this); }

    void setState(OrderState newState) {
        System.out.printf("[STATE] %s: %s → %s%n", id, state.name(), newState.name());
        this.state = newState;
    }

    public String getId()       { return id; }
    public String stateName()   { return state.name(); }
}
