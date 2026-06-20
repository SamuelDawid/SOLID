package org.example.patterns.factoryMethod;

public class SmsNotification implements Notification {
    private final String gatewayUrl;

    public SmsNotification(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    @Override
    public void send(String to, String message) {
        System.out.printf("SMS przez %s do %s: %s%n", gatewayUrl, to, message);
    }

    @Override public String channel() { return "SMS"; }
}
