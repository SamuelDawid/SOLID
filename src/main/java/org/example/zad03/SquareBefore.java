package org.example.zad03;

public class SquareBefore extends RectangleBefore {

    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width;   // kwadrat MUSI mieć równe boki
    }

    @Override
    public void setHeight(int height) {
        this.width = height;
        this.height = height;
    }
}
