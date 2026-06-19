package org.example.patterns.templatemethod;

import java.util.List;

/**
 * Klasa bazowa z TEMPLATE METHOD.
 *
 * importData() to szkielet algorytmu (final — nie do nadpisania).
 * Konkretne kroki:
 *  - read() — wspólny,
 *  - parse() — abstrakcyjny (różny dla CSV/JSON/XML),
 *  - validate() — domyślna implementacja, podklasa może nadpisać,
 *  - save() — wspólny,
 *  - onError() — HOOK, pusta implementacja.
 */
public abstract class DataImporter {

    public final void importData(String filePath) {
        System.out.println("=== Start importu: " + filePath + " ===");
        try {
            String raw = read(filePath);
            List<Record> records = parse(raw);
            List<Record> valid = validate(records);
            save(valid);
            System.out.println("=== Import zakończony ===");
        } catch (Exception e) {
            onError(e);
            throw new RuntimeException(e);
        }
    }

    protected String read(String filePath) {
        System.out.println("[READ] otwieram " + filePath);
        return "Anna,30\nJan,25\nKasia,40";
    }

    protected abstract List<Record> parse(String raw);

    protected List<Record> validate(List<Record> records) {
        System.out.println("[VALIDATE] " + records.size() + " rekordów");
        return records.stream()
                .filter(r -> r.age() >= 0 && r.age() < 150)
                .toList();
    }

    protected void save(List<Record> records) {
        System.out.println("[SAVE] zapisuję " + records.size() + " rekordów do bazy");
        for (Record r : records) {
            System.out.println("  → " + r);
        }
    }

    protected void onError(Exception e) {}

    public record Record(String name, int age) {}
}
