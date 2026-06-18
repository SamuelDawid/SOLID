package org.example.patterns.abstractFactory;

import java.util.List;

public class PdfBody implements Body {
    @Override public String render(List<String> rows) {
        StringBuilder sb = new StringBuilder("[PDF] Tabela:\n");
        for (String r : rows) sb.append("  | ").append(r).append(" |\n");
        return sb.toString();
    }
}
