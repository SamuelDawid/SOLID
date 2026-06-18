package org.example.patterns.factoryMethod;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Statyczna fabryka — odpowiada na pytanie „jaki obiekt utworzyć?".
 * Klient zna tylko interfejs Notification, nie konkretne klasy.
 */
public class NotificationFactory {

    // Konfiguracja — w realnej aplikacji wczytana z pliku properties
    private static final String EMAIL_FROM = "noreply@fefe2022.com";
    private static final String SMS_GATEWAY = "https://sms.example.com/api";
    private static final String FCM_KEY = "FCM-PROD-KEY-12345";
    private static final String Platform = "FACEBOOK";

    private NotificationFactory() {
        // Klasa pomocnicza — nikt nie powinien jej instancjonować
    }
    private static final Map<String, Supplier<Notification>> REGISTRY = Map.of(
            "EMAIL", () -> new EmailNotification(EMAIL_FROM),
            "SMS",   () -> new SmsNotification(SMS_GATEWAY),
            "PUSH",  () -> new PushNotification(FCM_KEY),
            "SLACK", () -> new SlackNotification(Platform)
    );
    public static Notification create(String type) {
        var supplier = REGISTRY.get(type.toUpperCase());
        if (supplier == null) throw new IllegalArgumentException("Nieznany: " + type);
        return supplier.get();
    }
    public static void register(String type, Supplier<Notification> supplier){}
}
