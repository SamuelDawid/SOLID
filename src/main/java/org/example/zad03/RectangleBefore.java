package org.example.zad03;

public class RectangleBefore {

    protected int width;
    protected int height;

    public void setWidth(int width)   { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public int getWidth()  { return width; }
    public int getHeight() { return height; }
    public int area()      { return width * height; }
}
