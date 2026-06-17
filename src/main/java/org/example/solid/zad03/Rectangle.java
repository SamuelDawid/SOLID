package org.example.solid.zad03;

public record Rectangle(int width, int height) implements Shape {

    public Rectangle {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Wymiary muszą być dodatnie");
        }
    }

    @Override
    public int area() {
        return width * height;
    }
}
