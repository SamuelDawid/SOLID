package org.example.patterns.abstractFactory;

import java.util.List;

public class HtmlBody implements Body {
    @Override public String render(List<String> rows) {
        StringBuilder sb = new StringBuilder("<table>\n");
        for (String r : rows) sb.append("  <tr><td>").append(r).append("</td></tr>\n");
        sb.append("</table>");
        return sb.toString();
    }
}