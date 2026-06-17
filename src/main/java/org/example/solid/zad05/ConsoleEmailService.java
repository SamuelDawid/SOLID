package org.example.solid.zad05;

public class ConsoleEmailService implements EmailService {
    @Override
    public void send(String to, String body) {
        System.out.println("KONSOLA -> " + to + ": " + body);
    }
}
