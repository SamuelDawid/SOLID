package org.example.patterns.adapter;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Klient zna TYLKO PaymentProcessor — nie wie, czy to Stripe czy PayPal
        PaymentProcessor stripe  = new StripeAdapter(new LegacyStripeApi(), "cus_");
        PaymentProcessor paypal  = new PayPalAdapter();

        System.out.println("=== Stripe ===");
        stripe.pay("CUST-001", 199.99, "Subskrypcja roczna");

        System.out.println("\n=== PayPal ===");
        paypal.pay("CUST-001", 199.99, "Subskrypcja roczna");

        System.out.println("\n=== Class Adapter — tablica jako List ===");
        Integer[] tab = {1, 2, 3, 4, 5};
        List<Integer> jakoLista = new ArrayToListClassAdapter<>(tab);
        System.out.println("size: " + jakoLista.size());
        System.out.println("element[2]: " + jakoLista.get(2));
        System.out.println("toString: " + jakoLista);
    }
}
