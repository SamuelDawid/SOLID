package org.example.patterns.abstractFactory;

public class HtmlHeader implements Header {
    @Override public String render(String title) {
        return "<h1>" + title + "</h1>";
    }
}
