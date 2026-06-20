package org.example.patterns.abstractFactory;

public class PdfFooter implements Footer {
    @Override public String render(String author) {
        return "[PDF] Strona 1/1 — wygenerował: " + author;
    }
}