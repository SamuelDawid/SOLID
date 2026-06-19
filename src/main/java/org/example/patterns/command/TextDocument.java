package org.example.patterns.command;

/** RECEIVER — obiekt, na którym komendy operują. */
public class TextDocument {

    private final StringBuilder content = new StringBuilder();

    public void insert(int position, String text) {
        content.insert(position, text);
    }

    public void delete(int from, int to) {
        content.delete(from, to);
    }

    public String getContent()  { return content.toString(); }
    public int length()         { return content.length(); }
}
