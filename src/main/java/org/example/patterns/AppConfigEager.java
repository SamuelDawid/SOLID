package org.example.patterns;
/**
 * Singleton EAGER — instancja tworzona przy ładowaniu klasy.
 * Mechanizm class loadera JVM gwarantuje, że pole `static final` zostanie
 * zainicjalizowane DOKŁADNIE RAZ — zatem implementacja jest thread-safe „za darmo".
 *
 * Kiedy stosować: konfiguracja jest tania w stworzeniu i ZAWSZE potrzebna.
 */
public class AppConfigEager {

    // Pole static final — tworzone raz, przy pierwszym dotknięciu klasy
    private static final AppConfigEager INSTANCE = new AppConfigEager();
    String userTheme;
    // Prywatny konstruktor — nikt z zewnątrz nie wywoła new AppConfigEager()
    private AppConfigEager() {
        System.out.println("AppConfigEager: konstruktor wywołany");
    }

    public static AppConfigEager getInstance() {
        return INSTANCE;
    }

    // Przykładowe niemutowalne pola konfiguracji
    private final String dbUrl = "jdbc:postgresql://localhost:5432/mydb";
    private final int maxConnections = 10;
    private final String appName = "FEFE-CRM";
    public String setUserTheme(String theme){ return this.userTheme = theme;}
    public String getDbUrl()         { return dbUrl; }
    public int getMaxConnections()   { return maxConnections; }
    public String getAppName()       { return appName; }
}
