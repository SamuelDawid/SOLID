package org.example.patterns.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * COMPOSITE — folder zawiera dzieci (pliki LUB inne foldery).
 * getSize() rekurencyjnie sumuje rozmiary dzieci.
 */
public class FolderNode implements FileSystemNode {
    private final String name;
    private final List<FileSystemNode> children = new ArrayList<>();

    public FolderNode(String name) { this.name = name; }

    public FolderNode add(FileSystemNode child) {
        children.add(child);
        return this;
    }

    public void remove(FileSystemNode child) {
        children.remove(child);
    }

    @Override public String getName() { return name; }

    @Override
    public long getSize() {
        long total = 0;
        for (FileSystemNode child : children) {
            total += child.getSize();
        }
        return total;
    }

    @Override
    public void print(String prefix) {
        System.out.printf("%s+ %s/ (%d B)%n", prefix, name, getSize());
        for (FileSystemNode child : children) {
            child.print(prefix + "  ");
        }
    }
}
