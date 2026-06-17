package org.example.patterns;

/**
 * Singleton LAZY z Double-Checked Locking (DCL).
 * Używaj, gdy inicjalizacja jest droga (otwiera pulę, ładuje wielki plik, indeks Lucene)
 * i chcesz ją odroczyć do PIERWSZEGO wywołania getInstance().
 *
 * Kluczowe: pole volatile — gwarantuje widoczność między wątkami (happens-before)
 * i zapobiega instruction reordering. Bez volatile inny wątek może zobaczyć
 * referencję do obiektu PRZED zakończeniem konstruktora.
 */
public class HeavyServiceDCL {

    private static HeavyServiceDCL instance;

    private HeavyServiceDCL() {
        System.out.println("HeavyServiceDCL: ciężka inicjalizacja...");
        try { Thread.sleep(300); } catch (InterruptedException ignored) {}
        System.out.println("HeavyServiceDCL: inicjalizacja zakończona");
    }

    public static HeavyServiceDCL getInstance() {
        if (instance == null) {                       // 1. szybka ścieżka bez locka
            synchronized (HeavyServiceDCL.class) {
                if (instance == null) {               // 2. ścieżka bezpieczna z lockiem
                    instance = new HeavyServiceDCL();
                }
            }
        }
        return instance;
    }

    public void doExpensiveWork() {
        System.out.println("HeavyServiceDCL: wykonuję drogie wyliczenia");
    }
}