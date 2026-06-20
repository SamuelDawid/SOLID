package org.example.patterns.iterator;

public class Main {
    public static void main(String[] args) {

        // 1) Playlist — własna kolekcja w for-each
        Playlist pl = new Playlist();
        pl.add("Beat It");
        pl.add("Smooth Criminal");
        pl.add("Thriller");

        System.out.println("=== Standardowo ===");
        for (String song : pl) {
            System.out.println("  " + song);
        }

        System.out.println("\n=== Shuffle (drugi iterator) ===");
        var it = pl.shuffleIterator();
        while (it.hasNext()) {
            System.out.println("  " + it.next());
        }

        // 2) RangeIterator — iterator BEZ kolekcji
        System.out.println("\n=== Range 0..10 co 2 ===");
        for (int i : new RangeIterator(0, 10, 2)) {
            System.out.print(i + " ");
        }
        System.out.println();

        // 3) Tree — iterator po drzewie w pre-order
        System.out.println("\n=== Tree pre-order ===");
        Tree<String> root = new Tree<>("root")
                .add(new Tree<>("A")
                        .add(new Tree<>("A1"))
                        .add(new Tree<>("A2")))
                .add(new Tree<>("B"))
                .add(new Tree<>("C")
                        .add(new Tree<>("C1")));

        for (String value : root) {
            System.out.println("  " + value);
        }
    }
}
