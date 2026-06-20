package org.example.solid.zad05;

public class OrderServiceBefore {

    // PROBLEM 1: twarda zależność na implementacji
    private final MySqlOrderRepository repository = new MySqlOrderRepository();

    // PROBLEM 2: konkretne SMTP w polu
    private final SmtpEmailService email =
            new SmtpEmailService("smtp.gmail.com", 587, "user", "pass");

    public void placeOrder(String customer, double amount) {
        repository.save(customer, amount);
        email.send(customer, "Zamówienie przyjęte na " + amount + " PLN");
    }

    // Pomocnicze klasy (żeby kod się skompilował)
    static class MySqlOrderRepository {
        public void save(String customer, double amount) {
            System.out.println("MySQL INSERT: " + customer + "/" + amount);
        }
    }

    static class SmtpEmailService {
        public SmtpEmailService(String host, int port, String user, String pass) {}
        public void send(String to, String body) {
            System.out.println("SMTP -> " + to + ": " + body);
        }
    }
}
