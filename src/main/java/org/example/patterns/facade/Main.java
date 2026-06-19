package org.example.patterns.facade;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        OrderFacade facade = new OrderFacade(
                new PaymentService(),
                new WarehouseService(),
                new EmailService(),
                new AuditService()
        );

        Order zamowienie = new Order("ORD-001", "CUST-42",
                List.of("Laptop", "Mysz"), 4299.50);

        System.out.println("=== Składanie zamówienia ===");
        facade.placeOrder(zamowienie);

        System.out.println("\n=== Anulowanie ===");
        facade.cancelOrder(zamowienie);

        System.out.println("\n=== Próba pustego zamówienia ===");
        Order puste = new Order("ORD-002", "CUST-42", List.of(), 0.00);
        boolean ok = facade.placeOrder(puste);
        System.out.println("Sukces? " + ok);
    }
}
