package org.example.patterns.factoryMethod;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Klient zna TYLKO interfejs Notification i fabrykę
        List<String> typy = List.of("EMAIL", "SMS", "PUSH");

        for (String typ : typy) {
            Notification n = NotificationFactory.create(typ);
            System.out.println("Utworzono kanał: " + n.channel());
            n.send("klient@firma.pl", "Twoje zamówienie zostało przyjęte");
        }

        // Symulacja błędu
        try {
            NotificationFactory.create("FAX");
        } catch (IllegalArgumentException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }
}
