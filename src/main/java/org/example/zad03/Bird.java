package org.example.zad03;

class Bird {
    public void move() { /* spaceruje albo lata */ }
}

class Penguin extends Bird {
    @Override
    public void move() {
        if (waterAvailable()) {
            swim();
        } else {
            throw new IllegalStateException("Pingwin nie chodzi po lądzie bez wody!");
        }
    }

    private boolean waterAvailable() { return false; }
    private void swim() {}
}

