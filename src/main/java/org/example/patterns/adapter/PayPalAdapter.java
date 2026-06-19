package org.example.patterns.adapter;
/**
 * Drugi adapter — pokazuje, że łatwo dodać kolejny dostawca.
 * Klient nie zauważy różnicy — wciąż używa PaymentProcessor.
 */
public class PayPalAdapter implements PaymentProcessor {

    @Override
    public boolean pay(String customerId, double amountPln, String description) {
        System.out.printf("PayPal.send(to=%s, amount=%.2f PLN, ref=%s)%n",
                customerId + "@paypal.com", amountPln, description);
        return amountPln > 0;
    }
}
