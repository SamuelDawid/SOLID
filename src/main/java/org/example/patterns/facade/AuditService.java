package org.example.patterns.facade;

public class AuditService {
    public void log(String message) {
        System.out.printf("[AUDIT] %s%n", message);
    }
}
