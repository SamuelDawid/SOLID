package org.example.patterns.factoryMethod;

public class EmailNotification implements Notification {
    private final String fromAddress;

    public EmailNotification(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    @Override
    public void send(String to, String message) {
        System.out.printf("EMAIL z %s do %s: %s%n", fromAddress, to, message);
        // tu: prawdziwy klient SMTP (protokół wysyłania emaili), np. biblioteka JavaMail
    }

    @Override public String channel() { return "EMAIL"; }
}
