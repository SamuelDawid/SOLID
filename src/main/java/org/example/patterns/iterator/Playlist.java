package org.example.patterns.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;

/**
 * Własna kolekcja Playlist implementuje Iterable<String>.
 * Klient może iterować przez for-each, NIE znając wewnętrznej struktury.
 */
public class Playlist implements Iterable<String> {

    private final List<String> songs = new ArrayList<>();

    public void add(String song) { songs.add(song); }

    public int size() { return songs.size(); }

    @Override
    public Iterator<String> iterator() {
        return new PlaylistIterator();
    }

    public Iterator<String> shuffleIterator() {
        List<String> copy = new ArrayList<>(songs);
        java.util.Collections.shuffle(copy);
        return copy.iterator();
    }

    private class PlaylistIterator implements Iterator<String> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < songs.size();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Brak więcej utworów");
            }
            return songs.get(index++);
        }
    }
}
