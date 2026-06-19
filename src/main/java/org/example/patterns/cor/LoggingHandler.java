package org.example.patterns.cor;

public class LoggingHandler extends RequestHandler {

    @Override
    public void handle(HttpRequest req) {
        System.out.printf("[LOG] %s — body: %d bajtów%n",
                req.path(), req.body() == null ? 0 : req.body().length());
        passToNext(req);
    }
}
