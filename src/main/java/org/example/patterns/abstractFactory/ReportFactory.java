package org.example.patterns.abstractFactory;

/**
 * Abstract Factory — interfejs fabrykujący CAŁĄ RODZINĘ obiektów.
 * Każda konkretna implementacja produkuje spójny zestaw (PDF / HTML / CSV).
 */
public interface ReportFactory {
    Header createHeader();
    Body   createBody();
    Footer createFooter();
}
