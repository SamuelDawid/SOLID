package org.example.patterns.command;

public class InsertCommand implements Command {

    private final TextDocument doc;
    private final int position;
    private final String text;

    public InsertCommand(TextDocument doc, int position, String text) {
        this.doc = doc;
        this.position = position;
        this.text = text;
    }

    @Override
    public void execute() {
        doc.insert(position, text);
    }

    @Override
    public void undo() {
        doc.delete(position, position + text.length());
    }
}
