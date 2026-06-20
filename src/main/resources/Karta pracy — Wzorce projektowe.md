# Karta pracy — Wzorce projektowe (GoF — *Gang of Four*, „banda czterech" autorów klasycznej książki)

> Karta pracy do lekcji `29_wzorce_projektowe.md`. Implementujesz **17 najczęściej spotykanych wzorców** **GoF** (*Gang of Four* — czterech autorów książki *Design Patterns* z 1994 roku) — 5 kreacyjnych, 5 strukturalnych, 7 behawioralnych. Każdy wzorzec ma osobną sekcję: problem biznesowy, teoria, pełna implementacja, kod kliencki, eksperymenty i pytania kontrolne.
>
> **Jak korzystać:** otwórz IntelliJ, stwórz nowy projekt Maven `wzorce-projektowe` z pakietem bazowym `com.example.wzorce`. Każdy wzorzec wkładaj w osobny podpakiet (np. `com.example.wzorce.singleton`). Uruchamiaj klasy `Main.java` z metodą `main` — każde zadanie ma działający przykład.
>
> **Wzorzec to język, a nie szablon.** Po przejściu tej karty powinieneś **rozpoznawać** wzorce w cudzym kodzie i **mówić** ich nazwami z kolegami z zespołu. Nie wymuszaj wzorca — rozpoznawaj, kiedy problem sam go woła.
---

## Słowniczek skrótów używanych w karcie

Skróty pojawiają się wielokrotnie. Wracaj tu, gdy któryś jest niejasny.

| Skrót | Pełna nazwa | Co to znaczy w skrócie |
| --- | --- | --- |
| **GoF** | Gang of Four | Czterech autorów (Gamma, Helm, Johnson, Vlissides) klasycznej książki *Design Patterns* (1994) |
| **JVM** | Java Virtual Machine | Maszyna wirtualna Javy — środowisko, w którym uruchamia się skompilowany kod |
| **JDK** | Java Development Kit | Pakiet narzędzi dla programistów Javy (kompilator, biblioteki, JVM) |
| **IDE** | Integrated Development Environment | Zintegrowane środowisko programisty (np. IntelliJ, Eclipse) |
| **API** | Application Programming Interface | Zbiór metod/klas, przez które klient komunikuje się z biblioteką lub usługą |
| **POJO** | Plain Old Java Object | „Zwykły obiekt Javy" — bez magicznych adnotacji, bez dziedziczenia po klasach frameworka |
| **NPE** | `NullPointerException` | Wyjątek rzucany, gdy odwołujesz się do `null` |
| **CRUD** | Create, Read, Update, Delete | Cztery podstawowe operacje na danych |
| **DI** | Dependency Injection | „Wstrzykiwanie zależności" — klasa dostaje swoje zależności (repo, serwisy) jako parametry konstruktora, zamiast tworzyć je sama przez `new` |
| **IoC** | Inversion of Control | „Odwrócenie sterowania" — to framework/kontener wywołuje twój kod, a nie twój kod framework. DI jest jednym z rodzajów IoC. |
| **DCL** | Double-Checked Locking | „Podwójnie sprawdzane blokowanie" — technika leniwej inicjalizacji singletona bez kosztu blokady na każdym wywołaniu |
| **SMTP** | Simple Mail Transfer Protocol | Protokół wysyłania emaili |
| **FCM** | Firebase Cloud Messaging | Usługa Google do wysyłania powiadomień push na telefony |
| **HTTP** | Hypertext Transfer Protocol | Protokół, którym przeglądarka rozmawia z serwerem |
| **MVC** | Model-View-Controller | Wzorzec architektoniczny: model danych, widok, kontroler |
| **SQL** | Structured Query Language | Język zapytań do relacyjnych baz danych |
| **JDBC** | Java Database Connectivity | Standardowy interfejs Javy do baz SQL |
| **JPA** | Java Persistence API | Standard mapowania obiektów Javy na tabele bazy (jego najpopularniejszą implementacją jest Hibernate) |
| **ORM** | Object-Relational Mapping | „Mapowanie obiektowo-relacyjne" — automatyczna translacja między obiektami Javy a wierszami SQL |
| **Jakarta EE / Java EE** | Jakarta / Java Enterprise Edition | Zestaw standardów do dużych aplikacji serwerowych w Javie |
| **AOP** | Aspect-Oriented Programming | „Programowanie aspektowe" — dodawanie zachowań (np. logowanie, transakcje) wokół metod *bez* modyfikowania ich kodu |
| **cross-cutting concerns** | „aspekty przekrojowe" | Funkcjonalności potrzebne w wielu klasach naraz (logowanie, cache, autoryzacja) — naturalnie pasują do Proxy/AOP |
| **CGLIB** | Code Generation Library | Biblioteka generująca podklasy w runtime — używana, gdy *dynamic proxy* z JDK nie wystarcza (bo wymaga interfejsu) |
| **SLF4J** | Simple Logging Facade for Java | Fasada nad konkretnymi bibliotekami logowania (Logback, Log4j) |
| **DOM** | Document Object Model | Drzewiasta reprezentacja dokumentu HTML/XML w pamięci |
| **XML / JSON / CSV / PDF** | formaty plików | XML/JSON — formaty danych; CSV — wiersze rozdzielone przecinkami; PDF — dokument |
| **YAGNI** | You Aren't Gonna Need It | „Nie będziesz tego potrzebował" — nie buduj abstrakcji na zapas |
| **OCP / SRP / DIP / LSP / ISP** | zasady SOLID | Pięć zasad projektowania OOP — pełne wytłumaczenie w karcie `solid.md` |

> **Wzmianki o Springu w tej karcie** — Spring to popularny framework, który automatyzuje wiele rzeczy, których uczysz się tu robić ręcznie (głównie DI i Proxy). Każda wzmianka jest oznaczona jako *ciekawostka* i **nie wymaga znajomości frameworka** do zrozumienia wzorca.

---

## Mapa wzorców

| Kategoria | Pytanie | Wzorce w tej karcie |
| --- | --- | --- |
| **Kreacyjne** | „Jak tworzyć obiekty?" | Singleton, Factory Method, Abstract Factory, Builder, Prototype |
| **Strukturalne** | „Jak składać obiekty?" | Adapter, Decorator, Facade, Proxy, Composite |
| **Behawioralne** | „Jak obiekty komunikują się?" | Strategy, Observer, Command, Template Method, Iterator, State, Chain of Responsibility |

**Wzorce, które już znasz, nie wiedząc o tym:**

- `StringBuilder` to **Builder**.
- `Comparator` to **Strategy**.
- `BufferedReader(new FileReader(...))` to **Decorator**.
- `List.of(...)` to **Factory Method**.
- `for-each` to **Iterator**.
- `Runnable` to **Command**.
- *Ciekawostka:* w kontenerach IoC (np. Spring) obiekty oznaczone adnotacjami `@Component`/`@Service` są domyślnie singletonami w obrębie kontenera — czyli to **zarządzany Singleton**. Znajomość Springa **nie jest wymagana** — wracamy do tego w sekcji Singleton.

---


## 1. Wzorzec Singleton

**Cel:** Zagwarantować, że w całej aplikacji istnieje **dokładnie jedna instancja** danej klasy i zapewnić do niej globalny punkt dostępu. Nauczysz się trzech wariantów implementacji (eager, lazy z DCL, enum) i zrozumiesz, **dlaczego enum jest bezpieczniejszy** niż klasyczny Singleton.

**Kategoria:** Kreacyjny

**Kiedy stosować (problem):**
Aplikacja potrzebuje **dokładnie jednego** egzemplarza obiektu trzymającego globalny stan/zasób: konfiguracja wczytana z pliku, pula połączeń do bazy, fabryka loggerów, cache w pamięci. Wiele instancji = niespójny stan, marnowanie zasobów, race conditions przy zapisach do wspólnego pliku/portu. Klasyczny przykład biznesowy: klasa `AppConfig`, która przy starcie wczytuje `application.properties` z dysku — jeśli powstanie 5 razy, 5 razy czytasz plik, możesz dostać 5 różnych snapshotów, a to są pieniądze za I/O.

**Konsekwencje (zalety i wady):**

- Plus: gwarancja jednej instancji w obrębie ClassLoadera **JVM** (maszyny wirtualnej Javy).
- Plus: leniwa inicjalizacja (jeśli wybrana implementacja) odciąża start aplikacji.
- Plus: globalny punkt dostępu — `AppConfig.getInstance()` zamiast wstrzykiwania.
- Minus: ukryta zależność. Klasa, która woła `Logger.getInstance()`, w sygnaturze nie pokazuje, że potrzebuje loggera. Test jednostkowy nie wie, co podmienić.
- Minus: globalny stan = wróg testów jednostkowych. Singleton z mutowalnymi polami zachowuje stan między testami.
- Minus: trudna konkurencja. Mutowalny singleton wymusza synchronizację wszędzie.
- Minus: *ciekawostka, znajomość frameworka nie wymagana* — w aplikacji z **kontenerem DI** (np. Spring) ręczny Singleton to wymyślanie koła na nowo, bo obiekty zarządzane przez kontener są domyślnie singletonami w jego obrębie. Jeśli nie używasz takiego kontenera (jak na tej karcie), klasyczny Singleton z `getInstance()` jest w pełni uzasadniony.

**Teoria w pigułce:**
Klasa Singletona ma **prywatny konstruktor** (nikt z zewnątrz nie wywoła `new`), **statyczne pole z instancją** i **statyczną metodę dostępową** (`getInstance()`). Trzy główne implementacje różnią się momentem inicjalizacji i bezpieczeństwem wątkowym:
1. **Eager** („zachłannie") — instancja powstaje przy ładowaniu klasy. Najprostsza, zawsze thread-safe (JVM gwarantuje).
2. **Lazy z Double-Checked Locking** („leniwie, z podwójnym sprawdzeniem", **DCL**) — instancja powstaje przy pierwszym wywołaniu. Wymaga `volatile`.
3. **Enum** — jedna wartość enuma jest gwarantowanym singletonem. Najbezpieczniejsza (odporna na refleksję i serializację).

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/singleton/AppConfigEager.java`

```java
package com.example.wzorce.singleton;

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

    public String getDbUrl()         { return dbUrl; }
    public int getMaxConnections()   { return maxConnections; }
    public String getAppName()       { return appName; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/singleton/HeavyServiceDCL.java`

```java
package com.example.wzorce.singleton;

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

    private static volatile HeavyServiceDCL instance;

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
```

Utwórz plik: `src/main/java/com/example/wzorce/singleton/DatabasePool.java`

```java
package com.example.wzorce.singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton ENUM — rekomendowany przez Joshua Bloch'a (Effective Java, Item 3).
 *
 * Dlaczego enum jest NAJLEPSZY:
 * 1) Serializacja — deserializacja enuma daje DOKŁADNIE TĘ SAMĄ instancję
 *    (klasyczny Singleton po deserializacji daje DRUGĄ instancję, chyba że nadpiszesz readResolve()).
 * 2) Refleksja — Constructor.newInstance() na enumie rzuca IllegalArgumentException.
 *    Klasyczny Singleton można złamać przez setAccessible(true).
 * 3) Thread-safety — gwarantowana przez JVM, BEZ żadnej dodatkowej logiki.
 * 4) Lazy — instancja powstaje przy pierwszym dotknięciu enuma.
 */
public enum DatabasePool {

    INSTANCE;

    private final List<String> connections = new ArrayList<>();
    private final int poolSize = 5;

    DatabasePool() {
        System.out.println("DatabasePool: otwieram pulę połączeń...");
        for (int i = 0; i < poolSize; i++) {
            connections.add("conn-" + i);
        }
        System.out.println("DatabasePool: pula gotowa (" + poolSize + " połączeń)");
    }

    public String borrow() {
        if (connections.isEmpty()) {
            throw new IllegalStateException("Brak wolnych połączeń");
        }
        return connections.remove(connections.size() - 1);
    }

    public void giveBack(String conn) {
        connections.add(conn);
    }

    public int availableCount() { return connections.size(); }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/singleton/Main.java`

```java
package com.example.wzorce.singleton;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== EAGER ===");
        AppConfigEager c1 = AppConfigEager.getInstance();
        AppConfigEager c2 = AppConfigEager.getInstance();
        System.out.println("c1 == c2? " + (c1 == c2));    // true
        System.out.println("DB URL: " + c1.getDbUrl());

        System.out.println("\n=== LAZY DCL ===");
        HeavyServiceDCL h1 = HeavyServiceDCL.getInstance();   // pierwsze wywołanie inicjalizuje
        HeavyServiceDCL h2 = HeavyServiceDCL.getInstance();   // drugie zwraca istniejące
        System.out.println("h1 == h2? " + (h1 == h2));
        h1.doExpensiveWork();

        System.out.println("\n=== ENUM ===");
        String conn = DatabasePool.INSTANCE.borrow();
        System.out.println("Wziąłem: " + conn);
        System.out.println("Wolnych: " + DatabasePool.INSTANCE.availableCount());
        DatabasePool.INSTANCE.giveBack(conn);
        System.out.println("Po zwrocie: " + DatabasePool.INSTANCE.availableCount());
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj kod, uruchom. Zauważ kolejność komunikatów — `AppConfigEager: konstruktor wywołany` powinno wypaść **przed** linią `=== EAGER ===`? Sprawdź! (Wskazówka: dla EAGER konstruktor odpala się przy pierwszym referencjonowaniu klasy.)
2. Sprawdź czy `c1 == c2` to `true` — to fundament Singletona. Identycznie dla `h1 == h2`.
3. Eksperyment DCL: usuń `volatile` z `HeavyServiceDCL.instance`. Kod się skompiluje, na lokalnym uruchomieniu nic nie wybuchnie. Ale na **produkcji pod obciążeniem** może. Dlaczego? (Wskazówka: instruction reordering, happens-before.)
4. Spróbuj złamać klasyczny Singleton refleksją:
   ```java
   var ctor = AppConfigEager.class.getDeclaredConstructor();
   ctor.setAccessible(true);
   AppConfigEager smuggled = ctor.newInstance();   // tworzymy DRUGĄ instancję!
   System.out.println(AppConfigEager.getInstance() == smuggled);   // false!
   ```
   To samo zrób z `DatabasePool` (enum) — dostaniesz `IllegalArgumentException: Cannot reflectively create enum objects`. Refleksja **nie obroni** klasycznego Singletona — enum tak.
5. Refaktor: dodaj do `AppConfigEager` mutowalne pole `String userTheme` z setterem. Czy nadal jest to bezpieczny Singleton w środowisku wielowątkowym? (Nie — potrzebowałbyś `synchronized` na setterze lub `volatile`.)
6. Sprawdź `Runtime.getRuntime()` z JDK — to klasyczny Singleton (eager). Otwórz źródło w IntelliJ przez `Ctrl+klik`, znajdź pole `private static Runtime currentRuntime = new Runtime();`.

### Pytania kontrolne

1. Dlaczego konstruktor Singletona musi być `private`? Co by się stało, gdyby był `public`?
2. Wytłumacz, dlaczego DCL **wymaga** słowa kluczowego `volatile`. Jaki konkretnie problem (z modelu pamięci) to naprawia?
3. Wymień co najmniej dwa powody, dla których enum jest bezpieczniejszą implementacją Singletona niż klasyczna klasa z polem static.
4. *Pytanie dodatkowe (jeśli znasz Spring — opcjonalne).* Singleton klasyczny (`getInstance()`) vs „bean singleton" w kontenerze DI — gdzie leży różnica? Co jest singletonem „w czym"? Jeśli nie znasz Springa, pomiń.
5. Wymień 3 sytuacje, w których Singleton jest **anty-wzorcem**. (Wskazówka: testy, mutowalny stan, ukryta zależność uniemożliwiająca wstrzykiwanie przez konstruktor.)
6. Kiedy wybrałbyś EAGER, a kiedy LAZY z DCL? Podaj przykład biznesowy dla każdego.
7. Klasa `AppConfigEager` ma instancję tworzoną przy ładowaniu klasy. Jeśli klasa nigdy nie zostanie zreferencjonowana w aplikacji, czy konstruktor zostanie wywołany?

---

## 2. Wzorzec Factory Method

**Cel:** Oddzielić **klienta** od konkretnej klasy obiektu, którą tworzy. Klient wywołuje `Factory.create("X")` i dostaje obiekt implementujący wspólny interfejs — bez znajomości konkretnej klasy. Nauczysz się dwóch wariantów: statycznej metody fabrykującej oraz polimorficznej fabryki z dziedziczeniem.

**Kategoria:** Kreacyjny

**Kiedy stosować (problem):**
W kodzie biznesowym pojawia się drabinka if-else / switch tworząca obiekty na podstawie typu wejścia:

```java
Notification n;
if (type.equals("EMAIL"))      n = new EmailNotification(...);
else if (type.equals("SMS"))   n = new SmsNotification(...);
else if (type.equals("PUSH"))  n = new PushNotification(...);
```

Każdy nowy typ powiadomienia wymaga modyfikacji wszystkich miejsc, gdzie ta drabinka się powtarza. Naruszenie OCP, ryzyko literówki, trudne testowanie. Sygnał, że potrzebujesz **Factory Method**: kilka miejsc w kodzie konstruuje obiekty wybranego typu na podstawie danych runtime'owych (string z konfiguracji, typ z bazy, parametr z requesta).

**Konsekwencje (zalety i wady):**

- Plus: klient zna tylko interfejs, nie konkretne klasy. Łatwa rozbudowa.
- Plus: jedno centralne miejsce decyzji „jaki obiekt utworzyć".
- Plus: wprowadzanie nowego typu wymaga tylko dodania nowej klasy + jednej linii w fabryce.
- Plus: fabryka może zwrócić **cached** obiekt zamiast `new` (np. `Integer.valueOf` cachuje wartości -128..127).
- Minus: każdy nowy typ wymaga edycji fabryki (chyba że używasz rejestru — patrz dalej).
- Minus: ukrywa `new` — debugger pokazuje fabrykę, nie konkretną klasę.
- Minus: nadużywany — dla 2 typów to overkill, użyj zwykłego konstruktora.

**Teoria w pigułce:**
Fabryka to klasa lub statyczna metoda, której jedynym zadaniem jest **decydowanie i tworzenie**. Klient: `Notification n = NotificationFactory.create("EMAIL", ...)`. Fabryka zwraca **interfejs** (`Notification`), nie konkretną klasę — to fundament. Wariant „Factory Method" Gang of Four mówi o fabryce w postaci metody w klasie bazowej, nadpisywanej przez podklasy. W praktyce w Javie 90% przypadków to **statyczna metoda fabrykująca** (`List.of`, `Optional.of`, `Path.of`).

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/factorymethod/Notification.java`

```java
package com.example.wzorce.factorymethod;

/** Wspólny interfejs — klient zna TYLKO ten typ. */
public interface Notification {
    void send(String to, String message);
    String channel();
}
```

Utwórz plik: `src/main/java/com/example/wzorce/factorymethod/EmailNotification.java`

```java
package com.example.wzorce.factorymethod;

public class EmailNotification implements Notification {
    private final String fromAddress;

    public EmailNotification(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    @Override
    public void send(String to, String message) {
        System.out.printf("EMAIL z %s do %s: %s%n", fromAddress, to, message);
        // tu: prawdziwy klient SMTP (protokół wysyłania emaili), np. biblioteka JavaMail
    }

    @Override public String channel() { return "EMAIL"; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/factorymethod/SmsNotification.java`

```java
package com.example.wzorce.factorymethod;

public class SmsNotification implements Notification {
    private final String gatewayUrl;

    public SmsNotification(String gatewayUrl) {
        this.gatewayUrl = gatewayUrl;
    }

    @Override
    public void send(String to, String message) {
        System.out.printf("SMS przez %s do %s: %s%n", gatewayUrl, to, message);
    }

    @Override public String channel() { return "SMS"; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/factorymethod/PushNotification.java`

```java
package com.example.wzorce.factorymethod;

public class PushNotification implements Notification {
    private final String firebaseKey;

    public PushNotification(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    @Override
    public void send(String to, String message) {
        // FCM = Firebase Cloud Messaging, usługa Google do wysyłania powiadomień push na telefony
        System.out.printf("PUSH (FCM %s) do %s: %s%n",
                firebaseKey.substring(0, 4) + "...", to, message);
    }

    @Override public String channel() { return "PUSH"; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/factorymethod/NotificationFactory.java`

```java
package com.example.wzorce.factorymethod;

/**
 * Statyczna fabryka — odpowiada na pytanie „jaki obiekt utworzyć?".
 * Klient zna tylko interfejs Notification, nie konkretne klasy.
 */
public class NotificationFactory {

    // Konfiguracja — w realnej aplikacji wczytana z pliku properties
    private static final String EMAIL_FROM   = "noreply@fefe2022.com";
    private static final String SMS_GATEWAY  = "https://sms.example.com/api";
    private static final String FCM_KEY      = "FCM-PROD-KEY-12345";

    private NotificationFactory() {
        // Klasa pomocnicza — nikt nie powinien jej instancjonować
    }

    public static Notification create(String type) {
        if (type == null) {
            throw new IllegalArgumentException("Typ nie może być null");
        }
        return switch (type.toUpperCase()) {
            case "EMAIL" -> new EmailNotification(EMAIL_FROM);
            case "SMS"   -> new SmsNotification(SMS_GATEWAY);
            case "PUSH"  -> new PushNotification(FCM_KEY);
            default      -> throw new IllegalArgumentException(
                    "Nieznany typ powiadomienia: " + type);
        };
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/factorymethod/Main.java`

```java
package com.example.wzorce.factorymethod;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Klient zna TYLKO interfejs Notification i fabrykę
        List<String> typy = List.of("EMAIL", "SMS", "PUSH");

        for (String typ : typy) {
            Notification n = NotificationFactory.create(typ);
            System.out.println("Utworzono kanał: " + n.channel());
            n.send("klient@firma.pl", "Twoje zamówienie zostało przyjęte");
        }

        // Symulacja błędu
        try {
            NotificationFactory.create("FAX");
        } catch (IllegalArgumentException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj kod, uruchom. Powinieneś zobaczyć 3 wysłane powiadomienia + komunikat o błędzie dla `FAX`.
2. Zauważ: w `Main` nie ma ani jednego `new EmailNotification(...)`. Klient nie wie, że istnieje klasa `EmailNotification` — zna tylko interfejs i fabrykę.
3. Dodaj czwarty typ — `SlackNotification` (kanał `"SLACK"`). Co musisz zmienić, a co zostaje bez zmian?
4. Refaktor: zamień drabinkę `switch` w fabryce na `Map<String, Supplier<Notification>>` (rejestr fabryk):
   ```java
   private static final Map<String, Supplier<Notification>> REGISTRY = Map.of(
           "EMAIL", () -> new EmailNotification(EMAIL_FROM),
           "SMS",   () -> new SmsNotification(SMS_GATEWAY),
           "PUSH",  () -> new PushNotification(FCM_KEY)
   );
   public static Notification create(String type) {
       var supplier = REGISTRY.get(type.toUpperCase());
       if (supplier == null) throw new IllegalArgumentException("Nieznany: " + type);
       return supplier.get();
   }
   ```
   Dodaj możliwość `register(String type, Supplier<Notification> supplier)` — teraz fabryka jest **rozszerzalna w runtime** bez modyfikacji jej kodu (Open/Closed).
5. Sprawdź w JDK: `List.of(1, 2, 3)`, `Optional.of("x")`, `Integer.valueOf(42)`, `Path.of("/tmp")`. Wszystkie to statyczne metody fabrykujące. Otwórz źródło `Integer.valueOf(int)` — zobaczysz cache wartości -128..127.
6. Anti-pattern check: czy fabryka, która zwraca konkretną klasę (`EmailNotification create(...)`) zamiast interfejsu, ma sens? (Wskazówka: nie. To zwykła metoda, nie wzorzec Factory.)

### Pytania kontrolne

1. Dlaczego fabryka **musi** zwracać interfejs/abstrakcyjny typ, a nie konkretną klasę?
2. Czym różni się Factory Method od bezpośredniego użycia konstruktora `new`?
3. Wymień 3 statyczne metody fabrykujące z JDK i powiedz, jakie korzyści daje ich istnienie zamiast `new`.
4. Co to znaczy, że fabryka może być „rozszerzalna w runtime"? Jak to osiągnąć (rejestr fabryk)?
5. Kiedy Factory Method jest **przesadą**? (Wskazówka: dla 1-2 typów bez wariantowości.)
6. Co jest lepsze: `Integer.valueOf(42)` czy `new Integer(42)`? Dlaczego (JDK to celowo deprecuje)?

---

## 3. Wzorzec Abstract Factory

**Cel:** Tworzyć **rodziny powiązanych obiektów** bez ujawniania klientowi konkretnych klas. Nauczysz się modelować scenariusz biznesowy „silnik raportów" (PDF, HTML, CSV), gdzie każdy silnik produkuje spójny zestaw obiektów: nagłówek, treść, stopkę.

**Kategoria:** Kreacyjny

**Kiedy stosować (problem):**
Twoja aplikacja generuje raporty w wielu formatach (PDF, HTML, CSV). Każdy format wymaga konsekwentnie używanych komponentów: nagłówka, tabeli, stopki. Mieszanie komponentów z różnych rodzin (np. `PdfHeader` z `HtmlTable`) prowadzi do błędów wizualnych i renderingowych. Naturalna potrzeba: gdy wybierasz format „PDF", **wszystkie** komponenty mają być spójnie PDF. Klasyczny przykład: motywy UI (Light/Dark), **drivery JDBC** — JDBC to standardowy interfejs Javy do baz SQL; każda baza (MySQL, PostgreSQL) ma własny driver, który dostarcza spójną rodzinę klas `Connection`, `Statement`, `PreparedStatement`.

**Konsekwencje (zalety i wady):**

- Plus: gwarancja spójności rodziny obiektów.
- Plus: klient operuje na interfejsach — łatwa zmiana całej rodziny przez podmianę jednej fabryki.
- Plus: izoluje konkretne klasy od klienta.
- Minus: dodanie **nowego komponentu** do rodziny (np. „logo") wymaga edycji **wszystkich** fabryk.
- Minus: złożone, gdy mamy 2-3 komponenty w rodzinie — wtedy Factory Method wystarczy.
- Minus: wiele klas — łatwo o eksplozję liczby plików.

**Teoria w pigułce:**
Abstract Factory to interfejs definiujący metody fabrykujące dla **kilku powiązanych** typów. Konkretne fabryki implementują ten interfejs i zwracają obiekty z jednej **rodziny**. Klient zna tylko interfejs fabryki — nie wie, czy używa `PdfReportFactory` czy `HtmlReportFactory`.

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/Header.java`

```java
package com.example.wzorce.abstractfactory;

public interface Header { String render(String title); }
```

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/Body.java`

```java
package com.example.wzorce.abstractfactory;

import java.util.List;

public interface Body { String render(List<String> rows); }
```

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/Footer.java`

```java
package com.example.wzorce.abstractfactory;

public interface Footer { String render(String author); }
```

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/ReportFactory.java`

```java
package com.example.wzorce.abstractfactory;

/**
 * Abstract Factory — interfejs fabrykujący CAŁĄ RODZINĘ obiektów.
 * Każda konkretna implementacja produkuje spójny zestaw (PDF / HTML / CSV).
 */
public interface ReportFactory {
    Header createHeader();
    Body   createBody();
    Footer createFooter();
}
```

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/pdf/PdfHeader.java`

```java
package com.example.wzorce.abstractfactory.pdf;

import com.example.wzorce.abstractfactory.Header;

public class PdfHeader implements Header {
    @Override public String render(String title) {
        return "[PDF] <<<TITLE: " + title + ">>>";
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/pdf/PdfBody.java`

```java
package com.example.wzorce.abstractfactory.pdf;

import com.example.wzorce.abstractfactory.Body;
import java.util.List;

public class PdfBody implements Body {
    @Override public String render(List<String> rows) {
        StringBuilder sb = new StringBuilder("[PDF] Tabela:\n");
        for (String r : rows) sb.append("  | ").append(r).append(" |\n");
        return sb.toString();
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/pdf/PdfFooter.java`

```java
package com.example.wzorce.abstractfactory.pdf;

import com.example.wzorce.abstractfactory.Footer;

public class PdfFooter implements Footer {
    @Override public String render(String author) {
        return "[PDF] Strona 1/1 — wygenerował: " + author;
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/pdf/PdfReportFactory.java`

```java
package com.example.wzorce.abstractfactory.pdf;

import com.example.wzorce.abstractfactory.*;

public class PdfReportFactory implements ReportFactory {
    @Override public Header createHeader() { return new PdfHeader(); }
    @Override public Body   createBody()   { return new PdfBody(); }
    @Override public Footer createFooter() { return new PdfFooter(); }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/html/HtmlHeader.java`

```java
package com.example.wzorce.abstractfactory.html;

import com.example.wzorce.abstractfactory.Header;

public class HtmlHeader implements Header {
    @Override public String render(String title) {
        return "<h1>" + title + "</h1>";
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/html/HtmlBody.java`

```java
package com.example.wzorce.abstractfactory.html;

import com.example.wzorce.abstractfactory.Body;
import java.util.List;

public class HtmlBody implements Body {
    @Override public String render(List<String> rows) {
        StringBuilder sb = new StringBuilder("<table>\n");
        for (String r : rows) sb.append("  <tr><td>").append(r).append("</td></tr>\n");
        sb.append("</table>");
        return sb.toString();
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/html/HtmlFooter.java`

```java
package com.example.wzorce.abstractfactory.html;

import com.example.wzorce.abstractfactory.Footer;

public class HtmlFooter implements Footer {
    @Override public String render(String author) {
        return "<footer>autor: " + author + "</footer>";
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/html/HtmlReportFactory.java`

```java
package com.example.wzorce.abstractfactory.html;

import com.example.wzorce.abstractfactory.*;

public class HtmlReportFactory implements ReportFactory {
    @Override public Header createHeader() { return new HtmlHeader(); }
    @Override public Body   createBody()   { return new HtmlBody(); }
    @Override public Footer createFooter() { return new HtmlFooter(); }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/ReportPrinter.java`

```java
package com.example.wzorce.abstractfactory;

import java.util.List;

/**
 * Klient — operuje TYLKO na interfejsach.
 * Nie wie, czy fabryka produkuje PDF czy HTML.
 */
public class ReportPrinter {
    private final ReportFactory factory;

    public ReportPrinter(ReportFactory factory) { this.factory = factory; }

    public String build(String title, List<String> rows, String author) {
        return factory.createHeader().render(title)
                + "\n" + factory.createBody().render(rows)
                + "\n" + factory.createFooter().render(author);
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/abstractfactory/Main.java`

```java
package com.example.wzorce.abstractfactory;

import com.example.wzorce.abstractfactory.pdf.PdfReportFactory;
import com.example.wzorce.abstractfactory.html.HtmlReportFactory;
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
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj wszystkie pliki, uruchom. Powinieneś zobaczyć dwa raporty — w PDF-styled (`[PDF]`) i HTML.
2. Zauważ — `ReportPrinter` nie ma ANI jednego `new PdfXxx()` czy `new HtmlXxx()`. Zna tylko `ReportFactory`. To kluczowa właściwość.
3. Dodaj trzecią rodzinę: `csv/CsvReportFactory.java` + `CsvHeader`, `CsvBody`, `CsvFooter`. Header: `# title`, body: wiersze rozdzielone przecinkami, footer: `# author`. Sprawdź, że `ReportPrinter` **nie wymaga zmiany**.
4. Sprawdź eksplozję klas: ile klas musiałbyś dodać, żeby dodać czwarty komponent rodziny (np. `Logo`)? (Odpowiedź: 1 nowy interfejs + 3 implementacje + 3 modyfikacje fabryk + 1 wywołanie w `ReportPrinter`. To główna wada wzorca.)
5. Anti-pattern check: jeśli twoja „abstract factory" produkuje tylko **jeden** typ obiektu — to nie Abstract Factory, tylko zwykły Factory Method. Cofnij refaktor.
6. Porównanie z JDK: zobacz `DocumentBuilderFactory` (XML), `javax.sql.DataSource` (driver bazy) — klasyczne Abstract Factory.

### Pytania kontrolne

1. Czym różni się Abstract Factory od Factory Method?
2. Co to znaczy „rodzina obiektów" w Abstract Factory? Podaj przykład z UI lub bazą danych.
3. Co się stanie, jeśli klient sam stworzy `new PdfHeader()` i połączy go z `HtmlBody()`? Dlaczego Abstract Factory tego nie pozwala?
4. Dlaczego dodanie nowego komponentu (np. „logo") do rodziny w Abstract Factory jest droższe niż dodanie nowej rodziny?
5. Wymień przykład Abstract Factory z JDK lub bibliotek Jakarta EE (zestaw standardów do dużych aplikacji serwerowych w Javie — *znajomość Jakarta EE nie jest wymagana, wystarczy przykład z JDK*).
6. Jaki jest podstawowy „test" sensu Abstract Factory — czy potrzebujesz tworzyć **zestaw spójnych** obiektów?

---

## 4. Wzorzec Builder

**Cel:** Budować złożone, niemutowalne obiekty krok po kroku, z czytelnym fluent API, walidacją w jednym miejscu i wartościami domyślnymi. Nauczysz się dwóch wariantów: ręczny Builder oraz nowoczesny `record` z `Builder` (lub bez — pokażemy granicę użyteczności).

**Kategoria:** Kreacyjny

**Kiedy stosować (problem):**
Twoja klasa biznesowa ma 8 pól — 3 wymagane, 5 opcjonalnych. Klasyczne podejścia bolą:
- **Konstruktor z 8 argumentami** — `new Order(null, null, 25, false, null, "PL", true, null)` — co znaczy każdy null? Łatwo pomylić kolejność dwóch String'ów obok siebie.
- **Teleskopujące konstruktory** — `new Order(a)`, `new Order(a, b)`, `new Order(a, b, c)` — kilkanaście overloadów.
- **Settery** — łamie niemutowalność, obiekt w połowicznym stanie podczas konstrukcji.

Biznesowy przykład: budowanie zapytania SQL (`SELECT * FROM users WHERE active = true AND age > 18 ORDER BY name LIMIT 10`) — typowy SQL Query Builder. Albo `HttpRequest` z Java 11 — `HttpRequest.newBuilder().uri(...).header(...).POST(...).build()`.

**Konsekwencje (zalety i wady):**

- Plus: czytelność — każde wywołanie jest nazwane (`.from("a@b").subject("Test")`).
- Plus: niemutowalność wyniku — final fields w klasie target.
- Plus: walidacja w jednym miejscu (`build()`), nie w 8 setterach.
- Plus: sensowne wartości domyślne dla pól opcjonalnych.
- Plus: idealny do testów — buduj obiekt-fixture z różnymi wariantami.
- Minus: dużo kodu — Builder często ma 2× więcej kodu niż target. Dlatego Lombok `@Builder`.
- Minus: zapomnienie wymaganego pola wyjdzie dopiero w runtime (w `build()`) — kompilator nie pomoże.
- Minus: dla 2-3 pól to overkill. Konstruktor wystarczy.

**Teoria w pigułce:**
Builder to wewnętrzna (lub osobna) klasa z **fluent setterami** (każdy zwraca `this`), prywatnym konstruktorem klasy docelowej i metodą `build()` która: (1) waliduje pola, (2) tworzy obiekt finalny. Klasa docelowa ma `private` konstruktor — TYLKO Builder ją tworzy. Dzięki temu obiekt po stworzeniu jest **niezmienny**, a wszystkie pola wymagane są zapewnione.

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/builder/Email.java`

```java
package com.example.wzorce.builder;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Klasyczny ręczny Builder.
 * - prywatny konstruktor → tylko Builder może utworzyć Email
 * - final fields → niemutowalność
 * - walidacja w konstruktorze (wywoływana z build())
 */
public class Email {
    private final String from;
    private final String to;
    private final String subject;
    private final String body;
    private final String cc;
    private final boolean html;
    private final List<String> attachments;

    private Email(Builder b) {
        // Wymagane — walidacja przez requireNonNull
        this.from    = Objects.requireNonNull(b.from,    "from jest wymagane");
        this.to      = Objects.requireNonNull(b.to,      "to jest wymagane");
        this.subject = Objects.requireNonNull(b.subject, "subject jest wymagane");

        // Opcjonalne — sensowne wartości domyślne
        this.body = b.body != null ? b.body : "";
        this.cc   = b.cc;
        this.html = b.html;

        // Defensywna kopia — zewnętrzna mutacja listy nie wpłynie na Email
        this.attachments = b.attachments != null
                ? List.copyOf(b.attachments)
                : List.of();
    }

    public String  getFrom()             { return from; }
    public String  getTo()               { return to; }
    public String  getSubject()          { return subject; }
    public String  getBody()             { return body; }
    public String  getCc()               { return cc; }
    public boolean isHtml()              { return html; }
    public List<String> getAttachments() { return attachments; }

    @Override public String toString() {
        return "Email{from=%s, to=%s, subject=%s, html=%s, attachments=%d}"
                .formatted(from, to, subject, html, attachments.size());
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String from;
        private String to;
        private String subject;
        private String body;
        private String cc;
        private boolean html = false;
        private List<String> attachments;

        public Builder from(String from)       { this.from = from;       return this; }
        public Builder to(String to)           { this.to = to;           return this; }
        public Builder subject(String subject) { this.subject = subject; return this; }
        public Builder body(String body)       { this.body = body;       return this; }
        public Builder cc(String cc)           { this.cc = cc;           return this; }
        public Builder html()                  { this.html = true;       return this; }

        public Builder attach(String path) {
            if (attachments == null) attachments = new ArrayList<>();
            attachments.add(path);
            return this;
        }

        public Email build() {
            return new Email(this);   // walidacja w konstruktorze
        }
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/builder/SqlQueryBuilder.java`

```java
package com.example.wzorce.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Drugi przykład Builder — generator SQL.
 * Pokazuje, że Builder jest naturalny dla obiektów,
 * które buduje się stopniowo z wielu opcjonalnych fragmentów.
 */
public class SqlQueryBuilder {

    private String table;
    private final List<String> columns = new ArrayList<>();
    private final List<String> wheres  = new ArrayList<>();
    private String orderBy;
    private Integer limit;

    public SqlQueryBuilder select(String... cols) {
        for (String c : cols) columns.add(c);
        return this;
    }

    public SqlQueryBuilder from(String table) {
        this.table = table;
        return this;
    }

    public SqlQueryBuilder where(String condition) {
        wheres.add(condition);
        return this;
    }

    public SqlQueryBuilder orderBy(String column) {
        this.orderBy = column;
        return this;
    }

    public SqlQueryBuilder limit(int n) {
        if (n <= 0) throw new IllegalArgumentException("limit > 0");
        this.limit = n;
        return this;
    }

    public String build() {
        if (table == null) {
            throw new IllegalStateException("Brak FROM — wywołaj from(...)");
        }
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(columns.isEmpty() ? "*" : String.join(", ", columns));
        sb.append(" FROM ").append(table);
        if (!wheres.isEmpty()) {
            sb.append(" WHERE ").append(String.join(" AND ", wheres));
        }
        if (orderBy != null) sb.append(" ORDER BY ").append(orderBy);
        if (limit   != null) sb.append(" LIMIT ").append(limit);
        return sb.toString();
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/builder/OrderRecord.java`

```java
package com.example.wzorce.builder;

import java.util.List;
import java.util.Objects;

/**
 * Nowoczesna alternatywa — record z STATYCZNĄ wewnętrzną klasą Builder.
 * Record sam w sobie ZASTĘPUJE prostą klasę POJO (Plain Old Java Object — „zwykły obiekt Javy") — getter, equals, hashCode, toString
 * są wygenerowane automatycznie. Builder pozostaje dla wygody konstrukcji.
 *
 * Kiedy record + Builder? Gdy:
 *  - chcesz niemutowalność i prostotę record'a
 *  - ALE masz wiele pól opcjonalnych
 *  - i nie chcesz konstruktora z 7 argumentami
 */
public record OrderRecord(String customerId, List<String> items,
                          double total, String coupon, boolean express) {

    // Compact constructor — walidacja przy tworzeniu
    public OrderRecord {
        Objects.requireNonNull(customerId, "customerId");
        Objects.requireNonNull(items,      "items");
        if (total < 0) throw new IllegalArgumentException("total >= 0");
        items = List.copyOf(items);    // defensywna kopia
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String customerId;
        private List<String> items = List.of();
        private double total;
        private String coupon;
        private boolean express = false;

        public Builder customerId(String id)         { this.customerId = id; return this; }
        public Builder items(List<String> i)         { this.items = i; return this; }
        public Builder total(double t)               { this.total = t; return this; }
        public Builder coupon(String c)              { this.coupon = c; return this; }
        public Builder express()                     { this.express = true; return this; }

        public OrderRecord build() {
            return new OrderRecord(customerId, items, total, coupon, express);
        }
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/builder/Main.java`

```java
package com.example.wzorce.builder;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // 1) Email Builder — wiele opcjonalnych pól
        Email mail = Email.builder()
                .from("nadawca@firma.pl")
                .to("odbiorca@klient.pl")
                .subject("Raport miesięczny")
                .body("<h1>Raport za styczeń</h1>")
                .cc("kopia@firma.pl")
                .html()
                .attach("/tmp/raport.pdf")
                .attach("/tmp/zestawienie.xlsx")
                .build();
        System.out.println(mail);

        // 2) SQL Query Builder — fragmentaryczna konstrukcja
        String sql = new SqlQueryBuilder()
                .select("id", "name", "email")
                .from("users")
                .where("active = true")
                .where("age >= 18")
                .orderBy("name")
                .limit(50)
                .build();
        System.out.println("SQL: " + sql);

        // 3) Record + Builder
        OrderRecord order = OrderRecord.builder()
                .customerId("CUST-42")
                .items(List.of("Laptop", "Mysz", "Mata"))
                .total(4299.50)
                .coupon("BLACKFRIDAY")
                .express()
                .build();
        System.out.println(order);

        // 4) Demonstracja błędu — brak wymaganego pola
        try {
            Email.builder().subject("Tylko temat").build();
        } catch (NullPointerException e) {
            System.out.println("Złapano NPE (NullPointerException): " + e.getMessage());
        }

        // 5) Demonstracja błędu — SQL bez FROM
        try {
            new SqlQueryBuilder().select("*").build();
        } catch (IllegalStateException e) {
            System.out.println("Złapano stan: " + e.getMessage());
        }
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj, uruchom. Trzy obiekty zbudowane, dwa błędy złapane.
2. Spróbuj zmienić Email po zbudowaniu: `mail.setSubject(...)` — nie ma settera, kompilator zaprotestuje. To celowe — Builder = obiekt niemutowalny.
3. Eksperyment: w `Email.Builder` zmień jedną metodę na `void` (np. `public void from(String from) { this.from = from; }`). Co się stanie z łańcuchem `.from(...).to(...).build()`? (Wskazówka: void nie ma metody to().)
4. Anty-pattern: rozsiana walidacja. Spróbuj dodać `if (email.isBlank())` w setterze `from()`. Co się stanie, jeśli klient woła `from("")` ZANIM wywoła inne settery? (Walidacja powinna być w `build()`, nie w setterach.)
5. Refaktor: spróbuj zastąpić `Email` rekordem `record Email(...)` z 7 polami. Czy klient zauważy różnicę w API? (`new Email.Builder()` musi zostać. Pole `html` wciąż boolean. Builder konstruuje record zamiast klasy.)
6. Porównanie z Lombok `@Builder`: zaimportuj Lombok do projektu, dodaj `@Builder` do klasy. IDE wygeneruje analogiczne API, mniej kodu. Wadą — magic, mniej kontroli nad walidacją.
7. Zobacz `HttpClient.newBuilder().connectTimeout(...).version(...).build()` w JDK 11+ — klasyczny Builder w bibliotece standardowej.

### Pytania kontrolne

1. Dlaczego konstruktor klasy docelowej (`Email`) jest `private`?
2. Czemu walidacja **musi** być w `build()` (lub w konstruktorze klasy docelowej), a nie w setterach Buildera?
3. Wymień dwie kategorie pól w Builderze: wymagane i opcjonalne. Jak każda jest obsługiwana?
4. Co znaczy „defensywna kopia" listy `attachments` i dlaczego jest potrzebna?
5. Dlaczego każdy setter Buildera zwraca `this`? Co się stanie, gdy zwróci `void`?
6. Kiedy preferować Lombok `@Builder` nad ręczną implementacją, a kiedy odwrotnie?
7. Wymień 2 Buildery z JDK lub bibliotek (`StringBuilder`, `HttpClient`, ...).
8. Czy record może mieć Builder? Co Builder dodaje do record'a, którego record sam nie ma?

---

## 5. Wzorzec Prototype

**Cel:** Tworzyć nowe obiekty przez **kopiowanie** istniejących (klonowanie), zamiast budować je od zera. Nauczysz się różnicy między klonowaniem płytkim a głębokim oraz dlaczego `Cloneable` z JDK jest źle zaprojektowany i preferuje się ręczne **copy constructors**.

**Kategoria:** Kreacyjny

**Kiedy stosować (problem):**
Masz drogi w stworzeniu obiekt (parsowanie XML, query do bazy, deserializacja), z którego chcesz tworzyć **warianty**. Tworzenie od zera kosztowne. Drugi scenariusz: obiekt jest niemutowalny w runtime, ale potrzebujesz „kopii z modyfikacją" (immutable update — czyli „aktualizacja przez stworzenie kopii ze zmienionym polem", bo oryginał jest niezmienny). *Ciekawostka, znajomość frameworka nie wymagana:* w Springu istnieje tzw. zakres `prototype`, który dla każdego żądania tworzy nową kopię obiektu — to dosłownie ten wzorzec w wydaniu kontenera DI.

Biznesowy przykład: szablon dokumentu (umowa) z domyślnym tekstem, gdzie każdy klient dostaje kopię z podmienionymi danymi. Drugi: niemutowalna konfiguracja serwera, w testach chcesz wariant z 1 polem zmienionym.

**Konsekwencje (zalety i wady):**

- Plus: szybkie tworzenie wariantów obiektów z prototypu.
- Plus: kopiuje aktualny **stan**, nie tylko strukturę.
- Plus: alternatywa dla licznych konstruktorów / fabryk dla każdej kombinacji.
- Minus: klonowanie płytkie vs głębokie — łatwo o pułapkę (zmiana w kopii wpływa na oryginał).
- Minus: `Cloneable` w JDK jest sporną decyzją projektową (Bloch: „Cloneable to broken").
- Minus: trudne dla obiektów z final fields, polami z cyklicznymi referencjami.

**Teoria w pigułce:**
Klient nie tworzy obiektu przez `new`, tylko prosi prototyp o `clone()`. Wynikowy obiekt jest **nową instancją** z tym samym stanem. Dwa warianty kopii:
- **Płytka kopia (shallow)** — pola prymitywne kopiowane bezpośrednio, pola obiektowe (referencje) wskazują na te same obiekty co oryginał.
- **Głęboka kopia (deep)** — rekurencyjnie klonujesz także zagnieżdżone obiekty.

W praktyce: w Javie polecaną drogą jest **copy constructor** lub statyczna metoda `copyOf`, nie `Cloneable`/`clone()` z JDK (które jest zaszyte w Object i ma dziwny kontrakt).

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/prototype/DocumentTemplate.java`

```java
package com.example.wzorce.prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasyczny Prototype z metodą copy() (czyli "copy constructor" jako statyczna metoda).
 * Klonowanie GŁĘBOKIE — sygnatariusze są nową listą.
 */
public class DocumentTemplate {

    private String title;
    private String body;
    private String author;
    private List<String> signatories;

    public DocumentTemplate(String title, String body, String author,
                            List<String> signatories) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.signatories = new ArrayList<>(signatories);
    }

    /** Copy constructor — głęboka kopia. */
    public DocumentTemplate(DocumentTemplate other) {
        this.title  = other.title;
        this.body   = other.body;
        this.author = other.author;
        this.signatories = new ArrayList<>(other.signatories);   // głęboka kopia listy
    }

    /** Operacja prototypu — zwraca nową kopię z aktualnego stanu. */
    public DocumentTemplate copy() {
        return new DocumentTemplate(this);
    }

    // Settery — Prototype to wzorzec o klonowaniu, klasa może być mutowalna
    public void setTitle(String title)      { this.title = title; }
    public void setBody(String body)        { this.body = body; }
    public void setAuthor(String author)    { this.author = author; }
    public void addSignatory(String name)   { this.signatories.add(name); }

    public String getTitle()                { return title; }
    public String getBody()                 { return body; }
    public String getAuthor()               { return author; }
    public List<String> getSignatories()    { return signatories; }

    @Override public String toString() {
        return "Doc{title=%s, author=%s, sig=%s}".formatted(title, author, signatories);
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/prototype/ServerConfig.java`

```java
package com.example.wzorce.prototype;

import java.util.Map;
import java.util.HashMap;

/**
 * Drugi przykład — Prototype z metodą withXxx().
 * Klasa niemutowalna — każde wywołanie withXxx zwraca NOWĄ instancję.
 * To wzorzec znany z record'ów (Java 14+).
 */
public class ServerConfig {

    private final String host;
    private final int port;
    private final Map<String, String> headers;

    public ServerConfig(String host, int port, Map<String, String> headers) {
        this.host = host;
        this.port = port;
        this.headers = Map.copyOf(headers);
    }

    public ServerConfig withHost(String host) {
        return new ServerConfig(host, this.port, this.headers);
    }

    public ServerConfig withPort(int port) {
        return new ServerConfig(this.host, port, this.headers);
    }

    public ServerConfig withHeader(String name, String value) {
        Map<String, String> merged = new HashMap<>(this.headers);
        merged.put(name, value);
        return new ServerConfig(this.host, this.port, merged);
    }

    public String getHost()                       { return host; }
    public int getPort()                          { return port; }
    public Map<String, String> getHeaders()       { return headers; }

    @Override public String toString() {
        return "ServerConfig{host=%s, port=%d, headers=%s}".formatted(host, port, headers);
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/prototype/Main.java`

```java
package com.example.wzorce.prototype;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        // 1) Mutowalny prototyp — szablon umowy
        DocumentTemplate template = new DocumentTemplate(
                "Umowa o pracę",
                "TREŚĆ UMOWY...",
                "Zarząd",
                List.of("Pracodawca"));

        // Wariant 1 — Anna
        DocumentTemplate anna = template.copy();
        anna.setTitle("Umowa o pracę — Anna Kowalska");
        anna.addSignatory("Anna Kowalska");
        System.out.println("Oryginał: " + template);
        System.out.println("Anna:    " + anna);

        // Wariant 2 — Jan, niezależny od Anny
        DocumentTemplate jan = template.copy();
        jan.setTitle("Umowa o pracę — Jan Nowak");
        jan.addSignatory("Jan Nowak");
        System.out.println("Jan:     " + jan);
        System.out.println("Oryginał wciąż czysty: " + template);

        // 2) Niemutowalny prototyp — konfiguracja serwera
        ServerConfig base = new ServerConfig(
                "localhost", 8080, Map.of("Content-Type", "application/json"));

        ServerConfig prod = base.withHost("prod.example.com")
                                .withPort(443)
                                .withHeader("X-Env", "prod");

        ServerConfig test = base.withHost("test.example.com")
                                .withHeader("X-Env", "test");

        System.out.println("\nbase: " + base);
        System.out.println("prod: " + prod);
        System.out.println("test: " + test);
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj, uruchom. `template` powinien pozostać czysty (`[Pracodawca]`), Anna i Jan mają każdy swojego sygnatariusza.
2. Eksperyment z płytką kopią — zmień w `DocumentTemplate.copy()` linię `this.signatories = new ArrayList<>(other.signatories);` na `this.signatories = other.signatories;`. Uruchom — co się stanie z `template.signatories`, gdy Anna doda swojego sygnatariusza? (Wszystkie trzy listy będą `[Pracodawca, Anna Kowalska, Jan Nowak]` — to płytka kopia.)
3. Przywróć głęboką kopię. Sprawdź ponownie — `template` znowu czysty.
4. Niemutowalny ServerConfig: zauważ, że `base` po `withHost/withPort` pozostaje niezmieniony. To wzorzec znany z record'ów (Java 14+) — record nie ma `withXxx`, ale ma `toBuilder()` jeśli używasz Lombok `@With`.
5. Anti-pattern: nie używaj `Object.clone()` z JDK. Sprawdź dlaczego — w IntelliJ otwórz `Object.clone()`, przeczytaj javadoc. Kontrakt jest oparty na konwencjach, nie typach. Bloch w Effective Java rekomenduje `copy constructor`.
6. Refaktor: spróbuj zaimplementować `DocumentTemplate implements Cloneable` z `@Override public Object clone()`. Zauważ ile boilerplate'u (cast, rzucanie `CloneNotSupportedException`).

### Pytania kontrolne

1. Wytłumacz różnicę między płytką (shallow) a głęboką (deep) kopią. Podaj scenariusz, kiedy płytka jest pułapką.
2. Dlaczego Joshua Bloch rekomenduje **copy constructor** zamiast `Cloneable`/`clone()`?
3. Czym przykład `ServerConfig.withHost(...)` różni się od `DocumentTemplate.copy()` koncepcyjnie?
4. *Pytanie dodatkowe, opcjonalne.* Gdzie w popularnych frameworkach Javy spotkasz Prototype? (Wskazówka: tzw. „scope" — *zakres życia* obiektu w kontenerze; jeśli nie używałeś żadnego frameworka, pomiń.)
5. Czy record (Java 14+) potrzebuje Prototype? Jaką jego namiastkę daje sam record (kompaktowy konstruktor, deconstruction)?
6. Dlaczego dla niemutowalnej konfiguracji `withXxx` zwraca **nową** instancję, a nie modyfikuje obecnej?
7. Wymień scenariusz biznesowy, w którym Prototype jest tańszy niż klasyczny `new` + setery.

---

## 6. Wzorzec Adapter

**Cel:** Połączyć dwie niezgodne klasy/interfejsy, których nie możesz (lub nie chcesz) modyfikować — przez stworzenie warstwy „tłumacza". Nauczysz się dwóch wariantów: **object adapter** (kompozycja) i **class adapter** (dziedziczenie) oraz dlaczego pierwszy jest preferowany w Javie.

**Kategoria:** Strukturalny

**Kiedy stosować (problem):**
W twojej aplikacji jest interfejs `PaymentProcessor` (wewnętrzny standard), a chcesz zintegrować bibliotekę zewnętrzną `LegacyStripeApi`, która ma zupełnie inny interfejs (`charge(...)` zamiast `pay(...)`). Nie kontrolujesz kodu Stripe, nie możesz go zmienić. Modyfikacja klienta (wszystkich miejsc używających `PaymentProcessor`) byłaby koszmarem. **Adapter** to klasa, która implementuje twój interfejs (`PaymentProcessor`) i wewnętrznie deleguje do Stripe API, tłumacząc parametry i wyniki.

Inne klasyczne przykłady: `Arrays.asList()` to adapter tablicy do `List`, `InputStreamReader` to adapter `InputStream` (bajty) do `Reader` (znaki).

**Konsekwencje (zalety i wady):**

- Plus: integruje istniejące klasy bez modyfikacji ich kodu.
- Plus: oddziela kod aplikacji od specyfiki konkretnej biblioteki.
- Plus: łatwo podmienić bibliotekę — wystarczy napisać nowy adapter.
- Plus: idealny przy migracjach (stara biblioteka → nowa, ale z adapterem `OldToNewAdapter`).
- Minus: dodatkowa warstwa pośrednia = więcej kodu, narzut wywołań metod.
- Minus: skomplikowane mapowanie może ukrywać prawdziwą logikę.
- Minus: nadużywany — gdy interfejsy są „prawie zgodne", lepiej dostosować jeden niż pisać adapter.

**Teoria w pigułce:**
**Object adapter** (Java preferuje) — adapter trzyma referencję do adaptee (kompozycja) i implementuje target interface. Wewnątrz każda metoda deleguje + tłumaczy. **Class adapter** (Java ma ograniczenia: brak wielodziedziczenia klas) — adapter dziedziczy po adaptee i implementuje target interface. Mniej elastyczny, bo nie zadziała, jeśli adaptee jest `final`.

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/adapter/PaymentProcessor.java`

```java
package com.example.wzorce.adapter;

/** Nasz wewnętrzny interfejs — używany w całej aplikacji. */
public interface PaymentProcessor {
    boolean pay(String customerId, double amountPln, String description);
}
```

Utwórz plik: `src/main/java/com/example/wzorce/adapter/LegacyStripeApi.java`

```java
package com.example.wzorce.adapter;

/**
 * Symulacja zewnętrznej biblioteki — innego API,
 * nad którą NIE mamy kontroli (nie możemy zmienić sygnatury).
 */
public class LegacyStripeApi {

    /** Stripe operuje w GROSZACH (long), nie PLN. */
    public StripeResult charge(String stripeCustomerId, long amountInCents,
                                String currency, String memo) {
        System.out.printf("Stripe.charge(customer=%s, cents=%d %s, memo=%s)%n",
                stripeCustomerId, amountInCents, currency, memo);
        // Symulujemy sukces dla niezerowej kwoty
        return new StripeResult(amountInCents > 0,
                "ch_" + System.nanoTime(),
                amountInCents > 0 ? "ok" : "amount must be positive");
    }

    public record StripeResult(boolean success, String chargeId, String message) {}
}
```

Utwórz plik: `src/main/java/com/example/wzorce/adapter/StripeAdapter.java`

```java
package com.example.wzorce.adapter;

/**
 * OBJECT ADAPTER (kompozycja) — preferowane podejście w Javie.
 * Adapter implementuje TARGET interface (PaymentProcessor)
 * i wewnętrznie deleguje do ADAPTEE (LegacyStripeApi).
 */
public class StripeAdapter implements PaymentProcessor {

    private final LegacyStripeApi stripe;
    private final String stripeCustomerIdPrefix;

    public StripeAdapter(LegacyStripeApi stripe, String stripeCustomerIdPrefix) {
        this.stripe = stripe;
        this.stripeCustomerIdPrefix = stripeCustomerIdPrefix;
    }

    @Override
    public boolean pay(String customerId, double amountPln, String description) {
        // Tłumaczenie parametrów: PLN (double) → grosze (long), customerId → stripeCustomerId
        long cents = Math.round(amountPln * 100);
        String stripeCustomerId = stripeCustomerIdPrefix + customerId;

        LegacyStripeApi.StripeResult res =
                stripe.charge(stripeCustomerId, cents, "PLN", description);

        // Tłumaczenie wyniku: StripeResult → boolean
        return res.success();
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/adapter/PayPalAdapter.java`

```java
package com.example.wzorce.adapter;

/**
 * Drugi adapter — pokazuje, że łatwo dodać kolejny dostawca.
 * Klient nie zauważy różnicy — wciąż używa PaymentProcessor.
 */
public class PayPalAdapter implements PaymentProcessor {

    @Override
    public boolean pay(String customerId, double amountPln, String description) {
        System.out.printf("PayPal.send(to=%s, amount=%.2f PLN, ref=%s)%n",
                customerId + "@paypal.com", amountPln, description);
        return amountPln > 0;
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/adapter/ArrayToListClassAdapter.java`

```java
package com.example.wzorce.adapter;

import java.util.AbstractList;

/**
 * CLASS ADAPTER — adapter dziedziczy z target type (AbstractList),
 * a źródłem danych jest pole (tablica wewnętrzna).
 *
 * To uproszczona wersja Arrays.asList(...) — dokładnie ten sam wzorzec
 * dziedziczenia po AbstractList w JDK.
 *
 * UWAGA: Class adapter w Javie ma ograniczenia — nie można jednocześnie
 * dziedziczyć po dwóch klasach. Dlatego JDK używa AbstractList jako
 * BASE CLASS i dziedziczenia, nie pełnoprawnego class adaptera GoF.
 */
public class ArrayToListClassAdapter<T> extends AbstractList<T> {

    private final T[] array;

    public ArrayToListClassAdapter(T[] array) { this.array = array; }

    @Override public T   get(int index) { return array[index]; }
    @Override public int size()         { return array.length; }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/adapter/Main.java`

```java
package com.example.wzorce.adapter;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Klient zna TYLKO PaymentProcessor — nie wie, czy to Stripe czy PayPal
        PaymentProcessor stripe  = new StripeAdapter(new LegacyStripeApi(), "cus_");
        PaymentProcessor paypal  = new PayPalAdapter();

        System.out.println("=== Stripe ===");
        stripe.pay("CUST-001", 199.99, "Subskrypcja roczna");

        System.out.println("\n=== PayPal ===");
        paypal.pay("CUST-001", 199.99, "Subskrypcja roczna");

        System.out.println("\n=== Class Adapter — tablica jako List ===");
        Integer[] tab = {1, 2, 3, 4, 5};
        List<Integer> jakoLista = new ArrayToListClassAdapter<>(tab);
        System.out.println("size: " + jakoLista.size());
        System.out.println("element[2]: " + jakoLista.get(2));
        System.out.println("toString: " + jakoLista);
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj i uruchom. Trzy bloki — Stripe płaci w groszach, PayPal w PLN, tablica zachowuje się jak lista.
2. Wszędzie klient widzi TEN SAM interfejs (`PaymentProcessor` lub `List`) — adapter ukrywa różnice.
3. Eksperyment: zmień `amountPln` na ujemną (np. `-5.0`). Stripe powinien zwrócić `false` z powodu reguły "amount must be positive" — bez modyfikacji kodu Stripe.
4. Refaktor: dodaj trzeci adapter `BlikAdapter`, który wymaga numeru telefonu zamiast `customerId`. Sygnatura `pay(...)` zostaje, BLIK wewnętrznie szuka telefonu w mapie `customerId → phone`.
5. Class adapter w JDK: otwórz `Arrays.asList(...)` w IDE. Zauważ, że zwraca `Arrays.ArrayList` — wewnętrzną klasę, która dziedziczy `AbstractList` i trzyma tablicę. To dokładnie wzorzec class adapter.
6. Anti-pattern: jeśli adapter ma więcej niż 50 linii skomplikowanej logiki konwersji, prawdopodobnie potrzebujesz dedykowanej klasy (np. `PaymentMapper`), a adapter ma trzymać tylko delegację. Sygnał, że twoje interfejsy są **fundamentalnie** różne.
7. Sprawdź też w JDK: `InputStreamReader` (adapter bajtów na znaki), `Collections.enumeration(...)` (Iterator → Enumeration).

### Pytania kontrolne

1. Czym różni się **object adapter** (kompozycja) od **class adapter** (dziedziczenie)? Który Java preferuje i dlaczego?
2. Wymień dwa przykłady Adaptera z JDK i opisz, co i do czego adapteruje.
3. Adapter zmienia **interfejs**, ale czy zmienia **zachowanie**? Jaka jest jego intencja?
4. Czemu w `StripeAdapter` mamy konwersję PLN → grosze, a nie po prostu `(long) amountPln`? (Wskazówka: błąd floating-point, `Math.round`.)
5. Co się stanie, jeśli `LegacyStripeApi` zmieni sygnaturę `charge(...)` w nowej wersji? Co musisz zmienić w klientach? (Tylko adapter — klient `PaymentProcessor` jest izolowany.)
6. Kiedy Adapter jest **anty-wzorcem**? (Wskazówka: dla małych różnic, lepiej dostosować jeden z interfejsów. Adapter to overkill dla 1 metody.)
7. Czy Adapter to to samo co Facade? Jaka jest różnica intencji?

---

## 7. Wzorzec Decorator

**Cel:** Dodawać zachowania do obiektu **w runtime**, bez modyfikacji jego klasy i bez nadużywania dziedziczenia. Nauczysz się klasycznego przykładu „kawa z dodatkami" oraz zobaczysz, że strumienie I/O w JDK to dekoratory.

**Kategoria:** Strukturalny

**Kiedy stosować (problem):**
Masz klasę bazową `Coffee` (lub `Beverage`), i chcesz dodawać warianty: mleko, cukier, syrop, bita śmietana. Klasyczne rozwiązanie przez dziedziczenie eksploduje:
`CoffeeWithMilk`, `CoffeeWithSugar`, `CoffeeWithMilkAndSugar`, `CoffeeWithMilkAndSyrup`, `CoffeeWithMilkAndSugarAndCream`... **Liczba klas = 2^liczba dodatków.**

Klasyczny przykład biznesowy: cennik kawy w restauracji (8 dodatków = 256 możliwych kombinacji), strumienie I/O (buforowanie, kompresja, szyfrowanie), warstwy w middleware HTTP.

**Konsekwencje (zalety i wady):**

- Plus: kompozycja zamiast dziedziczenia — uniknięcie kombinacji klas.
- Plus: można dodawać dekoratory w **runtime** (np. na podstawie wyboru klienta).
- Plus: każdy dekorator ma jedną odpowiedzialność (SRP).
- Plus: kolejność dekoratorów ma znaczenie i daje elastyczność.
- Minus: wiele małych klas — trudniej zrozumieć od pierwszego rzutu oka.
- Minus: dla typu wynikowego widzisz „Decorator", a nie konkretną klasę.
- Minus: nadużywany — dla 2 cech wystarczy dziedziczenie lub kompozycja prostsza.

**Teoria w pigułce:**
Dekorator implementuje **ten sam interfejs** co obiekt dekorowany, trzyma go jako pole (`delegate`/`wrappee`), i w każdej metodzie deleguje + dodaje swoje zachowanie. Klient widzi typ interfejsu — może wywołać `coffee.cost()` na `Coffee`, ale nie wie, czy to `EspressoCoffee` czy `MilkDecorator(SugarDecorator(EspressoCoffee))`.

**Różnica z Proxy:** dekorator **rozszerza** zachowanie (dodaje cechę), proxy **kontroluje** dostęp (logging, cache, autoryzacja). Kod wygląda identycznie — różni się intencja.

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/decorator/Coffee.java`

```java
package com.example.wzorce.decorator;

/** Wspólny interfejs — wszyscy (kawa bazowa + dekoratory) implementują go. */
public interface Coffee {
    String description();
    double cost();
}
```

Utwórz plik: `src/main/java/com/example/wzorce/decorator/Espresso.java`

```java
package com.example.wzorce.decorator;

/** Konkretna kawa bazowa — najprostszy wariant. */
public class Espresso implements Coffee {
    @Override public String description() { return "Espresso"; }
    @Override public double cost()        { return 8.00; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/decorator/Americano.java`

```java
package com.example.wzorce.decorator;

public class Americano implements Coffee {
    @Override public String description() { return "Americano"; }
    @Override public double cost()        { return 10.00; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/decorator/CoffeeDecorator.java`

```java
package com.example.wzorce.decorator;

/**
 * Wspólna klasa bazowa dla dekoratorów — opcjonalna, ale wygodna.
 * Implementuje Coffee i trzyma delegate. Konkretne dekoratory
 * nadpisują tylko to, co rzeczywiście zmieniają.
 */
public abstract class CoffeeDecorator implements Coffee {

    protected final Coffee delegate;

    protected CoffeeDecorator(Coffee delegate) {
        this.delegate = delegate;
    }

    // Domyślne implementacje — delegują do wrappee.
    // Konkretne dekoratory nadpisują w razie potrzeby.
    @Override public String description() { return delegate.description(); }
    @Override public double cost()        { return delegate.cost(); }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/decorator/MilkDecorator.java`

```java
package com.example.wzorce.decorator;

public class MilkDecorator extends CoffeeDecorator {

    public MilkDecorator(Coffee delegate) { super(delegate); }

    @Override public String description() { return delegate.description() + " + mleko"; }
    @Override public double cost()        { return delegate.cost() + 1.50; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/decorator/SugarDecorator.java`

```java
package com.example.wzorce.decorator;

public class SugarDecorator extends CoffeeDecorator {

    public SugarDecorator(Coffee delegate) { super(delegate); }

    @Override public String description() { return delegate.description() + " + cukier"; }
    @Override public double cost()        { return delegate.cost() + 0.50; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/decorator/WhippedCreamDecorator.java`

```java
package com.example.wzorce.decorator;

public class WhippedCreamDecorator extends CoffeeDecorator {

    public WhippedCreamDecorator(Coffee delegate) { super(delegate); }

    @Override public String description() { return delegate.description() + " + bita śmietana"; }
    @Override public double cost()        { return delegate.cost() + 3.00; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/decorator/SyrupDecorator.java`

```java
package com.example.wzorce.decorator;

public class SyrupDecorator extends CoffeeDecorator {

    private final String flavor;
    private static final double PRICE = 2.00;

    public SyrupDecorator(Coffee delegate, String flavor) {
        super(delegate);
        this.flavor = flavor;
    }

    @Override public String description() {
        return delegate.description() + " + syrop " + flavor;
    }
    @Override public double cost() {
        return delegate.cost() + PRICE;
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/decorator/Main.java`

```java
package com.example.wzorce.decorator;

public class Main {
    public static void main(String[] args) {

        // 1) Czyste espresso
        Coffee espresso = new Espresso();
        printCoffee(espresso);

        // 2) Americano z mlekiem
        Coffee americanoMleko = new MilkDecorator(new Americano());
        printCoffee(americanoMleko);

        // 3) Espresso z mlekiem, cukrem i bitą śmietaną
        Coffee zlozona = new WhippedCreamDecorator(
                            new SugarDecorator(
                                new MilkDecorator(
                                    new Espresso())));
        printCoffee(zlozona);

        // 4) Americano z dwiema porcjami cukru
        Coffee podwojnyCukier = new SugarDecorator(new SugarDecorator(new Americano()));
        printCoffee(podwojnyCukier);

        // 5) Z syropem waniliowym
        Coffee waniliowa = new SyrupDecorator(new MilkDecorator(new Espresso()), "wanilia");
        printCoffee(waniliowa);
    }

    private static void printCoffee(Coffee c) {
        System.out.printf("%-50s %.2f PLN%n", c.description(), c.cost());
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj wszystko, uruchom. 5 wierszy w tabelce z cenami.
2. Zauważ — `Espresso + mleko + cukier + bita śmietana` to `8 + 1.50 + 0.50 + 3.00 = 13.00 PLN`. Każdy dekorator dodaje swoje.
3. Zauważ kompozycję od środka — `new WhippedCreamDecorator(new SugarDecorator(new MilkDecorator(new Espresso())))`. „Najpierw espresso, potem mleko, potem cukier, potem bita śmietana".
4. Dodaj szósty dekorator: `IceDecorator` — dodaje `+ lód`, ale **bez kosztu**. Sprawdź, że bazowa cena się nie zmienia.
5. Refaktor stream'owy: spróbuj zbudować kawę za pomocą `Stream.of(...).reduce(...)` przekazując funkcję dekoratora. Czy to jeszcze Decorator GoF, czy już kompozycja funkcji?
6. Otwórz w IDE `BufferedReader` — to dekorator dla `Reader`. Zauważ konstrukcję `new BufferedReader(new FileReader("x.txt"))` — to ta sama struktura co `new WhippedCreamDecorator(new Espresso())`. Strumienie I/O w Javie to **kanoniczne** dekoratory.
7. Anti-pattern: jeśli każdy „dekorator" robi to samo (np. tylko loguje), to nie dekorator — to Proxy lub middleware. Sygnał: ich `description()`/`cost()` nie różni się od delegata.

### Pytania kontrolne

1. Wytłumacz, czym dekorator różni się od **proxy** (na poziomie intencji, nie struktury).
2. Czy kolejność dekoratorów ma znaczenie? Podaj przykład, w którym `A(B(x))` daje inny wynik niż `B(A(x))`.
3. Wymień strumień z `java.io`, który jest klasycznym dekoratorem. Jakie zachowanie dodaje?
4. Dlaczego zamiast 256 klas (`CoffeeWithMilkAndSugarAndCream...`) wystarczy 1 klasa bazowa + N dekoratorów?
5. Co by się stało, gdyby `MilkDecorator` rozszerzał `Espresso` zamiast implementować `Coffee` (przez `CoffeeDecorator`)? (Wskazówka: nie zadziała dla `Americano`.)
6. Czy dekorator może dziedziczyć po dekorowanym obiekcie? Jakie są tego konsekwencje?
7. Jak dekorator ma się do zasady „kompozycja > dziedziczenie"?

---

## 8. Wzorzec Facade

**Cel:** Schować skomplikowany podsystem za **jednym prostym interfejsem**. Klient nie musi wiedzieć, że za fasadą stoi 10 klas — wywołuje jedną metodę i ma wynik. Nauczysz się tworzyć fasadę dla podsystemu obsługi zamówień (płatność + magazyn + email + audyt).

**Kategoria:** Strukturalny

**Kiedy stosować (problem):**
Twoja aplikacja ma 5 modułów: `PaymentService`, `WarehouseService`, `EmailService`, `AuditService`, `AnalyticsService`. Złożenie zamówienia wymaga koordynacji wszystkich pięciu w odpowiedniej kolejności. Klient (np. controller HTTP) musi wszystko poznać i poprawnie połączyć. Zmiana kolejności = zmiana w 50 miejscach.

Fasada `OrderFacade` ma jedną metodę `placeOrder(...)`, która ukrywa koordynację. Klient woła tylko `facade.placeOrder(order)`.

Inne klasyczne przykłady: `HttpClient` jako fasada nad warstwą sieciową; **JPA** (*Java Persistence API* — standard mapowania obiektów Javy na tabele bazy) z klasą `EntityManager` jako fasadą nad **ORM** (*Object-Relational Mapping* — automatyczna translacja obiekt ↔ wiersz SQL); **SLF4J** (*Simple Logging Facade for Java*) — sama nazwa zdradza wzorzec: fasada nad konkretnymi bibliotekami logowania (Logback, Log4j). *Te przykłady są kontekstem — nie musisz znać żadnej z tych bibliotek, by zrozumieć wzorzec.*

**Konsekwencje (zalety i wady):**

- Plus: redukcja sprzężenia — klient zna tylko fasadę.
- Plus: skomplikowana kolejność operacji ukryta w jednym miejscu.
- Plus: fasadę łatwo testować (test integracyjny zachowania) i mockować.
- Plus: można mieć kilka fasad o różnym poziomie szczegółowości („simple" i „advanced" API).
- Minus: fasada może stać się „god class" jeśli zaczniesz do niej dorzucać wszystko.
- Minus: ukrywa elastyczność — czasem klient chce dostęp do konkretnego subsystemu.
- Minus: dodaje warstwę pośrednią — narzut wywołań, więcej kodu.

**Teoria w pigułce:**
Fasada to klasa, która przyjmuje zależności do wszystkich podsystemów (kompozycja) i wystawia metody **wysokopoziomowe** opisujące cele biznesowe (`placeOrder`, `cancelSubscription`). W metodach koordynuje wywołania podsystemów. Fasada **nie dodaje logiki biznesowej** sama — tylko orkiestruje. Jeśli fasada zaczyna mieć własną logikę, to już nie fasada — to Service / Use Case.

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/facade/PaymentService.java`

```java
package com.example.wzorce.facade;

public class PaymentService {
    public boolean charge(String customerId, double amount) {
        System.out.printf("[PAYMENT] obciążam %s o %.2f PLN%n", customerId, amount);
        return amount > 0;
    }

    public void refund(String customerId, double amount) {
        System.out.printf("[PAYMENT] zwrot %.2f PLN dla %s%n", amount, customerId);
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/facade/WarehouseService.java`

```java
package com.example.wzorce.facade;

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
```

Utwórz plik: `src/main/java/com/example/wzorce/facade/EmailService.java`

```java
package com.example.wzorce.facade;

public class EmailService {
    public void sendConfirmation(String customerId, String orderId) {
        System.out.printf("[EMAIL] potwierdzenie do %s dla %s%n", customerId, orderId);
    }

    public void sendCancellation(String customerId, String orderId) {
        System.out.printf("[EMAIL] anulowanie do %s dla %s%n", customerId, orderId);
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/facade/AuditService.java`

```java
package com.example.wzorce.facade;

public class AuditService {
    public void log(String message) {
        System.out.printf("[AUDIT] %s%n", message);
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/facade/Order.java`

```java
package com.example.wzorce.facade;

import java.util.List;

public record Order(String orderId, String customerId,
                    List<String> items, double total) {}
```

Utwórz plik: `src/main/java/com/example/wzorce/facade/OrderFacade.java`

```java
package com.example.wzorce.facade;

/**
 * FASADA — jedna metoda placeOrder(...) ukrywa koordynację 4 podsystemów.
 * Klient nie wie, że za fasadą stoi PaymentService, WarehouseService,
 * EmailService, AuditService.
 */
public class OrderFacade {

    private final PaymentService payment;
    private final WarehouseService warehouse;
    private final EmailService email;
    private final AuditService audit;

    public OrderFacade(PaymentService payment, WarehouseService warehouse,
                       EmailService email, AuditService audit) {
        this.payment = payment;
        this.warehouse = warehouse;
        this.email = email;
        this.audit = audit;
    }

    public boolean placeOrder(Order order) {
        audit.log("Próba złożenia zamówienia " + order.orderId());

        boolean paid = payment.charge(order.customerId(), order.total());
        if (!paid) {
            audit.log("Płatność nieudana dla " + order.orderId());
            return false;
        }

        boolean reserved = warehouse.reserve(order.items());
        if (!reserved) {
            payment.refund(order.customerId(), order.total());   // rollback
            audit.log("Magazyn pusty, zrefundowano " + order.orderId());
            return false;
        }

        email.sendConfirmation(order.customerId(), order.orderId());
        audit.log("Zamówienie " + order.orderId() + " złożone pomyślnie");
        return true;
    }

    public void cancelOrder(Order order) {
        warehouse.release(order.items());
        payment.refund(order.customerId(), order.total());
        email.sendCancellation(order.customerId(), order.orderId());
        audit.log("Zamówienie " + order.orderId() + " anulowane");
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/facade/Main.java`

```java
package com.example.wzorce.facade;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Klient (np. kontroler HTTP) widzi TYLKO fasadę.
        OrderFacade facade = new OrderFacade(
                new PaymentService(),
                new WarehouseService(),
                new EmailService(),
                new AuditService()
        );

        Order zamowienie = new Order("ORD-001", "CUST-42",
                List.of("Laptop", "Mysz"), 4299.50);

        System.out.println("=== Składanie zamówienia ===");
        facade.placeOrder(zamowienie);

        System.out.println("\n=== Anulowanie ===");
        facade.cancelOrder(zamowienie);

        System.out.println("\n=== Próba pustego zamówienia ===");
        Order puste = new Order("ORD-002", "CUST-42", List.of(), 0.00);
        boolean ok = facade.placeOrder(puste);
        System.out.println("Sukces? " + ok);
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj wszystko i uruchom. Powinny pojawić się 3 bloki — udane zamówienie, anulowanie, próba błędnego (płatność nieudana).
2. Zauważ: `Main` nie wie nic o `WarehouseService`, `EmailService`, `AuditService`. Zna tylko fasadę i 4 wstrzykiwane zależności (które są tu tworzone ręcznie przez `new ...()` — *ciekawostka:* w aplikacji używającej kontenera DI obiekty te byłyby tworzone i wstrzykiwane automatycznie, ale to nie jest wymagane do zrozumienia wzorca).
3. Symulacja błędu magazynu: zmień `WarehouseService.reserve()` na `return false;`. Uruchom — w logach powinien pojawić się rollback płatności.
4. Refaktor: wyciągnij parametry kolejności z `placeOrder` do listy `OrderStep` (interfejs funkcyjny) i iteruj. Co właśnie zrobiłeś? (Chain of Responsibility wewnątrz fasady. To OK — fasada może wewnętrznie używać innych wzorców.)
5. Anti-pattern: fasada z 50 metodami i logiką biznesową w środku to nie fasada — to god class. Sygnał: fasada nie powinna mieć więcej niż ~10 metod publicznych i każda metoda krótka (orkiestracja, nie logika).
6. Sprawdź w JDK: `HttpClient` to fasada (ukrywa connection pooling, retry, parsing, redirecty). Otwórz `HttpClient.send(...)` w IDE.
7. *Opcjonalnie, jeśli kiedyś poznałeś JPA:* `EntityManager` (z JPA) — fasada nad ORM (np. Hibernate). API jest proste (`persist`, `find`, `merge`), pod spodem dzieje się magia (generacja SQL, cache, śledzenie zmian). Jeśli nie znasz JPA, pomiń ten krok — wystarczy `HttpClient` z punktu 6.

### Pytania kontrolne

1. Czym fasada różni się od adaptera? Oba ukrywają coś za sobą — czym?
2. Dlaczego `OrderFacade.placeOrder(...)` ma **rollback** płatności, gdy magazyn nie ma towaru?
3. Jakie elementy aplikacji powinny być za fasadą, a jakie nie? (Wskazówka: złożone podsystemy z kilkoma krokami, NIE prosta klasa z jedną metodą.)
4. Czy fasada może mieć swoją własną logikę biznesową? Czym fasada różni się od zwykłej klasy „Service" (klasy serwisowej, która zawiera logikę domenową)? (Wskazówka: fasada *orkiestruje* podsystemy, serwis *implementuje* logikę. To są dwie różne role, choć implementacja klasą może wyglądać podobnie.)
5. Wymień przykład fasady z JDK lub biblioteki Java.
6. Co znaczy „god class" w kontekście fasady i jak tego uniknąć?
7. Jak fasada wspiera zasadę Demeter (Law of Demeter)? Czy zmniejsza, czy zwiększa sprzężenie?

---

## 9. Wzorzec Proxy (Static + Dynamic Proxy)

**Cel:** Zastąpić obiekt „zastępnikiem" implementującym ten sam interfejs, dodającym **kontrolę dostępu** (logging, cache, autoryzacja, lazy loading, transakcje). Nauczysz się dwóch wariantów: **statyczny proxy** (ręczna klasa) oraz **dynamic proxy** (`java.lang.reflect.Proxy`) — fundament całego **AOP** (*Aspect-Oriented Programming* — programowania aspektowego: dodawania zachowań wokół metod *bez* zmiany ich kodu) w nowoczesnych frameworkach. *Znajomość AOP nie jest wymagana* — tutaj implementujesz mechanizm od zera, więc zobaczysz, jak to działa pod spodem.

**Kategoria:** Strukturalny

**Kiedy stosować (problem):**
Chcesz dodać do każdej metody serwisu logowanie + pomiar czasu + cache. Klasyczne podejście „dopisz wszędzie" da:
- duplikowany boilerplate w 20 metodach,
- zaszumioną logikę biznesową,
- trudne testowanie (logika i logging w jednej metodzie).

Proxy daje czystą logikę biznesową w realnej klasie i **dodaje** zachowania w klasie-proxy. Dynamic Proxy idzie krok dalej — jeden `InvocationHandler` (uchwyt do wywołań) obsługuje **wszystkie** metody interfejsu w runtime, bez ręcznego pisania proxy dla każdej klasy. *Ciekawostka:* to dokładnie ten mechanizm, którego nowoczesne frameworki używają do adnotacji typu `@Transactional` (otwarcie/zamknięcie transakcji bazy danych), `@Cacheable` (zapisanie wyniku w cache) czy `@PreAuthorize` (sprawdzenie uprawnień przed wywołaniem) — gdy w kodzie widzisz taką adnotację, „za kulisami" framework tworzy proxy. Znajomość tych adnotacji **nie jest tu wymagana** — sam zbudujesz analogiczny mechanizm w prostszej formie.

**Konsekwencje (zalety i wady):**

- Plus: **cross-cutting concerns** („aspekty przekrojowe" — funkcjonalności potrzebne w wielu klasach naraz: logowanie, cache, autoryzacja, audyt) trzymane w jednym miejscu.
- Plus: klient nie wie, że gada z proxy — operuje na interfejsie.
- Plus: można stackować wiele proxy (nakładać warstwami).
- Plus: Dynamic Proxy generuje proxy w runtime — żadnego boilerplate'u (powtarzalnego kodu szkieletowego).
- Minus: trudniejszy debug — stack trace pełen klas `Proxy$N`.
- Minus: wywołanie wewnętrzne (`this.method()`) omija proxy — częsta pułapka, gdy używasz Proxy do logiki przekrojowej.
- Minus: Dynamic Proxy działa tylko dla **interfejsów** (nie dla klas). Aby owinąć proxy klasę bez interfejsu, potrzebujesz bibliotek typu **CGLIB** (*Code Generation Library* — generuje podklasy w runtime) lub ByteBuddy. *Znajomość tych bibliotek nie jest wymagana* — wystarczy wiedzieć, że istnieją na wypadek ograniczeń JDK.
- Minus: łatwo pomylić z Decoratorem (identyczna struktura, inna intencja).

**Teoria w pigułce:**
**Statyczny Proxy** — klasa implementująca ten sam interfejs co target, trzymająca delegate, dodająca zachowanie wokół wywołań. **Dynamic Proxy** — `Proxy.newProxyInstance(loader, interfaces, handler)` tworzy w runtime obiekt implementujący podane interfejsy; wywołania trafiają do `InvocationHandler.invoke(proxy, method, args)`.

**Różnica z Decoratorem:** identyczna struktura, różna intencja. Decorator **rozszerza** zachowanie (dodaje cechę: buforowanie, mleko, cukier). Proxy **kontroluje** dostęp (logging, cache, security).

### Kod krok-po-kroku — implementacja (Static Proxy)

Utwórz plik: `src/main/java/com/example/wzorce/proxy/UserService.java`

```java
package com.example.wzorce.proxy;

import java.util.Optional;
import java.util.List;

public interface UserService {
    Optional<String> findById(Long id);
    String save(String name);
    List<String> findAll();
}
```

Utwórz plik: `src/main/java/com/example/wzorce/proxy/DatabaseUserService.java`

```java
package com.example.wzorce.proxy;

import java.util.Optional;
import java.util.List;

/** Realna implementacja — TYLKO logika biznesowa (SRP). */
public class DatabaseUserService implements UserService {

    @Override
    public Optional<String> findById(Long id) {
        System.out.println("[DB] SELECT * FROM users WHERE id = " + id);
        try { Thread.sleep(50); } catch (InterruptedException ignored) {}   // symulacja
        return id > 0 ? Optional.of("User#" + id) : Optional.empty();
    }

    @Override
    public String save(String name) {
        System.out.println("[DB] INSERT INTO users(name) VALUES('" + name + "')");
        return name + "-saved";
    }

    @Override
    public List<String> findAll() {
        System.out.println("[DB] SELECT * FROM users");
        return List.of("User#1", "User#2", "User#3");
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/proxy/LoggingProxy.java`

```java
package com.example.wzorce.proxy;

import java.util.Optional;
import java.util.List;

/**
 * STATYCZNY PROXY z logowaniem + pomiarem czasu.
 * Każda metoda: log → delegate → log z czasem.
 */
public class LoggingProxy implements UserService {

    private final UserService delegate;

    public LoggingProxy(UserService delegate) { this.delegate = delegate; }

    @Override
    public Optional<String> findById(Long id) {
        long start = System.nanoTime();
        System.out.println("[LOG] findById(" + id + ") — start");
        try {
            Optional<String> result = delegate.findById(id);
            System.out.printf("[LOG] findById(%d) — %dms — %s%n",
                    id, (System.nanoTime() - start) / 1_000_000,
                    result.isPresent() ? "found" : "empty");
            return result;
        } catch (Exception e) {
            System.out.println("[LOG] findById ERROR: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public String save(String name) {
        long start = System.nanoTime();
        System.out.println("[LOG] save(" + name + ") — start");
        String r = delegate.save(name);
        System.out.printf("[LOG] save(%s) — %dms%n", name,
                (System.nanoTime() - start) / 1_000_000);
        return r;
    }

    @Override
    public List<String> findAll() { return delegate.findAll(); }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/proxy/CachingProxy.java`

```java
package com.example.wzorce.proxy;

import java.util.Map;
import java.util.Optional;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/** STATYCZNY PROXY z cache. */
public class CachingProxy implements UserService {

    private final UserService delegate;
    private final Map<Long, String> cache = new ConcurrentHashMap<>();

    public CachingProxy(UserService delegate) { this.delegate = delegate; }

    @Override
    public Optional<String> findById(Long id) {
        String cached = cache.get(id);
        if (cached != null) {
            System.out.println("[CACHE] HIT id=" + id);
            return Optional.of(cached);
        }
        System.out.println("[CACHE] MISS id=" + id);
        Optional<String> result = delegate.findById(id);
        result.ifPresent(v -> cache.put(id, v));
        return result;
    }

    @Override
    public String save(String name) {
        cache.clear();   // unieważniamy cache po zapisie (uproszczone)
        return delegate.save(name);
    }

    @Override
    public List<String> findAll() { return delegate.findAll(); }
}
```

### Kod krok-po-kroku — implementacja (Dynamic Proxy)

Utwórz plik: `src/main/java/com/example/wzorce/proxy/dynamicproxy/DynamicLoggingHandler.java`

```java
package com.example.wzorce.proxy.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * DYNAMIC PROXY — InvocationHandler obsługuje WSZYSTKIE metody interfejsu.
 * Nie musisz ręcznie pisać proxy dla każdej klasy — jeden handler załatwia całość.
 *
 * Ciekawostka (znajomość frameworka nie wymagana):
 * to DOKŁADNIE ten mechanizm, którego nowoczesne frameworki używają do
 * adnotacji typu @Transactional (otwarcie transakcji bazy),
 * @Cacheable (cache wyników) czy @PreAuthorize (sprawdzenie uprawnień).
 */
public class DynamicLoggingHandler implements InvocationHandler {

    private final Object target;

    public DynamicLoggingHandler(Object target) { this.target = target; }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.nanoTime();
        System.out.println("[DYNAMIC-LOG] " + method.getName() + " — start");
        try {
            Object result = method.invoke(target, args);
            System.out.printf("[DYNAMIC-LOG] %s — %dms — %s%n",
                    method.getName(),
                    (System.nanoTime() - start) / 1_000_000,
                    result);
            return result;
        } catch (java.lang.reflect.InvocationTargetException e) {
            // Rozpakuj rzeczywisty wyjątek — InvocationTargetException jest opakowaniem
            throw e.getCause();
        }
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/proxy/dynamicproxy/DynamicProxyFactory.java`

```java
package com.example.wzorce.proxy.dynamicproxy;

import java.lang.reflect.Proxy;

public class DynamicProxyFactory {

    /**
     * Tworzy proxy implementujące te same interfejsy co target,
     * przekierowujące wywołania do handlera.
     */
    @SuppressWarnings("unchecked")
    public static <T> T wrap(T target, Class<T> iface) {
        return (T) Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class<?>[] { iface },
                new DynamicLoggingHandler(target)
        );
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/proxy/Main.java`

```java
package com.example.wzorce.proxy;

import com.example.wzorce.proxy.dynamicproxy.DynamicProxyFactory;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== STATYCZNY PROXY: logging + caching ===");
        UserService real    = new DatabaseUserService();
        UserService cached  = new CachingProxy(real);
        UserService logged  = new LoggingProxy(cached);

        logged.findById(5L);
        System.out.println();
        logged.findById(5L);   // drugie wywołanie — cache hit

        System.out.println("\n=== DYNAMIC PROXY ===");
        UserService dynamicProxy = DynamicProxyFactory.wrap(
                new DatabaseUserService(), UserService.class);

        dynamicProxy.findById(42L);
        dynamicProxy.save("Anna");
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj, uruchom. Pierwsze `findById(5)`: LOG start → CACHE miss → DB → CACHE store → LOG koniec. Drugie: LOG start → CACHE hit → LOG koniec (bez DB!).
2. Zamień kolejność proxy: `logged(cached(real))` na `cached(logged(real))`. Co się zmieni w logach? (Cache hity nie pojawią się w logu — bo logging jest **wewnątrz** cache.)
3. Dynamic Proxy: zauważ, że NIE było pisania ręcznej klasy `LoggingProxy` dla każdej metody. Jeden `InvocationHandler` obsłużył wszystkie.
4. Dodaj drugi handler `DynamicCachingHandler` — przyjmuje target i cache `Map<String, Object>`, kluczem jest `method.getName()+args`. Połącz przez kompozycję handlerów. Czy to wciąż czytelne?
5. Sprawdź ograniczenia Dynamic Proxy: spróbuj owinąć (*wrapnąć*) klasę bez interfejsu (usuń `implements UserService` z `DatabaseUserService`). Nie da się — Dynamic Proxy wymaga **interfejsu**. *Ciekawostka:* właśnie dlatego frameworki takie jak Spring sięgają po bibliotekę CGLIB, gdy chcą proxy na klasie konkretnej. Znajomość Springa nie jest tu wymagana — istotne jest ograniczenie samego JDK.
6. Eksperyment: jak wygląda `target.getClass().getName()` dla obiektu zwróconego przez `Proxy.newProxyInstance`? (Coś jak `com.sun.proxy.$Proxy0`.) To wyjaśnia, dlaczego stack trace aplikacji używających dynamic proxy bywa „magiczny" — w środku siedzi wygenerowana w runtime klasa.
7. Anti-pattern: wywołanie wewnętrzne — w `DatabaseUserService.findAll()` dodaj `this.findById(1L)`. Owin całość `LoggingProxy`. Wywołanie `findAll` zaloguje się raz, a `findById` w środku **nie**. Dlaczego? (`this` to konkretna klasa, nie proxy.)
8. Anti-pattern: mylenie z Decoratorem. Kawa + mleko + cukier = Decorator (rozszerzanie). Logging + cache + security = Proxy (kontrola dostępu). Kod identyczny, intencja inna.

### Pytania kontrolne

1. Czym Proxy różni się od Decorator? (Jedna odpowiedź dotyczy intencji — jakiej?)
2. Wyjaśnij, dlaczego Dynamic Proxy wymaga **interfejsu**. Co zrobiłbyś, gdyby twoja klasa nie miała interfejsu?
3. Dlaczego wywołanie `this.metoda()` z wnętrza klasy **omija** Proxy w nowoczesnych frameworkach?
4. Wymień 3 *cross-cutting concerns* (czyli funkcjonalności potrzebne w wielu klasach naraz — typu logowanie, cache, autoryzacja, audyt, pomiar czasu), które naturalnie pasują do Proxy.
5. Jakie 3 elementy musisz przekazać do `Proxy.newProxyInstance(...)`?
6. Co robi `method.invoke(target, args)` wewnątrz `InvocationHandler.invoke`? Dlaczego nie wywołujesz target bezpośrednio?
7. Co znaczy „stackowanie" proxy? Podaj przykład sensownej kolejności (logging vs caching vs auth).

---

## 10. Wzorzec Composite

**Cel:** Reprezentować strukturę drzewiastą tak, by **liście i węzły traktować jednakowo**. Klient nie musi rozróżniać „czy to plik, czy folder" — obu się traktuje przez ten sam interfejs. Nauczysz się modelować system plików oraz strukturę organizacyjną firmy.

**Kategoria:** Strukturalny

**Kiedy stosować (problem):**
Modelujesz strukturę hierarchiczną (system plików, menu z podmenu, strukturę organizacyjną, **DOM** HTML — *Document Object Model*, czyli drzewiastą reprezentację dokumentu w pamięci). Klient chce wyliczyć rozmiar folderu — ale „folder" zawiera pliki **i** podfoldery. Jeśli `getSize()` żyje tylko na pliku, klient musi rekurencyjnie obsługiwać foldery. Composite pozwala wywołać `node.getSize()` na czymkolwiek — folder zsumuje swoich dzieci.

Biznesowe przykłady: struktura organizacyjna (pracownik + zespół + dział), faktura z pozycjami i grupami pozycji, panel UI (przycisk + grupa przycisków + okno), drzewo zadań w project management.

**Konsekwencje (zalety i wady):**

- Plus: klient traktuje liść i węzeł jednakowo (jednolity interfejs).
- Plus: łatwe operacje rekurencyjne (zsumuj, policz, znajdź).
- Plus: łatwe dodawanie nowych typów liści/węzłów.
- Minus: trudniej zachować typową bezpieczność — wszystko jest „komponentem", więc traci się typy specyficzne.
- Minus: metody `add(child)`, `remove(child)` na liściu są bezsensowne — co zwrócić lub rzucić?
- Minus: nadużywany dla płaskich struktur — wtedy lepsza zwykła lista.

**Teoria w pigułce:**
Wspólny interfejs `Component` mają **liść** (`Leaf` — np. plik) i **węzeł** (`Composite` — np. folder). Composite trzyma listę `Component` (czyli zarówno liści, jak i innych Composite). Operacje są rekurencyjne — Composite woła operację na każdym dziecku, agreguje wyniki.

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/composite/FileSystemNode.java`

```java
package com.example.wzorce.composite;

/** Wspólny interfejs — file i folder go implementują. */
public interface FileSystemNode {
    String getName();
    long getSize();
    void print(String prefix);
}
```

Utwórz plik: `src/main/java/com/example/wzorce/composite/FileNode.java`

```java
package com.example.wzorce.composite;

/** LIŚĆ — plik nie ma dzieci. */
public class FileNode implements FileSystemNode {
    private final String name;
    private final long size;

    public FileNode(String name, long size) {
        this.name = name;
        this.size = size;
    }

    @Override public String getName() { return name; }
    @Override public long getSize()   { return size; }

    @Override
    public void print(String prefix) {
        System.out.printf("%s- %s (%d B)%n", prefix, name, size);
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/composite/FolderNode.java`

```java
package com.example.wzorce.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * COMPOSITE — folder zawiera dzieci (pliki LUB inne foldery).
 * getSize() rekurencyjnie sumuje rozmiary dzieci.
 */
public class FolderNode implements FileSystemNode {
    private final String name;
    private final List<FileSystemNode> children = new ArrayList<>();

    public FolderNode(String name) { this.name = name; }

    public FolderNode add(FileSystemNode child) {
        children.add(child);
        return this;
    }

    public void remove(FileSystemNode child) {
        children.remove(child);
    }

    @Override public String getName() { return name; }

    @Override
    public long getSize() {
        // Rekurencja — każde dziecko może być plikiem lub folderem,
        // ale dla composite to nie ma znaczenia (jednolity interfejs).
        long total = 0;
        for (FileSystemNode child : children) {
            total += child.getSize();
        }
        return total;
    }

    @Override
    public void print(String prefix) {
        System.out.printf("%s+ %s/ (%d B)%n", prefix, name, getSize());
        for (FileSystemNode child : children) {
            child.print(prefix + "  ");
        }
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/composite/Employee.java`

```java
package com.example.wzorce.composite;

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
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/composite/Main.java`

```java
package com.example.wzorce.composite;

public class Main {
    public static void main(String[] args) {

        // 1) System plików
        FolderNode root = new FolderNode("root");
        root.add(new FileNode("readme.txt", 200));
        FolderNode src = new FolderNode("src");
        src.add(new FileNode("Main.java", 1500));
        src.add(new FileNode("Utils.java", 800));
        root.add(src);

        FolderNode tests = new FolderNode("tests");
        tests.add(new FileNode("MainTest.java", 1100));
        root.add(tests);

        root.print("");
        System.out.println("\nRozmiar całości: " + root.getSize() + " B");

        // 2) Struktura organizacyjna
        System.out.println("\n=== Struktura organizacyjna ===");
        Employee cto = new Employee("CTO Marek", 25000);
        cto.addReport(new Employee("Dev Anna", 10000));
        cto.addReport(new Employee("Dev Jan", 11000));

        Employee teamLead = new Employee("TL Kasia", 16000);
        teamLead.addReport(new Employee("Junior Tomek", 6500));
        teamLead.addReport(new Employee("Junior Ola", 6500));
        cto.addReport(teamLead);

        cto.print("");
        System.out.printf("%nCałkowity koszt: %.2f PLN, rozmiar zespołu: %d osób%n",
                cto.totalSalaryCost(), cto.teamSize());
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj i uruchom — powinieneś zobaczyć strukturę katalogów i strukturę organizacyjną.
2. Zauważ rekurencję — `root.getSize()` sumuje wszystko, łącznie z plikami w podfolderach.
3. Klient (`Main`) wywołuje `getSize()` na `FolderNode` bez rozróżniania, czy w środku są same pliki, czy zagnieżdżone foldery. Klient = klient.
4. Dodaj `addReport` do siebie samego: `anna.addReport(cto)`. Rekurencja wybuchnie StackOverflow. Composite nie chroni przed cyklami — w realnym kodzie trzeba je wykrywać.
5. Refaktor: dodaj metodę `find(String name)` w `FolderNode`, która rekurencyjnie szuka pliku/folderu po nazwie. Zwraca `Optional<FileSystemNode>`.
6. Eksperyment: spróbuj dodać `void add(FileSystemNode)` do interfejsu `FileSystemNode`. Co zrobi `FileNode.add(...)`? (Rzucić wyjątek, zwrócić false, ignorować?) To znana wada Composite — przeciekająca abstrakcja, kompromis między czystym typem a wygodą.
7. Sprawdź w Swing/AWT: `Container` zawiera `Component`. `Container` sam jest `Component`. To klasyczny Composite w bibliotece UI Javy.

### Pytania kontrolne

1. Czym różni się **liść** od **composite** w tym wzorcu?
2. Dlaczego klient nie potrzebuje wiedzieć, czy obiekt to plik czy folder?
3. Wymień przykład Composite z biblioteki UI Javy (Swing/JavaFX).
4. Jak wzorzec radzi sobie z metodami typu `add(child)` — czy powinny być w interfejsie, czy tylko w composite?
5. Co się stanie, jeśli struktura ma cykl (A zawiera B, B zawiera A)? Jak temu zapobiec?
6. Jak Composite ma się do struktury DOM w HTML / XML?
7. Wymień scenariusz biznesowy, w którym Composite jest naturalny.

---

## 11. Wzorzec Strategy (z lambdami w Java 8+)

**Cel:** Wymieniać algorytmy w runtime — bez modyfikacji klienta. Nauczysz się klasycznej formy Strategy (interfejs + klasy) oraz nowoczesnej (interfejs funkcyjny + lambdy + dispatch table).

**Kategoria:** Behawioralny

**Kiedy stosować (problem):**
Sklep ma 5 typów rabatów: STANDARD (brak), BULK (10% dla ≥10 sztuk), LOYALTY (-10%), SALE (-50 PLN), VIP (-20%). Klasyczna implementacja jako drabinka `if/else` lub `switch` jest pułapką:

```java
if (type.equals("STANDARD")) return total;
else if (type.equals("BULK")) return total * (qty >= 10 ? 0.85 : 1.0);
else if (type.equals("LOYALTY")) return total * 0.90;
// ...
```

Każdy nowy rabat to zmiana TEGO kodu (naruszenie OCP), klasa rośnie, trudna kompozycja („VIP + SALE razem"), trudne testy. **Strategy** wymienialny algorytm jako obiekt — w Javie 8+ to często **lambda**.

Inne biznesowe przykłady: polityki cenowe, formuły podatkowe, walidacja danych (regex per pole), strategie wysyłki (Inpost/DPD/UPS), algorytmy sortowania (Comparator).

**Konsekwencje (zalety i wady):**

- Plus: nowy algorytm = nowa klasa/lambda + wpis do mapy. Brak modyfikacji klienta.
- Plus: każdy algorytm testowalny osobno.
- Plus: dynamiczna zmiana strategii w runtime.
- Plus: kompozycja strategii (dekoracja, łączenie).
- Minus: dla 2 wariantów to overkill — switch ma sens.
- Minus: rozproszenie logiki — strategie w wielu plikach (chyba że są lambdami w jednej mapie).
- Minus: klient musi wybrać strategię — przesuwa decyzję do warstwy wyżej.

**Teoria w pigułce:**
Interfejs `Strategy` definiuje **jedną metodę** algorytmu. Klient (context) trzyma referencję do strategii i woła jej metodę. Klient nie wie, czy używa A, B czy C. Dispatch table (`Map<klucz, strategia>`) to czysta forma — klucz wybierany z konfiguracji/requesta, strategia z mapy. W Java 8+ strategia często to interfejs funkcyjny i jedna lambda.

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/strategy/DiscountStrategy.java`

```java
package com.example.wzorce.strategy;

/**
 * Interfejs funkcyjny — strategia jako lambda.
 * apply(price, quantity) → cena finalna.
 */
@FunctionalInterface
public interface DiscountStrategy {
    double apply(double price, int quantity);
}
```

Utwórz plik: `src/main/java/com/example/wzorce/strategy/DiscountRegistry.java`

```java
package com.example.wzorce.strategy;

import java.util.Map;
import java.util.HashMap;

/**
 * Dispatch table — klucz to typ klienta, wartość to strategia.
 * Dodanie nowej strategii = jedna linia w register().
 */
public class DiscountRegistry {

    private final Map<String, DiscountStrategy> strategies = new HashMap<>();
    private final DiscountStrategy fallback;

    public DiscountRegistry(DiscountStrategy fallback) {
        this.fallback = fallback;
    }

    public DiscountRegistry register(String key, DiscountStrategy strategy) {
        strategies.put(key, strategy);
        return this;
    }

    public DiscountStrategy get(String key) {
        return strategies.getOrDefault(key, fallback);
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/strategy/PriceCalculator.java`

```java
package com.example.wzorce.strategy;

/**
 * CONTEXT — trzyma referencję do strategii, deleguje obliczenia.
 * Nie wie, jaka jest konkretna strategia — używa tylko interfejsu.
 */
public class PriceCalculator {

    private DiscountStrategy strategy;

    public PriceCalculator(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(double price, int quantity) {
        return strategy.apply(price, quantity);
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/strategy/Discounts.java`

```java
package com.example.wzorce.strategy;

/**
 * Wszystkie strategie jako STATIC FINAL lambdy w jednej klasie.
 * To czyste, czytelne i łatwe do utrzymania.
 * Tak właśnie pisze się Strategy w Java 8+.
 */
public final class Discounts {

    private Discounts() {}   // klasa pomocnicza

    public static final DiscountStrategy NONE =
            (price, qty) -> price * qty;

    public static final DiscountStrategy BULK =
            (price, qty) -> price * qty * (qty >= 10 ? 0.85 : 1.0);

    public static final DiscountStrategy LOYALTY =
            (price, qty) -> price * qty * 0.90;

    public static final DiscountStrategy SUMMER_SALE =
            (price, qty) -> Math.max(price * qty - 50, 0);

    public static final DiscountStrategy VIP =
            (price, qty) -> price * qty * 0.80;

    /** Dekorator strategii — dodaje warunkowy bonus -50 PLN dla totalu > 500. */
    public static DiscountStrategy withBonusAbove500(DiscountStrategy base) {
        return (price, qty) -> {
            double total = base.apply(price, qty);
            return total > 500 ? total - 50 : total;
        };
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/strategy/Main.java`

```java
package com.example.wzorce.strategy;

public class Main {
    public static void main(String[] args) {

        DiscountRegistry registry = new DiscountRegistry(Discounts.NONE)
                .register("STANDARD", Discounts.NONE)
                .register("BULK",     Discounts.BULK)
                .register("LOYALTY",  Discounts.LOYALTY)
                .register("SALE",     Discounts.SUMMER_SALE)
                .register("VIP",      Discounts.VIP);

        // Symulacja transakcji dla różnych klientów
        String[] typy = {"STANDARD", "BULK", "LOYALTY", "SALE", "VIP", "UNKNOWN"};
        double price = 49.99;
        int qty = 12;

        System.out.printf("%-10s | %s%n", "Typ", "Cena finalna");
        System.out.println("-----------+-------------");
        for (String typ : typy) {
            DiscountStrategy s = registry.get(typ);
            PriceCalculator calc = new PriceCalculator(s);
            System.out.printf("%-10s | %.2f PLN%n", typ, calc.calculate(price, qty));
        }

        // Kompozycja strategii — VIP + bonus
        System.out.println("\n=== VIP z bonusem -50 powyżej 500 ===");
        DiscountStrategy vipPlus = Discounts.withBonusAbove500(Discounts.VIP);
        System.out.printf("Cena: %.2f PLN%n",
                new PriceCalculator(vipPlus).calculate(price, qty));

        // Zmiana strategii w runtime
        System.out.println("\n=== Dynamiczna zmiana strategii ===");
        PriceCalculator dynamic = new PriceCalculator(Discounts.NONE);
        System.out.printf("Bazowa: %.2f PLN%n", dynamic.calculate(price, qty));
        dynamic.setStrategy(Discounts.VIP);
        System.out.printf("Po zmianie na VIP: %.2f PLN%n", dynamic.calculate(price, qty));
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj wszystko, uruchom. Powinieneś zobaczyć tabelkę 6 typów klientów z różnymi cenami.
2. Dodaj nową strategię `STUDENT` (15% rabatu). Tylko 2 linie zmian: nowa lambda w `Discounts`, jeden wpis w `register(...)`. Klasy `PriceCalculator` i `DiscountRegistry` NIE wymagają zmian — to OCP.
3. Sprawdź kompozycję — `withBonusAbove500(VIP)` to dekoracja strategii. To może być **Strategy** lub **Decorator** — kod identyczny, intencja inna.
4. Eksperyment: spróbuj zamienić `DiscountStrategy` na `Function<...>` lub `BiFunction<Double, Integer, Double>`. Co tracisz, co zyskujesz? (Tracisz semantykę, zyskujesz konwertowalność z `Function.compose/andThen`.)
5. Sprawdź klasyczny przykład Strategy w JDK: `Comparator`. `list.sort(Comparator.comparing(...))` to Strategy — wybierasz **strategię porównywania** w runtime.
6. Anti-pattern: jeśli widzisz w kodzie `Map<String, Strategy>` i obok `if-else` „dla wyjątków" — to znaczy, że Strategy nie jest dokończona. Wszystko ma być w mapie.
7. Refaktor: zamień `Discounts.NONE` na lambdę inline w jednym z miejsc i porównaj czytelność. Często osobna stała lepiej dokumentuje intencję niż lambda inline.

### Pytania kontrolne

1. Czym Strategy różni się od Factory Method? (Wskazówka: Factory **tworzy** obiekty, Strategy **wykonuje** algorytm.)
2. Dlaczego Java 8+ ułatwia Strategy? (Wskazówka: interfejs funkcyjny + lambda = jedna linia.)
3. Wyjaśnij, dlaczego dispatch table (Map) jest lepsza od switch. Co możesz w runtime zmienić w mapie, czego nie zmienisz w switch?
4. Wymień klasyczny przykład Strategy w JDK.
5. Co znaczy „kompozycja strategii"? Podaj przykład.
6. Czy `Comparator` to Strategy? Uzasadnij.
7. Kiedy Strategy jest **przesadą**? (Wskazówka: dla 2 wariantów bez planowanego wzrostu.)
8. Czy strategia powinna trzymać stan? (Wskazówka: jeśli tak, traci „funkcyjność" — wtedy bliżej do Command.)

---

## 12. Wzorzec Observer (PropertyChangeSupport + ręcznie)

**Cel:** Powiadamiać wiele obiektów o zmianie stanu **bez ich znajomości**. Nauczysz się ręcznej implementacji EventBus (zalecanej w nowoczesnych aplikacjach) oraz korzystania z `PropertyChangeSupport` z JDK (klasyczny mechanizm JavaBeans).

**Kategoria:** Behawioralny

**Kiedy stosować (problem):**
Zamówienie zostało złożone — trzeba wysłać maila, zarezerwować towar w magazynie, zalogować w audycie, zwiększyć licznik konwersji w analytics, wysłać SMS do kuriera. Naiwne rozwiązanie — `OrderService` zna wszystkie 5 modułów:

```java
class OrderService {
    void placeOrder(Order o) {
        save(o);
        emailService.send(o);
        warehouseService.reserve(o);
        analyticsService.record(o);
        smsService.notify(o);
        auditService.log(o);
    }
}
```

Każdy nowy listener = zmiana `OrderService`. Naruszenie OCP, SRP, sprzężenie. **Observer** rozwiązuje to: `OrderService` publikuje zdarzenie, niezależne moduły rejestrują się jako listenery.

**Konsekwencje (zalety i wady):**

- Plus: rozluźnienie sprzężenia — publisher nie zna subskrybentów.
- Plus: dynamiczne dodawanie/usuwanie listenerów w runtime.
- Plus: każdy listener testowalny osobno (publish zdarzenie, sprawdź efekt).
- Plus: idealne dla event-driven architecture.
- Minus: trudno przewidzieć kolejność wykonania listenerów.
- Minus: błąd jednego listenera może wpłynąć na innych — trzeba odpowiednio obsłużyć wyjątki.
- Minus: trudne debugowanie — „kto słucha tego eventu?" wymaga przeszukania kodu.
- Minus: memory leak — niesprzątane subskrypcje (klasyczny problem w GUI).

**Teoria w pigułce:**
Publisher (subject) trzyma listę listenerów. Listener implementuje wspólny interfejs (jedną metodę). Metoda `publish(event)` iteruje po listenerach i woła `listener.onEvent(event)`. Każdy listener pakuje try/catch, by jego błąd nie zatrzymał innych. W Javie historycznie używano `Observable`/`Observer` (deprecated w Java 9) oraz `PropertyChangeSupport` z `java.beans` — wciąż używany w Swing i JavaFX.

### Kod krok-po-kroku — implementacja (ręczny EventBus)

Utwórz plik: `src/main/java/com/example/wzorce/observer/OrderEvent.java`

```java
package com.example.wzorce.observer;

/** Sealed interface — zamknięty zestaw eventów. */
public sealed interface OrderEvent permits OrderPlaced, OrderCancelled, OrderShipped {
    String orderId();
}
```

Utwórz plik: `src/main/java/com/example/wzorce/observer/OrderPlaced.java`

```java
package com.example.wzorce.observer;

public record OrderPlaced(String orderId, String customerId, double total)
        implements OrderEvent {}
```

Utwórz plik: `src/main/java/com/example/wzorce/observer/OrderCancelled.java`

```java
package com.example.wzorce.observer;

public record OrderCancelled(String orderId, String reason)
        implements OrderEvent {}
```

Utwórz plik: `src/main/java/com/example/wzorce/observer/OrderShipped.java`

```java
package com.example.wzorce.observer;

public record OrderShipped(String orderId, String trackingNumber)
        implements OrderEvent {}
```

Utwórz plik: `src/main/java/com/example/wzorce/observer/OrderEventListener.java`

```java
package com.example.wzorce.observer;

@FunctionalInterface
public interface OrderEventListener {
    void onEvent(OrderEvent event);
}
```

Utwórz plik: `src/main/java/com/example/wzorce/observer/OrderEventBus.java`

```java
package com.example.wzorce.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * EventBus — ręczna implementacja Observer.
 *
 * CopyOnWriteArrayList chroni przed ConcurrentModificationException,
 * gdy listener rejestruje/wyrejestrowuje się w trakcie publish().
 *
 * Try/catch wokół każdego listenera — błąd jednego NIE zatrzymuje pozostałych.
 */
public class OrderEventBus {

    private final List<OrderEventListener> listeners = new CopyOnWriteArrayList<>();

    public void subscribe(OrderEventListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(OrderEventListener listener) {
        listeners.remove(listener);
    }

    public void publish(OrderEvent event) {
        System.out.printf("[BUS] publikuję %s [%s]%n",
                event.getClass().getSimpleName(), event.orderId());
        for (OrderEventListener listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                System.err.printf("[BUS] Listener %s rzucił błąd: %s%n",
                        listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
}
```

### Kod krok-po-kroku — implementacja (PropertyChangeSupport)

Utwórz plik: `src/main/java/com/example/wzorce/observer/pcs/StockItem.java`

```java
package com.example.wzorce.observer.pcs;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Klasyczny mechanizm Observer w JDK — PropertyChangeSupport.
 * Używany w Swing/JavaFX dla powiadamiania o zmianach pól.
 *
 * NIE wymaga ręcznej implementacji listy listenerów — JDK to obsługuje.
 */
public class StockItem {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private int quantity;
    private double price;

    public StockItem(int quantity, double price) {
        this.quantity = quantity;
        this.price = price;
    }

    public void setQuantity(int newQty) {
        int oldQty = this.quantity;
        this.quantity = newQty;
        pcs.firePropertyChange("quantity", oldQty, newQty);
    }

    public void setPrice(double newPrice) {
        double oldPrice = this.price;
        this.price = newPrice;
        pcs.firePropertyChange("price", oldPrice, newPrice);
    }

    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/observer/Main.java`

```java
package com.example.wzorce.observer;

import com.example.wzorce.observer.pcs.StockItem;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== Ręczny EventBus ===\n");
        OrderEventBus bus = new OrderEventBus();

        // 3 listenery — emailowy, magazyn, audyt
        bus.subscribe(event -> {
            if (event instanceof OrderPlaced op) {
                System.out.printf("[EMAIL] Potwierdzenie do %s na kwotę %.2f%n",
                        op.customerId(), op.total());
            }
        });

        bus.subscribe(event -> {
            if (event instanceof OrderPlaced op) {
                System.out.println("[WAREHOUSE] Rezerwuję towary dla " + op.orderId());
            } else if (event instanceof OrderCancelled oc) {
                System.out.printf("[WAREHOUSE] Zwalniam %s (powód: %s)%n",
                        oc.orderId(), oc.reason());
            }
        });

        bus.subscribe(event -> System.out.println("[AUDIT] " + event));

        // Listener z błędem — pozostali muszą i tak dostać event
        bus.subscribe(event -> {
            if (event.orderId().startsWith("BAD")) {
                throw new RuntimeException("Symulowany błąd listenera!");
            }
        });

        bus.publish(new OrderPlaced("ORD-001", "CUST-42", 199.99));
        System.out.println();
        bus.publish(new OrderCancelled("ORD-002", "brak płatności"));
        System.out.println();
        bus.publish(new OrderPlaced("BAD-001", "CUST-99", 50.00));  // wywołuje błąd
        System.out.println();
        bus.publish(new OrderShipped("ORD-001", "INPOST-99887766"));

        System.out.println("\n=== PropertyChangeSupport (JavaBeans) ===\n");
        StockItem laptop = new StockItem(10, 4299.00);

        laptop.addPropertyChangeListener(evt ->
                System.out.printf("[ZMIANA] %s: %s → %s%n",
                        evt.getPropertyName(), evt.getOldValue(), evt.getNewValue()));

        // Listener tylko dla ceny
        laptop.addPropertyChangeListener("price", evt ->
                System.out.println("[ALERT CENA] zmiana ceny na " + evt.getNewValue()));

        laptop.setQuantity(8);
        laptop.setPrice(3999.00);
        laptop.setQuantity(8);   // bez zmiany — PCS sprawdza i NIE woła listenerów
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj kod i uruchom. Powinieneś zobaczyć kilka publikacji eventów i kilka zmian property change.
2. Zwróć uwagę na obsługę wyjątków — listener który rzuca `RuntimeException` jest logowany, ale **nie zatrzymuje** pozostałych. To kluczowa właściwość EventBus.
3. Dodaj 4. listener dla `OrderShipped` — wysyła SMS z numerem trackingowym.
4. PropertyChangeSupport: ustaw `laptop.setQuantity(8)` dwa razy z rzędu — drugi raz NIE wywoła listenera (PCS porównuje stare i nowe). To wbudowana optymalizacja.
5. Eksperyment: napisz drugi listener przez `pattern matching switch` (Java 21+):
   ```java
   bus.subscribe(event -> {
       String msg = switch (event) {
           case OrderPlaced op    -> "Złożono: " + op.orderId();
           case OrderCancelled oc -> "Anulowano: " + oc.orderId();
           case OrderShipped os   -> "Wysłano: " + os.orderId();
       };
       System.out.println("[MULTI] " + msg);
   });
   ```
6. Memory leak: jeśli zarejestrujesz listenera i nigdy go nie wyrejestrujesz, EventBus trzyma referencję. Jeśli listener trzyma referencję do dużego obiektu (np. okna GUI), GC nigdy go nie wyczyści. Sprawdź — to klasyczny problem w Swing/JavaFX.
7. Anti-pattern: synchroniczne wywoływanie listenerów blokuje publisher do końca pętli. Dla wielu wolnych listenerów rozważ async (np. `ExecutorService.submit(...)`).

### Pytania kontrolne

1. Wytłumacz, dlaczego publisher (`OrderService`) nie powinien znać konkretnych listenerów.
2. Dlaczego pętla `publish` ma try/catch wokół każdego listenera? Co się stanie bez tego?
3. Co to jest `CopyOnWriteArrayList` i dlaczego pasuje do EventBus?
4. Czym `PropertyChangeSupport` różni się od ręcznego EventBus? Gdzie używałbyś każdego?
5. Wymień klasyczny problem memory leak w Observer i jak go uniknąć.
6. Jak Observer ma się do event-driven architecture i message brokerów (Kafka, RabbitMQ)?
7. Jak przetestować, że listener X obsługuje event Y?
8. Co odróżnia Observer od Mediator? (Wskazówka: Mediator wie o wszystkich, Observer nie.)

---

## 13. Wzorzec Command (z Undo)

**Cel:** Zapakować żądanie jako obiekt — z możliwością cofania (`undo`), kolejkowania, logowania, ponawiania. Nauczysz się klasycznego edytora tekstu z `undo`/`redo` oraz porównasz Command z `Runnable`.

**Kategoria:** Behawioralny

**Kiedy stosować (problem):**
Edytor tekstu musi obsługiwać `Ctrl+Z` (undo). Każda zmiana tekstu, formatowania, kopiowania musi pamiętać **jak ją cofnąć**. Naiwne podejście — globalna logika cofania w klasie `Editor` — eksploduje przy każdym nowym typie operacji.

**Command** opakowuje operację w obiekt z dwiema metodami: `execute()` i `undo()`. Edytor trzyma stos wykonanych komend — przy `Ctrl+Z` zdejmuje ostatnią i woła jej `undo()`.

Inne biznesowe przykłady: kolejka zadań do wykonania (z możliwością priorytetu, retry), makra (sekwencja komend), system płatności z auto-rollbackiem, transakcje, GUI buttons (każdy ma swoją Action = Command).

**Konsekwencje (zalety i wady):**

- Plus: encapsulacja operacji jako obiektu — można przekazać, serializować, kolejkować.
- Plus: undo/redo „za darmo" jeśli każda komenda implementuje `undo()`.
- Plus: makra (sekwencja komend) łatwe do implementacji.
- Plus: separacja „co" (komenda) od „kiedy/kto" (invoker).
- Minus: dużo klas — dla prostego CRUD-a to overkill.
- Minus: stan dla undo trzeba ręcznie zapamiętać w komendzie.
- Minus: serializacja komend wymaga rozważenia ich składowych.

**Teoria w pigułce:**
Interfejs `Command` ma `execute()` (i opcjonalnie `undo()`). Konkretna komenda trzyma referencję do **receivera** (obiektu, na którym wykonuje operację) i wszystkich potrzebnych argumentów. **Invoker** trzyma komendy (np. stos do undo) i woła ich metody. Klient tworzy komendy i podpina je do invokera. `Runnable` jest klasycznym Command bez `undo` — `executor.submit(runnable)` to wzorzec.

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/command/Command.java`

```java
package com.example.wzorce.command;

/** Wszystkie komendy mają tę samą sygnaturę. */
public interface Command {
    void execute();
    void undo();
}
```

Utwórz plik: `src/main/java/com/example/wzorce/command/TextDocument.java`

```java
package com.example.wzorce.command;

/** RECEIVER — obiekt, na którym komendy operują. */
public class TextDocument {

    private final StringBuilder content = new StringBuilder();

    public void insert(int position, String text) {
        content.insert(position, text);
    }

    public void delete(int from, int to) {
        content.delete(from, to);
    }

    public String getContent()  { return content.toString(); }
    public int length()         { return content.length(); }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/command/InsertCommand.java`

```java
package com.example.wzorce.command;

public class InsertCommand implements Command {

    private final TextDocument doc;
    private final int position;
    private final String text;

    public InsertCommand(TextDocument doc, int position, String text) {
        this.doc = doc;
        this.position = position;
        this.text = text;
    }

    @Override
    public void execute() {
        doc.insert(position, text);
    }

    @Override
    public void undo() {
        doc.delete(position, position + text.length());
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/command/DeleteCommand.java`

```java
package com.example.wzorce.command;

public class DeleteCommand implements Command {

    private final TextDocument doc;
    private final int from;
    private final int to;
    private String deletedText;   // zapamiętujemy do undo

    public DeleteCommand(TextDocument doc, int from, int to) {
        this.doc = doc;
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute() {
        deletedText = doc.getContent().substring(from, to);
        doc.delete(from, to);
    }

    @Override
    public void undo() {
        if (deletedText != null) {
            doc.insert(from, deletedText);
        }
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/command/MacroCommand.java`

```java
package com.example.wzorce.command;

import java.util.List;
import java.util.ArrayList;

/**
 * MAKRO — composite Command. Sekwencja komend traktowana jako jedna.
 * Undo cofa komendy w ODWROTNEJ kolejności.
 */
public class MacroCommand implements Command {

    private final List<Command> commands;

    public MacroCommand(List<Command> commands) {
        this.commands = new ArrayList<>(commands);
    }

    @Override
    public void execute() {
        for (Command c : commands) c.execute();
    }

    @Override
    public void undo() {
        // od końca!
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/command/EditorInvoker.java`

```java
package com.example.wzorce.command;

import java.util.Deque;
import java.util.ArrayDeque;

/**
 * INVOKER — trzyma stosy undo/redo.
 * Klient woła execute(cmd), później undo() / redo().
 */
public class EditorInvoker {

    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();

    public void execute(Command cmd) {
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear();   // nowa akcja invalidates redo
    }

    public boolean undo() {
        if (undoStack.isEmpty()) return false;
        Command cmd = undoStack.pop();
        cmd.undo();
        redoStack.push(cmd);
        return true;
    }

    public boolean redo() {
        if (redoStack.isEmpty()) return false;
        Command cmd = redoStack.pop();
        cmd.execute();
        undoStack.push(cmd);
        return true;
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/command/Main.java`

```java
package com.example.wzorce.command;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        TextDocument doc = new TextDocument();
        EditorInvoker editor = new EditorInvoker();

        editor.execute(new InsertCommand(doc, 0, "Hello"));
        System.out.println("[1] " + doc.getContent());

        editor.execute(new InsertCommand(doc, 5, " World"));
        System.out.println("[2] " + doc.getContent());

        editor.execute(new InsertCommand(doc, 11, "!"));
        System.out.println("[3] " + doc.getContent());

        editor.undo();
        System.out.println("[undo] " + doc.getContent());

        editor.undo();
        System.out.println("[undo] " + doc.getContent());

        editor.redo();
        System.out.println("[redo] " + doc.getContent());

        // Makro — wstaw + skasuj jako jedna operacja
        System.out.println("\n=== Makro ===");
        Command macro = new MacroCommand(List.of(
                new InsertCommand(doc, doc.length(), " [TEMP]"),
                new DeleteCommand(doc, 0, 5)
        ));
        editor.execute(macro);
        System.out.println("[macro] " + doc.getContent());

        editor.undo();   // cofnij całe makro
        System.out.println("[undo macro] " + doc.getContent());
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj wszystko i uruchom. Powinieneś zobaczyć stopniowo budowany tekst „Hello World!", undo cofa znaki, redo przywraca, makro robi 2 operacje razem.
2. Zauważ: makro `undo()` cofa w ODWROTNEJ kolejności. To kluczowe — bez tego stan się rozjeżdża.
3. Dodaj komendę `ReplaceCommand(doc, from, to, newText)`. W `execute()` zapamiętaj usuwany tekst, w `undo()` przywróć.
4. Wykonaj 5 komend, zrób undo, dodaj nową komendę — co się dzieje ze stosem redo? (Czyści się — to standardowe zachowanie edytorów.)
5. Sprawdź w Swing/JavaFX: każdy `JButton` ma `Action` — to Command. `Action.actionPerformed(...)` = `execute()`.
6. Sprawdź `Runnable` i `ExecutorService.submit(Runnable)` — `Runnable` to Command bez undo. ExecutorService to invoker.
7. Eksperyment: zaimplementuj komendę, której `undo()` jest niemożliwe (np. „wyślij email"). Co powinno zwrócić? (Klasyczne podejście — `throw new UnsupportedOperationException("nie da się cofnąć")` lub trzeba wcześniej zapamiętać i tylko zlogować że trzeba ręcznie napisać przeprosiny.)
8. Anti-pattern: nie wkładaj logiki biznesowej w komendę. Komenda ma tylko **wywoływać** receivera. Logika należy do receivera.

### Pytania kontrolne

1. Czym Command różni się od Strategy? Oba kapsułują „coś do zrobienia" — czym konkretnie się różnią?
2. Wymień 4 elementy wzorca Command (Receiver, Command, Invoker, Client).
3. Czemu makro (`MacroCommand`) cofa w **odwrotnej** kolejności?
4. Jak komenda zapamiętuje stan potrzebny do undo? Wymień strategię z `DeleteCommand`.
5. Jak `Runnable` ma się do Command?
6. Wymień przykład Command w GUI Javy.
7. Co się stanie, jeśli klient wywoła `undo` 100 razy z pustym stosem?
8. Kiedy NIE należy używać Command? (Wskazówka: prosty CRUD bez undo/historii.)
9. Jak Command pasuje do kolejkowania (queue) i retry?

---

## 14. Wzorzec Template Method

**Cel:** Zdefiniować **szkielet algorytmu** w klasie bazowej, pozwalając podklasom nadpisać wybrane kroki — bez zmiany struktury. Nauczysz się typowego procesu „import danych z pliku" gdzie kroki (read, parse, validate, save) są wspólne, ale konkretne implementacje (CSV, JSON, XML) różnią się tylko parsowaniem.

**Kategoria:** Behawioralny

**Kiedy stosować (problem):**
Twoja aplikacja importuje dane z różnych źródeł (CSV, JSON, XML). Każdy import ma identyczne kroki:
1. Otwórz plik.
2. Wczytaj zawartość.
3. **Sparsuj** (różne dla CSV/JSON/XML).
4. Zwaliduj dane.
5. Zapisz do bazy.
6. Zamknij plik.

Tylko **krok 3** różni się między formatami. Reszta identyczna. Naiwne podejście — duplikacja kodu w 3 klasach — narusza DRY i powoduje rozjazdy.

**Template Method** definiuje algorytm w klasie bazowej, a podklasy nadpisują tylko zmienne kroki. Jak HttpServlet.service() w servletach — szkielet jest stały, doGet/doPost nadpisujesz.

Inne biznesowe przykłady: workflow zatwierdzania (rezerwacja → płatność → wysyłka), generator raportów (header → body → footer), framework testowy (setUp → test → tearDown).

**Konsekwencje (zalety i wady):**

- Plus: eliminacja duplikacji — wspólne kroki w jednym miejscu.
- Plus: podklasy implementują tylko **różnicę**.
- Plus: gwarantowana kolejność wywołań.
- Plus: hooki — opcjonalne kroki, które podklasa może (ale nie musi) nadpisać.
- Minus: silne sprzężenie podklasa-klasa bazowa.
- Minus: hierarchia dziedziczenia może rosnąć i być trudna do zrozumienia.
- Minus: zmiana algorytmu w klasie bazowej wpływa na wszystkie podklasy.
- Minus: trudne do testowania w izolacji — podklasa wymaga klasy bazowej.

**Teoria w pigułce:**
Klasa abstrakcyjna definiuje metodę `templateMethod()` — najczęściej `final`, by podklasy jej nie nadpisywały. Wewnątrz wywołuje sekwencję kroków: niektóre są zaimplementowane (wspólne), inne są abstrakcyjne (do nadpisania), jeszcze inne to **hooki** (mają puste implementacje, podklasa nadpisuje jeśli chce). Podklasa implementuje tylko abstrakcyjne kroki.

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/templatemethod/DataImporter.java`

```java
package com.example.wzorce.templatemethod;

import java.util.List;

/**
 * Klasa bazowa z TEMPLATE METHOD.
 *
 * importData() to szkielet algorytmu (final — nie do nadpisania).
 * Konkretne kroki:
 *  - read() — wspólny (czytanie pliku),
 *  - parse() — abstrakcyjny (różny dla CSV/JSON/XML),
 *  - validate() — domyślna implementacja, podklasa może nadpisać,
 *  - save() — wspólny (zapis do bazy),
 *  - onError() — HOOK, pusta implementacja, podklasa nadpisuje jeśli chce.
 */
public abstract class DataImporter {

    /** TEMPLATE METHOD — final, by podklasa nie zmieniła kolejności kroków. */
    public final void importData(String filePath) {
        System.out.println("=== Start importu: " + filePath + " ===");
        try {
            String raw = read(filePath);
            List<Record> records = parse(raw);
            List<Record> valid = validate(records);
            save(valid);
            System.out.println("=== Import zakończony ===");
        } catch (Exception e) {
            onError(e);   // HOOK
            throw new RuntimeException(e);
        }
    }

    protected String read(String filePath) {
        System.out.println("[READ] otwieram " + filePath);
        // W realnej aplikacji: Files.readString(Path.of(filePath))
        return "Anna,30\nJan,25\nKasia,40";
    }

    /** ABSTRAKCYJNY — podklasa MUSI dostarczyć. */
    protected abstract List<Record> parse(String raw);

    /** DOMYŚLNA implementacja — podklasa MOŻE nadpisać. */
    protected List<Record> validate(List<Record> records) {
        System.out.println("[VALIDATE] " + records.size() + " rekordów");
        return records.stream()
                .filter(r -> r.age() >= 0 && r.age() < 150)
                .toList();
    }

    protected void save(List<Record> records) {
        System.out.println("[SAVE] zapisuję " + records.size() + " rekordów do bazy");
        for (Record r : records) {
            System.out.println("  → " + r);
        }
    }

    /** HOOK — pusta domyślna implementacja, podklasa może nadpisać. */
    protected void onError(Exception e) {
        // domyślnie nic
    }

    public record Record(String name, int age) {}
}
```

Utwórz plik: `src/main/java/com/example/wzorce/templatemethod/CsvDataImporter.java`

```java
package com.example.wzorce.templatemethod;

import java.util.ArrayList;
import java.util.List;

public class CsvDataImporter extends DataImporter {

    @Override
    protected List<Record> parse(String raw) {
        System.out.println("[PARSE] CSV");
        List<Record> result = new ArrayList<>();
        for (String line : raw.split("\n")) {
            String[] cols = line.split(",");
            result.add(new Record(cols[0], Integer.parseInt(cols[1])));
        }
        return result;
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/templatemethod/JsonDataImporter.java`

```java
package com.example.wzorce.templatemethod;

import java.util.ArrayList;
import java.util.List;

public class JsonDataImporter extends DataImporter {

    /** Symulacja czytania — ten importer dostaje JSON. */
    @Override
    protected String read(String filePath) {
        System.out.println("[READ JSON] " + filePath);
        return "[{\"name\":\"Anna\",\"age\":30},{\"name\":\"Jan\",\"age\":25}]";
    }

    @Override
    protected List<Record> parse(String raw) {
        System.out.println("[PARSE] JSON (uproszczone)");
        List<Record> result = new ArrayList<>();
        // Bardzo uproszczony „parser" JSON dla demonstracji
        String stripped = raw.replaceAll("[\\[\\]{}\"]", "");
        for (String entry : stripped.split(",(?=name:)")) {
            String[] parts = entry.split(",");
            String name = parts[0].split(":")[1];
            int age = Integer.parseInt(parts[1].split(":")[1]);
            result.add(new Record(name, age));
        }
        return result;
    }

    /** Nadpisany HOOK — pokazujemy że można dodać własną logikę błędu. */
    @Override
    protected void onError(Exception e) {
        System.err.println("[JSON IMPORTER] Specjalna obsługa błędu: " + e.getMessage());
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/templatemethod/XmlDataImporter.java`

```java
package com.example.wzorce.templatemethod;

import java.util.ArrayList;
import java.util.List;

public class XmlDataImporter extends DataImporter {

    @Override
    protected String read(String filePath) {
        System.out.println("[READ XML] " + filePath);
        return "<users><user name='Anna' age='30'/><user name='Jan' age='25'/></users>";
    }

    @Override
    protected List<Record> parse(String raw) {
        System.out.println("[PARSE] XML (uproszczone)");
        List<Record> result = new ArrayList<>();
        // Uproszczony parser XML dla demo
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(
                "<user name='([^']+)' age='(\\d+)'/>");
        java.util.regex.Matcher m = p.matcher(raw);
        while (m.find()) {
            result.add(new Record(m.group(1), Integer.parseInt(m.group(2))));
        }
        return result;
    }

    /** Nadpisana walidacja — XML wymaga dodatkowo sprawdzenia długości nazwy. */
    @Override
    protected List<Record> validate(List<Record> records) {
        List<Record> base = super.validate(records);
        return base.stream().filter(r -> r.name().length() >= 2).toList();
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/templatemethod/Main.java`

```java
package com.example.wzorce.templatemethod;

public class Main {
    public static void main(String[] args) {

        DataImporter csv  = new CsvDataImporter();
        DataImporter json = new JsonDataImporter();
        DataImporter xml  = new XmlDataImporter();

        csv.importData("data.csv");
        System.out.println();
        json.importData("data.json");
        System.out.println();
        xml.importData("data.xml");
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj wszystko i uruchom. Trzy bloki — każdy z innym parserem, ale identyczną sekwencją kroków.
2. Zauważ: `importData(...)` jest wywoływane na 3 różnych importerach, ale **kolejność** (READ → PARSE → VALIDATE → SAVE) jest taka sama. To gwarancja template method.
3. Spróbuj nadpisać `importData(...)` w `CsvDataImporter`. Kompilator nie pozwoli — bo `final` w klasie bazowej. To celowe.
4. Dodaj 4. importer — `YamlDataImporter`. Implementuj tylko `parse()`. Zauważ ile zysku — używasz całej infrastruktury bazowej.
5. Dodaj nowy hook — `protected void afterSave(List<Record> records) {}` — pusta implementacja. W `CsvDataImporter` nadpisz, by wysłać email z liczbą rekordów. Inne importer NIE muszą tego implementować.
6. Sprawdź w JDK: `HttpServlet.service(...)` to template method — wewnętrznie woła `doGet`, `doPost` itp. zależnie od metody HTTP. Otwórz w IDE.
7. Sprawdź `AbstractList` — implementuje większość metod `List` przez `get(int)` i `size()`. Twoja podklasa musi nadpisać tylko te dwie — reszta (iteracja, subList, containsAll) działa.
8. Anti-pattern: jeśli każda podklasa nadpisuje WSZYSTKIE kroki — to nie template method, to dziedziczenie nadużyte. Sygnał: zerowa korzyść z klasy bazowej.

### Pytania kontrolne

1. Co znaczy „template method" — czy to klasa, metoda, czy oba?
2. Dlaczego metoda template (np. `importData`) jest często `final`?
3. Co to jest **hook** w Template Method? Podaj przykład z naszego kodu.
4. Czym Template Method różni się od Strategy? Oba pozwalają „zmienić algorytm" — gdzie różnica?
5. Wymień klasyczny przykład Template Method w JDK (`HttpServlet`, `AbstractList`).
6. Czy podklasa może zmienić **kolejność** kroków w algorytmie? Dlaczego nie?
7. Jak Template Method ma się do zasady „kompozycja > dziedziczenie"? (Wskazówka: tu dziedziczenie jest wymagane.)
8. Co odróżnia metodę `abstract` od metody z domyślną implementacją (hook) w klasie bazowej?

---

## 15. Wzorzec Iterator (własny + integracja z Iterable)

**Cel:** Przejść po elementach kolekcji **bez wystawiania jej wewnętrznej struktury**. Nauczysz się pisać własny `Iterator` i `Iterable` oraz zintegrować swoją kolekcję z pętlą `for-each`.

**Kategoria:** Behawioralny

**Kiedy stosować (problem):**
Masz własną strukturę danych — drzewo, graf, listę kolistą. Klient chce przejść po elementach: `for (X x : moja)`. Jeśli wystawisz wewnętrzne tablice/listy, klient zna szczegóły implementacji (encapsulation broken). Iterator daje klientowi kontrolowany dostęp do elementów bez pokazania, że twoja kolekcja to np. drzewo AVL.

**W Javie cały świat kolekcji** to Iterator. `for (X x : list)` to dosłownie `Iterator<X> it = list.iterator(); while (it.hasNext()) { X x = it.next(); ... }`. Każda klasa implementująca `Iterable<T>` może być w for-each.

Inne biznesowe przykłady: paginowane API (kursor next page), strumień zdarzeń (kolejny event z bus), feed RSS (kolejny artykuł).

**Konsekwencje (zalety i wady):**

- Plus: encapsulation — klient nie zna struktury danych.
- Plus: jedno API (`hasNext`/`next`) dla wszystkich kolekcji.
- Plus: można mieć kilka iteratorów dla tej samej kolekcji.
- Plus: integracja z for-each, Stream, Collection.
- Minus: prosty iterator nie obsługuje równoczesnych zmian (`ConcurrentModificationException`).
- Minus: dla bardzo prostych kolekcji to overkill — wystaw `List<X>` i koniec.
- Minus: trudniejsze niż for(int i; i < n; i++) dla kogoś, kto nie ufał wzorcom.

**Teoria w pigułce:**
Interfejs `Iterator<T>` ma `hasNext()` i `next()`. Interfejs `Iterable<T>` ma `iterator()` — zwraca świeży iterator. Pętla for-each wymaga `Iterable`. Konwencja: każde wywołanie `iterator()` zwraca **nowy** iterator (start od początku), bo użytkownik może chcieć dwóch równoległych przejść.

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/iterator/Playlist.java`

```java
package com.example.wzorce.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;

/**
 * Własna kolekcja Playlist implementuje Iterable<String>.
 * Klient może iterować przez for-each, NIE znając wewnętrznej struktury.
 */
public class Playlist implements Iterable<String> {

    private final List<String> songs = new ArrayList<>();

    public void add(String song) { songs.add(song); }

    public int size() { return songs.size(); }

    @Override
    public Iterator<String> iterator() {
        return new PlaylistIterator();
    }

    /** Drugi iterator — shuffle (losowa kolejność bez powtórzeń). */
    public Iterator<String> shuffleIterator() {
        List<String> copy = new ArrayList<>(songs);
        java.util.Collections.shuffle(copy);
        return copy.iterator();
    }

    /** Klasa wewnętrzna — iterator po liście. */
    private class PlaylistIterator implements Iterator<String> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < songs.size();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Brak więcej utworów");
            }
            return songs.get(index++);
        }
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/iterator/RangeIterator.java`

```java
package com.example.wzorce.iterator;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Drugi przykład — iterator bez kolekcji bazowej.
 * Generuje liczby z zakresu [start, end) z krokiem.
 *
 * Pokazuje, że iterator NIE musi „chodzić" po istniejącej strukturze —
 * może generować elementy w locie.
 */
public class RangeIterator implements Iterable<Integer> {

    private final int start;
    private final int end;
    private final int step;

    public RangeIterator(int start, int end, int step) {
        if (step <= 0) throw new IllegalArgumentException("step > 0");
        this.start = start;
        this.end = end;
        this.step = step;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            int current = start;

            @Override public boolean hasNext() { return current < end; }

            @Override
            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                int value = current;
                current += step;
                return value;
            }
        };
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/iterator/Tree.java`

```java
package com.example.wzorce.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Trzeci przykład — drzewo z iteracją PRE-ORDER.
 * Pokazuje, że iterator MOŻE ukrywać złożoną strukturę
 * (rekurencja po drzewie).
 */
public class Tree<T> implements Iterable<T> {

    private final T value;
    private final List<Tree<T>> children = new ArrayList<>();

    public Tree(T value) { this.value = value; }

    public Tree<T> add(Tree<T> child) {
        children.add(child);
        return this;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            // Linearyzujemy drzewo w pre-order do kolejki
            private final java.util.Deque<Tree<T>> stack = new java.util.ArrayDeque<>();
            {
                stack.push(Tree.this);
            }

            @Override public boolean hasNext() { return !stack.isEmpty(); }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                Tree<T> node = stack.pop();
                // Dodaj dzieci w odwrotnej kolejności,
                // by pierwszego dziecka czytać jako pierwsze
                for (int i = node.children.size() - 1; i >= 0; i--) {
                    stack.push(node.children.get(i));
                }
                return node.value;
            }
        };
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/iterator/Main.java`

```java
package com.example.wzorce.iterator;

public class Main {
    public static void main(String[] args) {

        // 1) Playlist — własna kolekcja w for-each
        Playlist pl = new Playlist();
        pl.add("Beat It");
        pl.add("Smooth Criminal");
        pl.add("Thriller");

        System.out.println("=== Standardowo ===");
        for (String song : pl) {        // for-each działa, bo Iterable
            System.out.println("  " + song);
        }

        System.out.println("\n=== Shuffle (drugi iterator) ===");
        var it = pl.shuffleIterator();
        while (it.hasNext()) {
            System.out.println("  " + it.next());
        }

        // 2) RangeIterator — iterator BEZ kolekcji
        System.out.println("\n=== Range 0..10 co 2 ===");
        for (int i : new RangeIterator(0, 10, 2)) {
            System.out.print(i + " ");
        }
        System.out.println();

        // 3) Tree — iterator po drzewie w pre-order
        System.out.println("\n=== Tree pre-order ===");
        Tree<String> root = new Tree<>("root")
                .add(new Tree<>("A")
                        .add(new Tree<>("A1"))
                        .add(new Tree<>("A2")))
                .add(new Tree<>("B"))
                .add(new Tree<>("C")
                        .add(new Tree<>("C1")));

        for (String value : root) {
            System.out.println("  " + value);
        }
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj wszystko i uruchom. Powinieneś zobaczyć playlistę w kolejności, w shuffle, range 0,2,4,6,8 i drzewo w kolejności root,A,A1,A2,B,C,C1.
2. Zauważ — `Playlist` nie wystawia `List<String>`. Klient może tylko `iterator()`, `add()`, `size()`. To encapsulation.
3. Spróbuj iterować po `Playlist` dwa razy w for-each. Każde wywołanie tworzy nowy iterator — to konwencja.
4. Eksperyment: w `RangeIterator` przekaż `step = 0`. Konstruktor rzuca wyjątek. Bez tego dostałbyś nieskończoną pętlę.
5. Sprawdź `Stream`: `StreamSupport.stream(myIterable.spliterator(), false)` — twoja kolekcja staje się strumieniem. Robi to każda kolekcja w Java.
6. Drzewo: zmień iterator na post-order (najpierw dzieci, potem rodzic). Zauważ, że klient w `Main` NIE musi się zmieniać — to siła abstrakcji iteratora.
7. Sprawdź `ConcurrentModificationException`. W `Playlist` dodaj utwór W TRAKCIE iteracji przez for-each. `ArrayList.iterator()` w JDK rzuca CME. Twój iterator nie sprawdza — co byś dodał? (Wskazówka: `modCount` w klasie nadrzędnej.)
8. Anti-pattern: nadawanie metodzie `iterator()` skutków ubocznych (np. resetowanie stanu). Konwencja: iterator() to **świeży** iterator od początku, nic więcej.

### Pytania kontrolne

1. Wymień dwa interfejsy potrzebne, by twoja kolekcja działała w for-each.
2. Jakie dwie metody ma `Iterator<T>`? Co każda robi?
3. Dlaczego konwencja mówi „każde wywołanie `iterator()` to nowy, świeży iterator"?
4. Jak iterator może ukrywać złożoną strukturę (drzewo)? Co robi `Tree.iterator()`?
5. Czy iterator musi wskazywać na istniejącą strukturę? (Wskazówka: `RangeIterator` generuje w locie.)
6. Co to jest `ConcurrentModificationException` i kiedy się pojawia?
7. Jak iterator współgra ze Stream w Javie?
8. Czy `for (int i = 0; i < list.size(); i++)` to też Iterator? (Wskazówka: nie, to pętla indeksowana. Iterator nie zna „indeksu".)
9. Wymień klasyczną sytuację, w której musisz napisać własny Iterator (a nie użyć kolekcji JDK).

---

## 16. Wzorzec State

**Cel:** Zmieniać zachowanie obiektu w zależności od jego **stanu wewnętrznego** — bez gigantycznych drabinek `if/else` lub `switch`. Nauczysz się modelować zamówienie z fazami (NOWE → POTWIERDZONE → WYSŁANE → DOSTARCZONE → ANULOWANE).

**Kategoria:** Behawioralny

**Kiedy stosować (problem):**
Klasa `Order` ma metody: `pay()`, `ship()`, `deliver()`, `cancel()`. Każda metoda zachowuje się inaczej w zależności od stanu zamówienia:
- `pay()` w stanie NOWE — przechodzi do POTWIERDZONE.
- `pay()` w stanie POTWIERDZONE — błąd, już zapłacone.
- `ship()` w stanie POTWIERDZONE — przejście do WYSŁANE.
- `ship()` w stanie NOWE — błąd, najpierw zapłać.

Naiwne podejście — `switch(state)` w każdej metodzie — daje matrycę N stanów × M metod = N·M gałęzi w jednej klasie. Trudno śledzić. Dodanie nowego stanu wymaga modyfikacji wszystkich metod.

**State** wydziela każdy stan do osobnej klasy. `Order` deleguje do `currentState.pay()`, a stan sam wie, jak zareagować i do jakiego stanu przejść.

Biznesowe przykłady: workflow zamówień, kontroler stanu maszyny stanów (FSM), gracz audio (PLAY/PAUSE/STOP), thread state (NEW/RUNNABLE/BLOCKED/WAITING/TERMINATED).

**Konsekwencje (zalety i wady):**

- Plus: każdy stan w osobnej klasie — łatwe testowanie.
- Plus: dodanie nowego stanu = nowa klasa, brak modyfikacji innych.
- Plus: brak gigantycznych switchów — kod czytelny.
- Plus: przejścia między stanami widoczne w kodzie stanu.
- Minus: dużo klas — dla 2 stanów to overkill.
- Minus: stan musi znać context (cykliczna zależność).
- Minus: trudniejsze do prześledzenia diagramu — trzeba przeskakiwać między plikami.
- Minus: pomyłka z Strategy — oba „wymieniają zachowanie", ale State zmienia się sam, Strategy ustawia klient.

**Teoria w pigułce:**
`Context` (np. `Order`) trzyma referencję do `State`. Wywołanie `context.method()` deleguje do `state.method(context)`. Stan wykonuje swoją logikę i **może** zmienić stan contextu (`context.setState(newState)`). Klient zna tylko Context — nie zna konkretnych stanów.

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/state/OrderState.java`

```java
package com.example.wzorce.state;

/**
 * Wspólny interfejs dla wszystkich stanów zamówienia.
 * Każda metoda dostaje context (Order), by móc zmienić stan.
 */
public interface OrderState {
    void pay(Order order);
    void ship(Order order);
    void deliver(Order order);
    void cancel(Order order);
    String name();
}
```

Utwórz plik: `src/main/java/com/example/wzorce/state/NewState.java`

```java
package com.example.wzorce.state;

public class NewState implements OrderState {

    @Override
    public void pay(Order order) {
        System.out.println("Zamówienie opłacone — przejście do POTWIERDZONE");
        order.setState(new ConfirmedState());
    }

    @Override
    public void ship(Order order) {
        throw new IllegalStateException("Nie można wysłać — najpierw zapłać");
    }

    @Override
    public void deliver(Order order) {
        throw new IllegalStateException("Nie można dostarczyć — najpierw zapłać i wyślij");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Anulowano zamówienie NOWE");
        order.setState(new CancelledState());
    }

    @Override public String name() { return "NOWE"; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/state/ConfirmedState.java`

```java
package com.example.wzorce.state;

public class ConfirmedState implements OrderState {

    @Override
    public void pay(Order order) {
        throw new IllegalStateException("Już opłacone");
    }

    @Override
    public void ship(Order order) {
        System.out.println("Zamówienie wysłane");
        order.setState(new ShippedState());
    }

    @Override
    public void deliver(Order order) {
        throw new IllegalStateException("Najpierw wyślij");
    }

    @Override
    public void cancel(Order order) {
        System.out.println("Anulowano POTWIERDZONE — uruchamiam refund");
        order.setState(new CancelledState());
    }

    @Override public String name() { return "POTWIERDZONE"; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/state/ShippedState.java`

```java
package com.example.wzorce.state;

public class ShippedState implements OrderState {

    @Override
    public void pay(Order order) {
        throw new IllegalStateException("Już opłacone");
    }

    @Override
    public void ship(Order order) {
        throw new IllegalStateException("Już wysłane");
    }

    @Override
    public void deliver(Order order) {
        System.out.println("Zamówienie dostarczone do klienta");
        order.setState(new DeliveredState());
    }

    @Override
    public void cancel(Order order) {
        throw new IllegalStateException("Nie można anulować — już wysłane");
    }

    @Override public String name() { return "WYSŁANE"; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/state/DeliveredState.java`

```java
package com.example.wzorce.state;

public class DeliveredState implements OrderState {

    @Override
    public void pay(Order order)     { throw new IllegalStateException("Już dostarczone"); }
    @Override
    public void ship(Order order)    { throw new IllegalStateException("Już dostarczone"); }
    @Override
    public void deliver(Order order) { throw new IllegalStateException("Już dostarczone"); }
    @Override
    public void cancel(Order order)  { throw new IllegalStateException("Nie można anulować po dostawie"); }
    @Override public String name()   { return "DOSTARCZONE"; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/state/CancelledState.java`

```java
package com.example.wzorce.state;

public class CancelledState implements OrderState {

    @Override
    public void pay(Order order)     { throw new IllegalStateException("Zamówienie anulowane"); }
    @Override
    public void ship(Order order)    { throw new IllegalStateException("Zamówienie anulowane"); }
    @Override
    public void deliver(Order order) { throw new IllegalStateException("Zamówienie anulowane"); }
    @Override
    public void cancel(Order order)  { System.out.println("Już anulowane — bez zmian"); }
    @Override public String name()   { return "ANULOWANE"; }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/state/Order.java`

```java
package com.example.wzorce.state;

/**
 * CONTEXT — trzyma stan, deleguje wywołania do stanu.
 * Klient woła order.pay(), order.ship() itd. — order nie ma logiki,
 * tylko deleguje do currentState.
 */
public class Order {

    private final String id;
    private OrderState state;

    public Order(String id) {
        this.id = id;
        this.state = new NewState();   // domyślny stan
    }

    public void pay()     { state.pay(this); }
    public void ship()    { state.ship(this); }
    public void deliver() { state.deliver(this); }
    public void cancel()  { state.cancel(this); }

    /** Tylko stany same się sobą manipulują. */
    void setState(OrderState newState) {
        System.out.printf("[STATE] %s: %s → %s%n", id, state.name(), newState.name());
        this.state = newState;
    }

    public String getId()       { return id; }
    public String stateName()   { return state.name(); }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/state/Main.java`

```java
package com.example.wzorce.state;

public class Main {
    public static void main(String[] args) {

        Order order = new Order("ORD-001");
        System.out.println("Stan: " + order.stateName());

        order.pay();
        System.out.println("Stan: " + order.stateName());

        order.ship();
        System.out.println("Stan: " + order.stateName());

        order.deliver();
        System.out.println("Stan: " + order.stateName());

        // Próba błędna — nie można płacić po dostawie
        try {
            order.pay();
        } catch (IllegalStateException e) {
            System.out.println("Błąd: " + e.getMessage());
        }

        System.out.println("\n=== Drugi przykład — anulowanie ===");
        Order order2 = new Order("ORD-002");
        order2.pay();
        order2.cancel();
        System.out.println("Stan: " + order2.stateName());

        // Już anulowanego nie można wysłać
        try {
            order2.ship();
        } catch (IllegalStateException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj wszystko i uruchom. Pierwsze zamówienie przechodzi pełen cykl, drugie zostaje anulowane przed wysyłką.
2. Zauważ — w klasie `Order` NIE ma ANI jednego if/else o stanie. Cała logika żyje w klasach stanów.
3. Dodaj nowy stan: `RefundedState` (po refundzie). W `CancelledState.pay()` — jeśli było wcześniej opłacone — przejdź do `RefundedState` zamiast `CancelledState`. Zauważ — modyfikacja w klasie stanu, nie w `Order`.
4. Refaktor: zmień stany na **enum** z metodami abstrakcyjnymi — Java pozwala. Każda wartość enuma to stan, metody są nadpisywane per wartość. Mniej klas, mniej plików. Ale trudniej rozszerzać.
5. Test każdego stanu osobno: napisz test `NewStateTest` który sprawdza, że `ship()` rzuca wyjątek, `pay()` zmienia stan, `cancel()` zmienia stan. Każdy stan = osobny test = łatwa weryfikacja.
6. Sprawdź `Thread.State` w JDK — to enum z wartościami `NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED`. To enumeracja stanów wątku — Java używa State w swoim API.
7. Anti-pattern: State bez przejść — wszystkie metody zawsze działają, brak `throw`. Wtedy to nie State — to Strategy. State = ma reguły przejść.

### Pytania kontrolne

1. Czym State różni się od Strategy? Oba „wymieniają zachowanie" — gdzie różnica?
2. Dlaczego context (`Order`) nie ma ani jednego `if/else` w metodach `pay/ship`?
3. Kto decyduje o przejściu między stanami — Context czy State?
4. Wyjaśnij, dlaczego setter `setState(...)` w `Order` jest `package-private`, a nie `public`.
5. Wymień przykład State w JDK (`Thread.State`).
6. Jak State pomaga w testowaniu? (Wskazówka: każdy stan testowany osobno, bez przygotowania całej historii.)
7. Czy `RefundedState.pay()` powinien zwracać do `RefundedState` czy do nowego stanu „refundowane drugi raz"? (Filozofia projektu — od ciebie zależy.)
8. Kiedy NIE używać State? (Wskazówka: dla 2 stanów wystarczy boolean, dla 3 stanów ze stabilną logiką — enum.)

---

## 17. Wzorzec Chain of Responsibility

**Cel:** Przepuszczać żądanie przez **łańcuch handlerów**, w którym każdy może zdecydować: obsłużyć żądanie, przekazać dalej, lub odrzucić. Nauczysz się modelować pipeline HTTP (autoryzacja → walidacja → rate limit → biznes) — dokładnie ten sam wzorzec, którego używają nowoczesne frameworki webowe.

**Kategoria:** Behawioralny

**Kiedy stosować (problem):**
Twoje API musi obsłużyć żądanie HTTP w kilku etapach: autoryzacja, walidacja Content-Type, rate limiting, logowanie, logika biznesowa. Naiwne podejście — wszystko w jednej metodzie z `if/return`:

```java
void handle(HttpRequest req) {
    if (!isAuthorized(req)) { reject(401); return; }
    if (!isJson(req))       { reject(415); return; }
    if (rateExceeded(req))  { reject(429); return; }
    // ... + 5 innych etapów
    process(req);
}
```

Każdy nowy etap = zmiana tej metody. Trudna konfiguracja per-endpoint (różne łańcuchy dla różnych URL). Trudne testowanie.

**Chain of Responsibility** dzieli każdy etap na osobny handler i łączy je w łańcuch. Klient woła pierwszy handler, on przetwarza i (opcjonalnie) deleguje dalej.

Inne biznesowe przykłady: pipeline middleware, Servlet Filters, drabinka aprobat (kierownik → dyrektor → zarząd), walidacja formularzy, logger z kilkoma appenderami.

**Konsekwencje (zalety i wady):**

- Plus: każdy handler robi jedną rzecz (SRP).
- Plus: łańcuch konfigurowalny w runtime (różny per endpoint).
- Plus: dodanie nowego etapu = nowa klasa + jedna linia setNext.
- Plus: każdy handler testowalny osobno.
- Minus: trudno śledzić, gdzie żądanie się zatrzymało.
- Minus: handler, który zapomni `passToNext`, cicho zjada żądanie.
- Minus: kolejność handlerów ma znaczenie — łatwo o pomyłkę.
- Minus: dla 2 prostych warunków to overkill.

**Teoria w pigułce:**
Wspólna klasa bazowa `Handler` ma pole `next` i metodę `handle(request)`. Konkretne handlery nadpisują `handle()`: jeśli mogą obsłużyć, robią to; potem decydują, czy wywołać `next.handle(request)`. Łańcuch: `h1.setNext(h2).setNext(h3)`. Klient woła `h1.handle(request)` — reszta dzieje się automatycznie. Alternatywa: lista handlerów + iteracja (czystsza).

### Kod krok-po-kroku — implementacja

Utwórz plik: `src/main/java/com/example/wzorce/cor/HttpRequest.java`

```java
package com.example.wzorce.cor;

import java.util.Map;

public record HttpRequest(String path, String token, String body,
                          Map<String, String> headers) {

    public boolean hasToken() { return token != null && !token.isBlank(); }
    public boolean isJson()   { return "application/json".equals(headers.get("Content-Type")); }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/cor/RequestHandler.java`

```java
package com.example.wzorce.cor;

public abstract class RequestHandler {

    protected RequestHandler next;

    /** Zwraca next, by umożliwić chaining: h1.setNext(h2).setNext(h3). */
    public RequestHandler setNext(RequestHandler next) {
        this.next = next;
        return next;
    }

    public abstract void handle(HttpRequest request);

    protected void passToNext(HttpRequest request) {
        if (next != null) {
            next.handle(request);
        } else {
            System.out.println("[CHAIN] Koniec łańcucha");
        }
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/cor/AuthHandler.java`

```java
package com.example.wzorce.cor;

public class AuthHandler extends RequestHandler {

    private static final String VALID_TOKEN = "Bearer valid-token-123";

    @Override
    public void handle(HttpRequest req) {
        System.out.println("[AUTH] sprawdzam token");
        if (!req.hasToken() || !req.token().equals(VALID_TOKEN)) {
            System.out.println("[AUTH] ODRZUCONO — 401");
            return;
        }
        System.out.println("[AUTH] OK");
        passToNext(req);
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/cor/ContentTypeHandler.java`

```java
package com.example.wzorce.cor;

public class ContentTypeHandler extends RequestHandler {

    @Override
    public void handle(HttpRequest req) {
        System.out.println("[CONTENT-TYPE] sprawdzam");
        if (!req.isJson()) {
            System.out.println("[CONTENT-TYPE] ODRZUCONO — 415");
            return;
        }
        System.out.println("[CONTENT-TYPE] OK");
        passToNext(req);
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/cor/RateLimitHandler.java`

```java
package com.example.wzorce.cor;

import java.util.ArrayDeque;
import java.util.Deque;

public class RateLimitHandler extends RequestHandler {

    private final Deque<Long> recent = new ArrayDeque<>();
    private static final int LIMIT = 5;
    private static final long WINDOW_MS = 1000;

    @Override
    public synchronized void handle(HttpRequest req) {
        long now = System.currentTimeMillis();
        while (!recent.isEmpty() && now - recent.peekFirst() > WINDOW_MS) {
            recent.pollFirst();
        }
        if (recent.size() >= LIMIT) {
            System.out.println("[RATE-LIMIT] ODRZUCONO — 429");
            return;
        }
        recent.add(now);
        System.out.println("[RATE-LIMIT] OK (" + recent.size() + "/" + LIMIT + ")");
        passToNext(req);
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/cor/LoggingHandler.java`

```java
package com.example.wzorce.cor;

public class LoggingHandler extends RequestHandler {

    @Override
    public void handle(HttpRequest req) {
        System.out.printf("[LOG] %s — body: %d bajtów%n",
                req.path(), req.body() == null ? 0 : req.body().length());
        passToNext(req);
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/cor/BusinessHandler.java`

```java
package com.example.wzorce.cor;

public class BusinessHandler extends RequestHandler {

    @Override
    public void handle(HttpRequest req) {
        System.out.println("[BUSINESS] przetwarzam " + req.path());
        System.out.println("[BUSINESS] Odpowiedź: 200 OK");
        // Nie ma passToNext — to ostatni handler
    }
}
```

Utwórz plik: `src/main/java/com/example/wzorce/cor/Pipeline.java`

```java
package com.example.wzorce.cor;

import java.util.List;

/**
 * Alternatywna implementacja — lista handlerów zamiast pointerów next.
 * Często czytelniejsze, łatwo zmienić kolejność lub usunąć etap.
 */
public class Pipeline {

    @FunctionalInterface
    public interface Step {
        /** true = przekaż dalej, false = zatrzymaj. */
        boolean handle(HttpRequest req);
    }

    private final List<Step> steps;

    public Pipeline(List<Step> steps) {
        this.steps = List.copyOf(steps);
    }

    public void execute(HttpRequest req) {
        for (Step step : steps) {
            if (!step.handle(req)) return;
        }
    }
}
```

### Kod krok-po-kroku — użycie (klient)

Utwórz plik: `src/main/java/com/example/wzorce/cor/Main.java`

```java
package com.example.wzorce.cor;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        // 1) Klasyczny łańcuch z setNext
        RequestHandler auth     = new AuthHandler();
        RequestHandler logging  = new LoggingHandler();
        RequestHandler content  = new ContentTypeHandler();
        RequestHandler rateLim  = new RateLimitHandler();
        RequestHandler business = new BusinessHandler();

        // logging → auth → content → rateLim → business
        logging.setNext(auth).setNext(content).setNext(rateLim).setNext(business);

        System.out.println("=== Poprawne żądanie ===");
        logging.handle(new HttpRequest(
                "/api/orders",
                "Bearer valid-token-123",
                "{\"item\":\"laptop\"}",
                Map.of("Content-Type", "application/json")));

        System.out.println("\n=== Brak tokenu ===");
        logging.handle(new HttpRequest(
                "/api/orders", null, "{}",
                Map.of("Content-Type", "application/json")));

        System.out.println("\n=== Zły Content-Type ===");
        logging.handle(new HttpRequest(
                "/api/orders", "Bearer valid-token-123",
                "item=laptop",
                Map.of("Content-Type", "application/x-www-form-urlencoded")));

        // 2) Pipeline z listą — alternatywna implementacja
        System.out.println("\n=== Pipeline (lista) ===");
        Pipeline pipeline = new Pipeline(List.of(
                req -> { System.out.println("[P-LOG] " + req.path()); return true; },
                req -> {
                    System.out.println("[P-AUTH]");
                    return req.hasToken();
                },
                req -> {
                    System.out.println("[P-CONTENT]");
                    return req.isJson();
                },
                req -> {
                    System.out.println("[P-BUSINESS] " + req.body());
                    return true;
                }
        ));
        pipeline.execute(new HttpRequest(
                "/api/test", "Bearer x", "{\"ok\":true}",
                Map.of("Content-Type", "application/json")));
    }
}
```

### Krok po kroku (czego ma się kursant nauczyć eksperymentalnie)

1. Skopiuj wszystkie pliki, uruchom. Powinieneś zobaczyć trzy scenariusze HTTP — każdy z innym wynikiem (poprawne, brak tokenu, zły content-type) i alternatywną pipeline.
2. Zauważ — handler bez tokenu **wraca** bez `passToNext`. To wzorzec — handler decyduje, czy przekazać dalej.
3. Eksperyment: ustaw kolejność rateLim PRZED auth. Co się zmieni? (Rate limit naliczy się dla NIEZALOGOWANYCH żądań — pewnie nie chciałeś.)
4. Anti-pattern: stwórz `BadHandler`, który robi coś użytecznego, ale ZAPOMINA wywołać `passToNext`. Włącz go w środek łańcucha. Następne handlery NIE wykonają się. To klasyczny bug.
5. Dodaj 6. handler — `MetricsHandler`, mierzy czas każdego żądania. Włącz jako pierwszy.
6. Refaktor: w lambdach (Pipeline) zwróć **enum** zamiast boolean: `CONTINUE`, `STOP`, `RETRY_AFTER`. Łatwiejsze rozszerzanie semantyki.
7. *Ciekawostka, znajomość frameworka nie wymagana:* w bibliotekach zabezpieczeń typu Spring Security żądanie HTTP przechodzi przez łańcuch filtrów (`Filter` chain) — każdy filtr decyduje, czy puścić je dalej, czy zwrócić błąd. To dokładnie ten sam wzorzec.
8. Sprawdź `Servlet Filter` — to *Chain of Responsibility* wbudowany w standard Servlet API (interfejs serwerów Javy do obsługi HTTP). Otwórz interfejs `Filter` w IDE — sygnatura `doFilter(request, response, chain)` mówi sama za siebie.

### Pytania kontrolne

1. Co decyduje, czy handler obsłuży żądanie, czy przekaże dalej?
2. Co się stanie, gdy handler ZAPOMNI wywołać `passToNext`?
3. Wyjaśnij, dlaczego kolejność handlerów ma znaczenie. Podaj przykład.
4. Czym Chain of Responsibility różni się od Pipeline (lista)? Kiedy które wybrać?
5. Wymień klasyczny przykład Chain of Responsibility w bibliotece Java (Servlet Filters).
6. Jak handler może powiedzieć „nie obsługuję, ktoś inny niech tym się zajmie"?
7. Co odróżnia Chain od Decorator? Oba „opakowują" — czym?
8. Jak przetestować pojedynczy handler w izolacji?
9. Kiedy NIE używać Chain of Responsibility? (Wskazówka: dla 2 prostych warunków wystarczy if/else.)
10. Jak Chain ma się do middleware'u w nowoczesnych frameworkach webowych?

---

## Szybka ściąga — wszystkie 17 wzorców z tej karty

| Wzorzec | Kategoria | Sygnał w kodzie / problem | Sygnał, że to ANTY-WZORZEC |
| --- | --- | --- | --- |
| Singleton | Kreacyjny | Jedna instancja zasobu (config, pool, logger) | Mutowalny stan + testy = piekło |
| Factory Method | Kreacyjny | `List.of(...)`, `Optional.of(...)` | Dla 1 typu — overkill |
| Abstract Factory | Kreacyjny | Rodziny powiązanych obiektów (UI Light/Dark, drivery baz danych) | Tylko 1 typ produktu — wystarczy Factory Method |
| Builder | Kreacyjny | Klasa z wieloma opcjonalnymi polami, fluent `.x(...).y(...).build()` | 2-3 pola — konstruktor wystarczy |
| Prototype | Kreacyjny | `clone()`/`copy()`, „nowa kopia na każde żądanie" w kontenerach DI | `Cloneable` z JDK — preferuj copy constructor |
| Adapter | Strukturalny | „Most" między niezgodnymi interfejsami (`Arrays.asList`) | Identyczne interfejsy — adapter zbędny |
| Decorator | Strukturalny | `new BufferedReader(new FileReader(...))`, kawa + dodatki | Wszystkie „dekoratory" robią to samo |
| Facade | Strukturalny | Jedna prosta metoda nad złożonym podsystemem | Fasada z 50 metodami i własną logiką = god class |
| Proxy | Strukturalny | Dodawanie zachowań przekrojowych (transakcje, cache, autoryzacja), lazy loading | Wywołanie wewnętrzne `this.x()` omija proxy |
| Composite | Strukturalny | Drzewo, gdzie liść i węzeł jednakowo (file system, Swing) | Płaska struktura — wystarczy lista |
| Strategy | Behawioralny | `Map<Klucz, Strategia>`, interfejs funkcyjny + lambda | If-else „dla wyjątków" zostaje |
| Observer | Behawioralny | `subscribe/publish`, EventBus, `PropertyChangeSupport` | Niesprzątane subskrypcje = memory leak |
| Command | Behawioralny | `Runnable`, undo/redo, makra | Logika biznesowa w komendzie zamiast w receiverze |
| Template Method | Behawioralny | Algorytm z krokami do nadpisania (`HttpServlet#service`) | Podklasa nadpisuje WSZYSTKO — zerowa korzyść |
| Iterator | Behawioralny | `Iterator<T>` + `Iterable<T>`, for-each | Po prostu wystaw `List<X>` — nie pisz iteratora ręcznie |
| State | Behawioralny | `Thread.State`, maszyny stanów, workflow | Stan bez przejść = to Strategy |
| Chain of Responsibility | Behawioralny | Servlet Filters, middleware, łańcuch aprobat | Handler zapomina `passToNext` = cichy bug |

---

## Końcowe ćwiczenie zbiorcze — ZL-14

Połącz **trzy wzorce w jednym mini-projekcie** — system płatności z konfigurowalnym workflow:

1. **Strategy** — metoda płatności (KARTA, BLIK, PRZELEW). Każda strategia jako lambda.
2. **Observer** — listenery na wynik płatności (LOG, ANALYTICS, AUDIT).
3. **Chain of Responsibility** — walidacja przed płatnością (kwota > 0, klient ma konto, klient nie jest zablokowany).

Wymagania:
- klasa `PaymentService` jako fasada nad strategią + listenerami + walidatorami,
- konstruktor `PaymentService(Map<String, PaymentStrategy> methods, List<Validator> validators)`,
- metoda `onPayment(Consumer<PaymentResult> listener)` rejestruje listenera,
- metoda `pay(String method, double amount, String customerId, String description)` przechodzi przez chain walidacji, jeśli OK — wykonuje strategię, w obu przypadkach informuje listenerów.

Po zakończeniu projektu zobacz, że:
- **dodanie nowej metody płatności** = nowa lambda + jedna linia w mapie strategii,
- **dodanie nowego listenera** = jedna linia `onPayment(...)`,
- **dodanie nowej walidacji** = nowa klasa Validator + wpis do listy.

To 3 zmiany w 3 osobnych miejscach — bez modyfikacji `PaymentService`. To jest piękno wzorców projektowych: **każda zmiana w izolowanym miejscu**.

---

## Materiały dodatkowe

- Joshua Bloch — Effective Java (3rd edition), Items: 1 (Static factory methods), 2 (Builder), 3 (Singleton with enum).
- Refactoring.Guru: https://refactoring.guru/design-patterns
- Gang of Four — „Design Patterns: Elements of Reusable Object-Oriented Software" (1994)
- Baeldung — Design Patterns: https://www.baeldung.com/design-patterns-series
- Lekcja źródłowa: `29_wzorce_projektowe.md`

---

## Podsumowanie

Po przejściu wszystkich 17 wzorców powinieneś:

1. **Rozpoznawać wzorce w cudzym kodzie** — gdy widzisz `Map<Klucz, Funkcja>`, mówisz „Strategy". Gdy widzisz fluent builder, mówisz „Builder". Gdy widzisz listener listę, mówisz „Observer".
2. **Komunikować się nazwami wzorców** — „użyłem Strategy" mówi więcej niż 50 linii opisu. Twój zespół zrozumie cię w pół sekundy.
3. **Dobierać wzorce do problemu** — nie wymuszać wzorca dla samego wzorca. Wzorzec to **odpowiedź na powtarzający się problem**, nie rozwiązanie a priori.
4. **Znać anty-wzorce** — wiedzieć, gdzie kursanci nadużywają wzorca i jak temu zapobiec.
5. **Łączyć wzorce** — w realnych systemach wzorce się przeplatają. Fasada w środku ma Chain. Strategy używa Dispatch Table (rejestru). Decorator nakłada Proxy.
