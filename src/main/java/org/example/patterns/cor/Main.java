package org.example.patterns.cor;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        // 1) Klasyczny łańcuch z setNext
        RequestHandler auth     = new AuthHandler();
        RequestHandler logging  = new LoggingHandler();
        RequestHandler content  = new ContentTypeHandler();
        RequestHandler rateLim  = new RateLimitHandler();
        RequestHandler business = new BusinessHandler();

        logging.setNext(auth).setNext(content).setNext(rateLim).setNext(business);

        System.out.println("=== Poprawne żądanie ===");
        logging.handle(new HttpRequest(
                "/api/orders",
                "Bearer valid-token-123",
                "{\"item\":\"laptop\"}",
                Map.of("Content-Type", "application/json")));

        System.out.println("\n=== Brak tokenu ===");
        logging.handle(new HttpRequest(
                "/api/orders", null, "{}",
                Map.of("Content-Type", "application/json")));

        System.out.println("\n=== Zły Content-Type ===");
        logging.handle(new HttpRequest(
                "/api/orders", "Bearer valid-token-123",
                "item=laptop",
                Map.of("Content-Type", "application/x-www-form-urlencoded")));

        // 2) Pipeline z listą — alternatywna implementacja
        System.out.println("\n=== Pipeline (lista) ===");
        Pipeline pipeline = new Pipeline(List.of(
                req -> { System.out.println("[P-LOG] " + req.path()); return true; },
                req -> {
                    System.out.println("[P-AUTH]");
                    return req.hasToken();
                },
                req -> {
                    System.out.println("[P-CONTENT]");
                    return req.isJson();
                },
                req -> {
                    System.out.println("[P-BUSINESS] " + req.body());
                    return true;
                }
        ));
        pipeline.execute(new HttpRequest(
                "/api/test", "Bearer x", "{\"ok\":true}",
                Map.of("Content-Type", "application/json")));
    }
}
