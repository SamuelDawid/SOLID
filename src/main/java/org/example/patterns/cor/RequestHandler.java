package org.example.patterns.cor;

public abstract class RequestHandler {

    protected RequestHandler next;

    /** Zwraca next, by umożliwić chaining: h1.setNext(h2).setNext(h3). */
    public RequestHandler setNext(RequestHandler next) {
        this.next = next;
        return next;
    }

    public abstract void handle(HttpRequest request);

    protected void passToNext(HttpRequest request) {
        if (next != null) {
            next.handle(request);
        } else {
            System.out.println("[CHAIN] Koniec łańcucha");
        }
    }
}
