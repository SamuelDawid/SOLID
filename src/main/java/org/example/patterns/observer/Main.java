package org.example.patterns.observer;

import org.example.patterns.observer.pcs.StockItem;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== Ręczny EventBus ===\n");
        OrderEventBus bus = new OrderEventBus();

        bus.subscribe(event -> {
            if (event instanceof OrderPlaced op) {
                System.out.printf("[EMAIL] Potwierdzenie do %s na kwotę %.2f%n",
                        op.customerId(), op.total());
            }
        });

        bus.subscribe(event -> {
            if (event instanceof OrderPlaced op) {
                System.out.println("[WAREHOUSE] Rezerwuję towary dla " + op.orderId());
            } else if (event instanceof OrderCancelled oc) {
                System.out.printf("[WAREHOUSE] Zwalniam %s (powód: %s)%n",
                        oc.orderId(), oc.reason());
            }
        });

        bus.subscribe(event -> System.out.println("[AUDIT] " + event));

        bus.subscribe(event -> {
            if (event.orderId().startsWith("BAD")) {
                throw new RuntimeException("Symulowany błąd listenera!");
            }
        });

        bus.publish(new OrderPlaced("ORD-001", "CUST-42", 199.99));
        System.out.println();
        bus.publish(new OrderCancelled("ORD-002", "brak płatności"));
        System.out.println();
        bus.publish(new OrderPlaced("BAD-001", "CUST-99", 50.00));
        System.out.println();
        bus.publish(new OrderShipped("ORD-001", "INPOST-99887766"));

        System.out.println("\n=== PropertyChangeSupport (JavaBeans) ===\n");
        StockItem laptop = new StockItem(10, 4299.00);

        laptop.addPropertyChangeListener(evt ->
                System.out.printf("[ZMIANA] %s: %s → %s%n",
                        evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()));

        laptop.addPropertyChangeListener("price", evt ->
                System.out.println("[ALERT CENA] zmiana ceny na " + evt.getNewValue()));

        laptop.setQuantity(8);
        laptop.setPrice(3999.00);
        laptop.setQuantity(8);
    }
}
