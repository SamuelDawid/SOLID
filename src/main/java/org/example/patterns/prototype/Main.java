package org.example.patterns.prototype;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        // 1) Mutowalny prototyp — szablon umowy
        DocumentTemplate template = new DocumentTemplate(
                "Umowa o pracę",
                "TREŚĆ UMOWY...",
                "Zarząd",
                List.of("Pracodawca"));

        // Wariant 1 — Anna
        DocumentTemplate anna = template.copy();
        anna.setTitle("Umowa o pracę — Anna Kowalska");
        anna.addSignatory("Anna Kowalska");
        System.out.println("Oryginał: " + template);
        System.out.println("Anna:    " + anna);

        // Wariant 2 — Jan, niezależny od Anny
        DocumentTemplate jan = template.copy();
        jan.setTitle("Umowa o pracę — Jan Nowak");
        jan.addSignatory("Jan Nowak");
        System.out.println("Jan:     " + jan);
        System.out.println("Oryginał wciąż czysty: " + template);

        // 2) Niemutowalny prototyp — konfiguracja serwera
        ServerConfig base = new ServerConfig(
                "localhost", 8080, Map.of("Content-Type", "application/json"));

        ServerConfig prod = base.withHost("prod.example.com")
                .withPort(443)
                .withHeader("X-Env", "prod");

        ServerConfig test = base.withHost("test.example.com")
                .withHeader("X-Env", "test");

        System.out.println("\nbase: " + base);
        System.out.println("prod: " + prod);
        System.out.println("test: " + test);
    }
}