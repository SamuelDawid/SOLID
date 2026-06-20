package org.example.patterns.cor;

public class BusinessHandler extends RequestHandler {

    @Override
    public void handle(HttpRequest req) {
        System.out.println("[BUSINESS] przetwarzam " + req.path());
        System.out.println("[BUSINESS] Odpowiedź: 200 OK");
    }
}
