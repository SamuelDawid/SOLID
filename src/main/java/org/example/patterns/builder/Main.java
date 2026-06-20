package org.example.patterns.builder;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // 1) Email Builder — wiele opcjonalnych pól
        Email mail = Email.builder()
                .from("nadawca@firma.pl")
                .to("odbiorca@klient.pl")
                .subject("Raport miesięczny")
                .body("<h1>Raport za styczeń</h1>")
                .cc("kopia@firma.pl")
                .html()
                .attach("/tmp/raport.pdf")
                .attach("/tmp/zestawienie.xlsx")
                .build();
        System.out.println(mail);

        // 2) SQL Query Builder — fragmentaryczna konstrukcja
        String sql = new SqlQueryBuilder()
                .select("id", "name", "email")
                .from("users")
                .where("active = true")
                .where("age >= 18")
                .orderBy("name")
                .limit(50)
                .build();
        System.out.println("SQL: " + sql);

        // 3) Record + Builder
        OrderRecord order = OrderRecord.builder()
                .customerId("CUST-42")
                .items(List.of("Laptop", "Mysz", "Mata"))
                .total(4299.50)
                .coupon("BLACKFRIDAY")
                .express()
                .build();
        System.out.println(order);

        // 4) Demonstracja błędu — brak wymaganego pola
        try {
            Email.builder().subject("Tylko temat").build();
        } catch (NullPointerException e) {
            System.out.println("Złapano NPE (NullPointerException): " + e.getMessage());
        }

        // 5) Demonstracja błędu — SQL bez FROM
        try {
            new SqlQueryBuilder().select("*").build();
        } catch (IllegalStateException e) {
            System.out.println("Złapano stan: " + e.getMessage());
        }
    }
}