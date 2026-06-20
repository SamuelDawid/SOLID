package org.example.patterns.cor;

import java.util.List;

/**
 * Alternatywna implementacja — lista handlerów zamiast pointerów next.
 * Często czytelniejsze, łatwo zmienić kolejność lub usunąć etap.
 */
public class Pipeline {

    @FunctionalInterface
    public interface Step {
        /** true = przekaż dalej, false = zatrzymaj. */
        boolean handle(HttpRequest req);
    }

    private final List<Step> steps;

    public Pipeline(List<Step> steps) {
        this.steps = List.copyOf(steps);
    }

    public void execute(HttpRequest req) {
        for (Step step : steps) {
            if (!step.handle(req)) return;
        }
    }
}
