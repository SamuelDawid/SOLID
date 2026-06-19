package org.example.patterns.strategy;

/**
 * Wszystkie strategie jako STATIC FINAL lambdy w jednej klasie.
 * To czyste, czytelne i łatwe do utrzymania.
 */
public final class Discounts {

    private Discounts() {}

    public static final DiscountStrategy NONE =
            (price, qty) -> price * qty;

    public static final DiscountStrategy BULK =
            (price, qty) -> price * qty * (qty >= 10 ? 0.85 : 1.0);

    public static final DiscountStrategy LOYALTY =
            (price, qty) -> price * qty * 0.90;

    public static final DiscountStrategy SUMMER_SALE =
            (price, qty) -> Math.max(price * qty - 50, 0);

    public static final DiscountStrategy VIP =
            (price, qty) -> price * qty * 0.80;

    /** Dekorator strategii — dodaje warunkowy bonus -50 PLN dla totalu > 500. */
    public static DiscountStrategy withBonusAbove500(DiscountStrategy base) {
        return (price, qty) -> {
            double total = base.apply(price, qty);
            return total > 500 ? total - 50 : total;
        };
    }
}
