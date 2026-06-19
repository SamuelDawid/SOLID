package org.example.patterns.facade;

public class PaymentService {
    public boolean charge(String customerId, double amount) {
        System.out.printf("[PAYMENT] obciążam %s o %.2f PLN%n", customerId, amount);
        return amount > 0;
    }

    public void refund(String customerId, double amount) {
        System.out.printf("[PAYMENT] zwrot %.2f PLN dla %s%n", amount, customerId);
    }
}
