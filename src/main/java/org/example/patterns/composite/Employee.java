package org.example.patterns.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Drugi przykład — struktura organizacyjna.
 * Pracownik MOŻE mieć podwładnych (jest zarówno liściem jak i composite).
 */
public class Employee {
    private final String name;
    private final double salary;
    private final List<Employee> reports = new ArrayList<>();

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public Employee addReport(Employee e) {
        reports.add(e);
        return this;
    }

    public double totalSalaryCost() {
        double total = salary;
        for (Employee e : reports) total += e.totalSalaryCost();
        return total;
    }

    public int teamSize() {
        int count = 1;
        for (Employee e : reports) count += e.teamSize();
        return count;
    }

    public void print(String prefix) {
        System.out.printf("%s%s (%.2f PLN)%n", prefix, name, salary);
        for (Employee e : reports) e.print(prefix + "  ");
    }
}
