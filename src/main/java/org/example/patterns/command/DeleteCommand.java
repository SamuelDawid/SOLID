package org.example.patterns.command;

public class DeleteCommand implements Command {

    private final TextDocument doc;
    private final int from;
    private final int to;
    private String deletedText;

    public DeleteCommand(TextDocument doc, int from, int to) {
        this.doc = doc;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute() {
        deletedText = doc.getContent().substring(from, to);
        doc.delete(from, to);
    }

    @Override
    public void undo() {
        if (deletedText != null) {
            doc.insert(from, deletedText);
        }
    }
}
