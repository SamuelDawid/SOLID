package org.example.patterns.facade;

public class EmailService {
    public void sendConfirmation(String customerId, String orderId) {
        System.out.printf("[EMAIL] potwierdzenie do %s dla %s%n", customerId, orderId);
    }

    public void sendCancellation(String customerId, String orderId) {
        System.out.printf("[EMAIL] anulowanie do %s dla %s%n", customerId, orderId);
    }
}
