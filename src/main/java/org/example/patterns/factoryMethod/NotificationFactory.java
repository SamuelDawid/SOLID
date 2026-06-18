package org.example.patterns.factoryMethod;
/**
 * Statyczna fabryka — odpowiada na pytanie „jaki obiekt utworzyć?".
 * Klient zna tylko interfejs Notification, nie konkretne klasy.
 */
public class NotificationFactory {

    // Konfiguracja — w realnej aplikacji wczytana z pliku properties
    private static final String EMAIL_FROM   = "noreply@fefe2022.com";
    private static final String SMS_GATEWAY  = "https://sms.example.com/api";
    private static final String FCM_KEY      = "FCM-PROD-KEY-12345";

    private NotificationFactory() {
        // Klasa pomocnicza — nikt nie powinien jej instancjonować
    }

    public static Notification create(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Typ nie może być null");
        }
        return switch (type.toUpperCase()) {
            case "EMAIL" -> new EmailNotification(EMAIL_FROM);
            case "SMS"   -> new SmsNotification(SMS_GATEWAY);
            case "PUSH"  -> new PushNotification(FCM_KEY);
            default      -> throw new IllegalArgumentException(
                    "Nieznany typ powiadomienia: " + type);
        };
    }
}
