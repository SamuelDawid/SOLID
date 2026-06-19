package org.example.patterns.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Drzewo z iteracją PRE-ORDER.
 * Iterator ukrywa złożoną strukturę (rekurencja po drzewie).
 */
public class Tree<T> implements Iterable<T> {

    private final T value;
    private final List<Tree<T>> children = new ArrayList<>();

    public Tree(T value) { this.value = value; }

    public Tree<T> add(Tree<T> child) {
        children.add(child);
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private final java.util.Deque<Tree<T>> stack = new java.util.ArrayDeque<>();
            {
                stack.push(Tree.this);
            }

            @Override public boolean hasNext() { return !stack.isEmpty(); }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                Tree<T> node = stack.pop();
                for (int i = node.children.size() - 1; i >= 0; i--) {
                    stack.push(node.children.get(i));
                }
                return node.value;
            }
        };
    }
}
