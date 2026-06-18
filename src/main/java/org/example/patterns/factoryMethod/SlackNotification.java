package org.example.patterns.factoryMethod;

public class SlackNotification implements Notification{
    private final String platform;

    public SlackNotification(String platform) {
        this.platform = platform;
    }

    @Override
    public void send(String to, String message) {
        System.out.printf("new SLACK notification: %s do %s: %s%n", platform,to,message);
    }

    @Override
    public String channel() {
        return "SLACK";
    }
}
