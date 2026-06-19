package org.example.patterns.command;

/** Wszystkie komendy mają tę samą sygnaturę. */
public interface Command {
    void execute();
    void undo();
}
