package org.example.patterns.singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton ENUM — rekomendowany przez Joshua Bloch'a (Effective Java, Item 3).
 *
 * Dlaczego enum jest NAJLEPSZY:
 * 1) Serializacja — deserializacja enuma daje DOKŁADNIE TĘ SAMĄ instancję
 *    (klasyczny Singleton po deserializacji daje DRUGĄ instancję, chyba że nadpiszesz readResolve()).
 * 2) Refleksja — Constructor.newInstance() na enumie rzuca IllegalArgumentException.
 *    Klasyczny Singleton można złamać przez setAccessible(true).
 * 3) Thread-safety — gwarantowana przez JVM, BEZ żadnej dodatkowej logiki.
 * 4) Lazy — instancja powstaje przy pierwszym dotknięciu enuma.
 */
public enum DatabasePool {

    INSTANCE;

    private final List<String> connections = new ArrayList<>();
    private final int poolSize = 5;

    DatabasePool() {
        System.out.println("DatabasePool: otwieram pulę połączeń...");
        for (int i = 0; i < poolSize; i++) {
            connections.add("conn-" + i);
        }
        System.out.println("DatabasePool: pula gotowa (" + poolSize + " połączeń)");
    }

    public String borrow() {
        if (connections.isEmpty()) {
            throw new IllegalStateException("Brak wolnych połączeń");
        }
        return connections.remove(connections.size() - 1);
    }

    public void giveBack(String conn) {
        connections.add(conn);
    }

    public int availableCount() { return connections.size(); }
}
