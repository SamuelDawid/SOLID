package org.example.patterns.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator bez kolekcji bazowej.
 * Generuje liczby z zakresu [start, end) z krokiem.
 */
public class RangeIterator implements Iterable<Integer> {

    private final int start;
    private final int end;
    private final int step;

    public RangeIterator(int start, int end, int step) {
        if (step <= 0) throw new IllegalArgumentException("step > 0");
        this.start = start;
        this.end = end;
        this.step = step;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            int current = start;

            @Override public boolean hasNext() { return current < end; }

            @Override
            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                int value = current;
                current += step;
                return value;
            }
        };
    }
}
