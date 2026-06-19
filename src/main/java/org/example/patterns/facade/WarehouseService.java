package org.example.patterns.facade;

import java.util.List;

public class WarehouseService {
    public boolean reserve(List<String> items) {
        System.out.println("[WAREHOUSE] rezerwuję: " + items);
        return true;
    }

    public void release(List<String> items) {
        System.out.println("[WAREHOUSE] zwalniam: " + items);
    }
}
