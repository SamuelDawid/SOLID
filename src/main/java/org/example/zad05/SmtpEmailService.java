package org.example.zad05;

public class SmtpEmailService implements EmailService {

    private final String host;
    private final int port;

    public SmtpEmailService(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void send(String to, String body) {
        System.out.println("SMTP[" + host + ":" + port + "] -> " + to + ": " + body);
    }
}
