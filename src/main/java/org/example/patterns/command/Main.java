package org.example.patterns.command;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        TextDocument doc = new TextDocument();
        EditorInvoker editor = new EditorInvoker();

        editor.execute(new InsertCommand(doc, 0, "Hello"));
        System.out.println("[1] " + doc.getContent());

        editor.execute(new InsertCommand(doc, 5, " World"));
        System.out.println("[2] " + doc.getContent());

        editor.execute(new InsertCommand(doc, 11, "!"));
        System.out.println("[3] " + doc.getContent());

        editor.undo();
        System.out.println("[undo] " + doc.getContent());

        editor.undo();
        System.out.println("[undo] " + doc.getContent());

        editor.redo();
        System.out.println("[redo] " + doc.getContent());

        System.out.println("\n=== Makro ===");
        Command macro = new MacroCommand(List.of(
                new InsertCommand(doc, doc.length(), " [TEMP]"),
                new DeleteCommand(doc, 0, 5)
        ));
        editor.execute(macro);
        System.out.println("[macro] " + doc.getContent());

        editor.undo();
        System.out.println("[undo macro] " + doc.getContent());
    }
}
