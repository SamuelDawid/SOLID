package org.example.solid.zad03;

class IntegerStack {
    /** Po push(x), peek() == x */
    public void push(int x) { /* ... */ }
}

class CountingStack extends IntegerStack {
    @Override
    public void push(int x) {
        // PUŁAPKA: jeśli wartość już była, nie wkładamy ponownie — peek() != x
        if (!contains(x)) super.push(x);
    }

    private boolean contains(int x) { return false; }
}
