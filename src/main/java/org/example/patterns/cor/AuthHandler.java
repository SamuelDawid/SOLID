package org.example.patterns.cor;

public class AuthHandler extends RequestHandler {

    private static final String VALID_TOKEN = "Bearer valid-token-123";

    @Override
    public void handle(HttpRequest req) {
        System.out.println("[AUTH] sprawdzam token");
        if (!req.hasToken() || !req.token().equals(VALID_TOKEN)) {
            System.out.println("[AUTH] ODRZUCONO — 401");
            return;
        }
        System.out.println("[AUTH] OK");
        passToNext(req);
    }
}
