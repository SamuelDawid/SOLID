package org.example.patterns.abstractFactory;

public class HtmlFooter implements Footer {
    @Override public String render(String author) {
        return "<footer>autor: " + author + "</footer>";
    }
}
