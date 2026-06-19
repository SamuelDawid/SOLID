package org.example.patterns.composite;

/** Wspólny interfejs — file i folder go implementują. */
public interface FileSystemNode {
    String getName();
    long getSize();
    void print(String prefix);
}
