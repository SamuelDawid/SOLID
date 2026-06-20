package org.example.patterns.builder;

import java.util.List;
import java.util.Objects;

/**
 * Nowoczesna alternatywa — record z STATYCZNĄ wewnętrzną klasą Builder.
 * Record sam w sobie ZASTĘPUJE prostą klasę POJO (Plain Old Java Object — „zwykły obiekt Javy") — getter, equals, hashCode, toString
 * są wygenerowane automatycznie. Builder pozostaje dla wygody konstrukcji.
 *
 * Kiedy record + Builder? Gdy:
 *  - chcesz niemutowalność i prostotę record'a
 *  - ALE masz wiele pól opcjonalnych
 *  - i nie chcesz konstruktora z 7 argumentami
 */
public record OrderRecord(String customerId, List<String> items,
                          double total, String coupon, boolean express) {

    // Compact constructor — walidacja przy tworzeniu
    public OrderRecord {
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(items,      "items");
        if (total < 0) throw new IllegalArgumentException("total >= 0");
        items = List.copyOf(items);    // defensywna kopia
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String customerId;
        private List<String> items = List.of();
        private double total;
        private String coupon;
        private boolean express = false;

        public Builder customerId(String id)         { this.customerId = id; return this; }
        public Builder items(List<String> i)         { this.items = i; return this; }
        public Builder total(double t)               { this.total = t; return this; }
        public Builder coupon(String c)              { this.coupon = c; return this; }
        public Builder express()                     { this.express = true; return this; }

        public OrderRecord build() {
            return new OrderRecord(customerId, items, total, coupon, express);
        }
    }
}
