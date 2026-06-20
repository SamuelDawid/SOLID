package org.example.patterns.abstractFactory;

import java.util.List;

/**
 * Klient — operuje TYLKO na interfejsach.
 * Nie wie, czy fabryka produkuje PDF czy HTML.
 */
public class ReportPrinter {
    private final ReportFactory factory;

    public ReportPrinter(ReportFactory factory) { this.factory = factory; }

    public String build(String title, List<String> rows, String author) {
        return factory.createHeader().render(title)
                + "\n" + factory.createBody().render(rows)
                + "\n" + factory.createFooter().render(author);
    }
}
