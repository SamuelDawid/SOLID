package org.example.patterns.decorator;

/**
 * Wspólna klasa bazowa dla dekoratorów — opcjonalna, ale wygodna.
 * Implementuje Coffee i trzyma delegate. Konkretne dekoratory
 * nadpisują tylko to, co rzeczywiście zmieniają.
 */
public abstract class CoffeeDecorator implements Coffee {

    protected final Coffee delegate;

    protected CoffeeDecorator(Coffee delegate) {
        this.delegate = delegate;
    }

    // Domyślne implementacje — delegują do wrappee.
    // Konkretne dekoratory nadpisują w razie potrzeby.
    @Override public String description() { return delegate.description(); }
    @Override public double cost()        { return delegate.cost(); }
}
