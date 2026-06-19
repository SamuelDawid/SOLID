package org.example.patterns.command;

import java.util.List;
import java.util.ArrayList;

/**
 * MAKRO — composite Command. Sekwencja komend traktowana jako jedna.
 * Undo cofa komendy w ODWROTNEJ kolejności.
 */
public class MacroCommand implements Command {

    private final List<Command> commands;

    public MacroCommand(List<Command> commands) {
        this.commands = new ArrayList<>(commands);
    }

    @Override
    public void execute() {
        for (Command c : commands) c.execute();
    }

    @Override
    public void undo() {
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }
}
