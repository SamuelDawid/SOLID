package org.example.patterns.state;

public class Main {
    public static void main(String[] args) {

        Order order = new Order("ORD-001");
        System.out.println("Stan: " + order.stateName());

        order.pay();
        System.out.println("Stan: " + order.stateName());

        order.ship();
        System.out.println("Stan: " + order.stateName());

        order.deliver();
        System.out.println("Stan: " + order.stateName());

        try {
            order.pay();
        } catch (IllegalStateException e) {
            System.out.println("Błąd: " + e.getMessage());
        }

        System.out.println("\n=== Drugi przykład — anulowanie ===");
        Order order2 = new Order("ORD-002");
        order2.pay();
        order2.cancel();
        System.out.println("Stan: " + order2.stateName());

        try {
            order2.ship();
        } catch (IllegalStateException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }
}
