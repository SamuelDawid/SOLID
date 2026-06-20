package org.example.patterns.factoryMethod;

public class PushNotification implements Notification {
    private final String firebaseKey;

    public PushNotification(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    @Override
    public void send(String to, String message) {
        // FCM = Firebase Cloud Messaging, usługa Google do wysyłania powiadomień push na telefony
        System.out.printf("PUSH (FCM %s) do %s: %s%n",
                firebaseKey.substring(0, 4) + "...", to, message);
    }

    @Override public String channel() { return "PUSH"; }
}
