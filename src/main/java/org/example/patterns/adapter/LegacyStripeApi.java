package org.example.patterns.adapter;

/**
 * Symulacja zewnętrznej biblioteki — innego API,
 * nad którą NIE mamy kontroli (nie możemy zmienić sygnatury).
 */
public class LegacyStripeApi {

    /** Stripe operuje w GROSZACH (long), nie PLN. */
    public StripeResult charge(String stripeCustomerId, long amountInCents,
                               String currency, String memo) {
        System.out.printf("Stripe.charge(customer=%s, cents=%d %s, memo=%s)%n",
                stripeCustomerId, amountInCents, currency, memo);
        // Symulujemy sukces dla niezerowej kwoty
        return new StripeResult(amountInCents > 0,
                "ch_" + System.nanoTime(),
                amountInCents > 0 ? "ok" : "amount must be positive");
    }

    public record StripeResult(boolean success, String chargeId, String message) {}
}
