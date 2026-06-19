package org.example.patterns.strategy;

public class Main {
    public static void main(String[] args) {

        DiscountRegistry registry = new DiscountRegistry(Discounts.NONE)
                .register("STANDARD", Discounts.NONE)
                .register("BULK",     Discounts.BULK)
                .register("LOYALTY",  Discounts.LOYALTY)
                .register("SALE",     Discounts.SUMMER_SALE)
                .register("VIP",      Discounts.VIP);

        String[] typy = {"STANDARD", "BULK", "LOYALTY", "SALE", "VIP", "UNKNOWN"};
        double price = 49.99;
        int qty = 12;

        System.out.printf("%-10s | %s%n", "Typ", "Cena finalna");
        System.out.println("-----------+-------------");
        for (String typ : typy) {
            DiscountStrategy s = registry.get(typ);
            PriceCalculator calc = new PriceCalculator(s);
            System.out.printf("%-10s | %.2f PLN%n", typ, calc.calculate(price, qty));
        }

        System.out.println("\n=== VIP z bonusem -50 powyżej 500 ===");
        DiscountStrategy vipPlus = Discounts.withBonusAbove500(Discounts.VIP);
        System.out.printf("Cena: %.2f PLN%n",
                new PriceCalculator(vipPlus).calculate(price, qty));

        System.out.println("\n=== Dynamiczna zmiana strategii ===");
        PriceCalculator dynamic = new PriceCalculator(Discounts.NONE);
        System.out.printf("Bazowa: %.2f PLN%n", dynamic.calculate(price, qty));
        dynamic.setStrategy(Discounts.VIP);
        System.out.printf("Po zmianie na VIP: %.2f PLN%n", dynamic.calculate(price, qty));
    }
}
