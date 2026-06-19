package org.example.patterns.observer.pcs;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Klasyczny mechanizm Observer w JDK — PropertyChangeSupport.
 * Używany w Swing/JavaFX dla powiadamiania o zmianach pól.
 */
public class StockItem {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private int quantity;
    private double price;

    public StockItem(int quantity, double price) {
        this.quantity = quantity;
        this.price = price;
    }

    public void setQuantity(int newQty) {
        int oldQty = this.quantity;
        this.quantity = newQty;
        pcs.firePropertyChange("quantity", oldQty, newQty);
    }

    public void setPrice(double newPrice) {
        double oldPrice = this.price;
        this.price = newPrice;
        pcs.firePropertyChange("price", oldPrice, newPrice);
    }

    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
