package org.example.patterns.abstractFactory;

public class PdfHeader implements Header {
    @Override public String render(String title) {
        return "[PDF] <<<TITLE: " + title + ">>>";
    }
}
