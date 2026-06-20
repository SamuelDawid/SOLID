package org.example.patterns.facade;

import java.util.List;

public record Order(String orderId, String customerId,
                    List<String> items, double total) {}
