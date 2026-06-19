package org.example.patterns.facade;

/**
 * FASADA — jedna metoda placeOrder(...) ukrywa koordynację 4 podsystemów.
 * Klient nie wie, że za fasadą stoi PaymentService, WarehouseService,
 * EmailService, AuditService.
 */
public class OrderFacade {

    private final PaymentService payment;
    private final WarehouseService warehouse;
    private final EmailService email;
    private final AuditService audit;

    public OrderFacade(PaymentService payment, WarehouseService warehouse,
                       EmailService email, AuditService audit) {
        this.payment = payment;
        this.warehouse = warehouse;
        this.email = email;
        this.audit = audit;
    }

    public boolean placeOrder(Order order) {
        audit.log("Próba złożenia zamówienia " + order.orderId());

        boolean paid = payment.charge(order.customerId(), order.total());
        if (!paid) {
            audit.log("Płatność nieudana dla " + order.orderId());
            return false;
        }

        boolean reserved = warehouse.reserve(order.items());
        if (!reserved) {
            payment.refund(order.customerId(), order.total());
            audit.log("Magazyn pusty, zrefundowano " + order.orderId());
            return false;
        }

        email.sendConfirmation(order.customerId(), order.orderId());
        audit.log("Zamówienie " + order.orderId() + " złożone pomyślnie");
        return true;
    }

    public void cancelOrder(Order order) {
        warehouse.release(order.items());
        payment.refund(order.customerId(), order.total());
        email.sendCancellation(order.customerId(), order.orderId());
        audit.log("Zamówienie " + order.orderId() + " anulowane");
    }
}
