package org.example.patterns.cor;

import java.util.ArrayDeque;
import java.util.Deque;

public class RateLimitHandler extends RequestHandler {

    private final Deque<Long> recent = new ArrayDeque<>();
    private static final int LIMIT = 5;
    private static final long WINDOW_MS = 1000;

    @Override
    public synchronized void handle(HttpRequest req) {
        long now = System.currentTimeMillis();
        while (!recent.isEmpty() && now - recent.peekFirst() > WINDOW_MS) {
            recent.pollFirst();
        }
        if (recent.size() >= LIMIT) {
            System.out.println("[RATE-LIMIT] ODRZUCONO — 429");
            return;
        }
        recent.add(now);
        System.out.println("[RATE-LIMIT] OK (" + recent.size() + "/" + LIMIT + ")");
        passToNext(req);
    }
}
