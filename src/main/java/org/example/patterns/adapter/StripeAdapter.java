package org.example.patterns.adapter;
/**
 * OBJECT ADAPTER (kompozycja) — preferowane podejście w Javie.
 * Adapter implementuje TARGET interface (PaymentProcessor)
 * i wewnętrznie deleguje do ADAPTEE (LegacyStripeApi).
 */
public class StripeAdapter implements PaymentProcessor {

    private final LegacyStripeApi stripe;
    private final String stripeCustomerIdPrefix;

    public StripeAdapter(LegacyStripeApi stripe, String stripeCustomerIdPrefix) {
        this.stripe = stripe;
        this.stripeCustomerIdPrefix = stripeCustomerIdPrefix;
    }

    @Override
    public boolean pay(String customerId, double amountPln, String description) {
        // Tłumaczenie parametrów: PLN (double) → grosze (long), customerId → stripeCustomerId
        long cents = Math.round(amountPln * 100);
        String stripeCustomerId = stripeCustomerIdPrefix + customerId;

        LegacyStripeApi.StripeResult res =
                stripe.charge(stripeCustomerId, cents, "PLN", description);

        // Tłumaczenie wyniku: StripeResult → boolean
        return res.success();
    }
}
