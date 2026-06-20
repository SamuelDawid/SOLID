package org.example.patterns.cor;

public class ContentTypeHandler extends RequestHandler {

    @Override
    public void handle(HttpRequest req) {
        System.out.println("[CONTENT-TYPE] sprawdzam");
        if (!req.isJson()) {
            System.out.println("[CONTENT-TYPE] ODRZUCONO — 415");
            return;
        }
        System.out.println("[CONTENT-TYPE] OK");
        passToNext(req);
    }
}
