package org.example.zad01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class OrderManagerBefore {

    public static class Cart {
        private final List<CartItem> items;
        public Cart(List<CartItem> items) { this.items = items; }
        public boolean isEmpty() { return items.isEmpty(); }
        public List<CartItem> getItems() { return items; }
    }

    public static class CartItem {
        private final double price;
        private final int quantity;
        public CartItem(double price, int quantity) {
            this.price = price;
            this.quantity = quantity;
        }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
    }

    public static class Order {
        private long id;
        private double total;
        public long getId() { return id; }
        public void setId(long id) { this.id = id; }
        public double getTotal() { return total; }
        public void setTotal(double total) { this.total = total; }
    }

    public Order createOrder(Cart cart) {
        // 1. LOGIKA BIZNESOWA: walidacja koszyka, kalkulacja sumy
        if (cart.isEmpty()) {
            throw new IllegalStateException("Pusty koszyk");
        }
        Order order = new Order();
        double total = 0;
        for (CartItem i : cart.getItems()) {
            total += i.getPrice() * i.getQuantity();
        }
        order.setTotal(total);
        return order;
    }

    public void saveToDatabase(Order order) throws Exception {
        // 2. WARSTWA DANYCH: SQL/JDBC
        Connection c = DriverManager.getConnection("jdbc:postgresql://localhost/shop");
        PreparedStatement ps = c.prepareStatement("INSERT INTO orders(total) VALUES(?)");
        ps.setDouble(1, order.getTotal());
        ps.executeUpdate();
    }

    public void sendConfirmationEmail(Order order) {
        // 3. INFRASTRUKTURA MAILOWA: SMTP
        System.out.println("SMTP: wysyłam potwierdzenie zamówienia #" + order.getId());
    }

    public String generatePdfReport(Order order) {
        // 4. GENEROWANIE RAPORTÓW PDF
        System.out.println("PDF: renderuję raport dla zamówienia #" + order.getId());
        return "/tmp/order-" + order.getId() + ".pdf";
    }
}
