package org.example.patterns.composite;

/** LIŚĆ — plik nie ma dzieci. */
public class FileNode implements FileSystemNode {
    private final String name;
    private final long size;

    public FileNode(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override public String getName() { return name; }
    @Override public long getSize()   { return size; }

    @Override
    public void print(String prefix) {
        System.out.printf("%s- %s (%d B)%n", prefix, name, size);
    }
}
