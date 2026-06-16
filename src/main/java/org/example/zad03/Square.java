package org.example.zad03;

public record Square(int side) implements Shape {

    public Square {
        if (side <= 0) {
            throw new IllegalArgumentException("Bok musi być dodatni");
        }
    }

    @Override
    public int area() {
        return side * side;
    }
}
