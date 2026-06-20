package org.example.patterns.abstractFactory;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> dane = List.of("Anna 1200 PLN", "Jan 950 PLN", "Maria 1800 PLN");

        // Decyzja podejmowana raz — np. na podstawie konfiguracji / żądania użytkownika
        ReportFactory factory = wybierzFormat("PDF");
        ReportPrinter printer = new ReportPrinter(factory);
        System.out.println(printer.build("Raport sprzedaży", dane, "Anna Kowalska"));

        System.out.println("\n--- ten sam klient, inna rodzina ---\n");

        ReportFactory factory2 = wybierzFormat("HTML");
        ReportPrinter printer2 = new ReportPrinter(factory2);
        System.out.println(printer2.build("Raport sprzedaży", dane, "Anna Kowalska"));
    }

    private static ReportFactory wybierzFormat(String fmt) {
        return switch (fmt) {
            case "PDF"  -> new PdfReportFactory();
            case "HTML" -> new HtmlReportFactory();
            default     -> throw new IllegalArgumentException("Brak: " + fmt);
        };
    }
}
