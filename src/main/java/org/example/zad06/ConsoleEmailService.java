package org.example.zad06;

public class ConsoleEmailService implements EmailService {

    @Override
    public void sendWelcome(User user) {
        System.out.printf("EMAIL → %s: Witamy w serwisie, %s!%n",
                user.email(), user.name());
    }
}
