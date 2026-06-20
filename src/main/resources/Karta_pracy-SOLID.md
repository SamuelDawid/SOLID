# Karta pracy — SOLID (pięć zasad dobrego projektowania)

> Karta uczy rozpoznawania naruszeń **SRP, OCP, LSP, ISP, DIP** w realnym kodzie i refaktoryzowania ich krok po kroku. Każda sekcja zawiera kod „przed", refaktor „po", instrukcję postępowania i pytania kontrolne. Część końcowa to mini-projekt **ZL-15** — refaktoryzacja God Class `UserManager`.
>
> SOLID to **filtry, przez które przepuszczasz każdą decyzję projektową** — nie checklist do odhaczenia. Stosujesz, gdy kod zaczyna się psuć (trudno testować, trudno zmienić jedną rzecz, trudno czytać). Stosowanie SOLID „na siłę" prowadzi do over-engineeringu.
---

## Słowniczek skrótów używanych w karcie

| Skrót | Pełna nazwa | Co to znaczy w skrócie |
| --- | --- | --- |
| **SOLID** | S(RP) + O(CP) + L(SP) + I(SP) + D(IP) | Akronim pięciu zasad — wyjaśnione poniżej |
| **SRP** | Single Responsibility Principle | Zasada jednej odpowiedzialności |
| **OCP** | Open/Closed Principle | Zasada otwarte/zamknięte (na rozszerzenie / na modyfikację) |
| **LSP** | Liskov Substitution Principle | Zasada podstawienia Liskov |
| **ISP** | Interface Segregation Principle | Zasada segregacji interfejsów |
| **DIP** | Dependency Inversion Principle | Zasada odwrócenia zależności |
| **DI** | Dependency Injection | Wstrzykiwanie zależności — przekazywanie obiektów przez konstruktor/setter, zamiast tworzenia ich w środku klasy |
| **YAGNI** | You Aren't Gonna Need It | „Nie będziesz tego potrzebował" — nie buduj abstrakcji na zapas |
| **DRY** | Don't Repeat Yourself | „Nie powtarzaj się" — unikaj duplikacji |
| **KISS** | Keep It Simple, Stupid | „Trzymaj to proste" |
| **NPE** | `NullPointerException` | Wyjątek rzucany, gdy próbujesz odwołać się do `null` |
| **SMTP** | Simple Mail Transfer Protocol | Protokół wysyłania emaili |
| **JDBC** | Java Database Connectivity | Standardowy interfejs Javy do baz SQL |
| **SQL** | Structured Query Language | Język zapytań do bazy danych |
| **PDF / CSV / JSON / XML** | formaty plików | PDF — dokument, CSV — wiersze z przecinkami, JSON/XML — formaty danych |
| **CRUD** | Create, Read, Update, Delete | Cztery podstawowe operacje na danych |
| **God Class** | termin slangowy | „Klasa-bóg" — klasa, która robi za dużo różnych rzeczy naraz |

---

## 0. Wprowadzenie — kiedy NIE stosować SOLID

**Cel:** Zrozumieć, że SOLID to narzędzie, nie dogmat. Rozpoznać sytuacje, w których zastosowanie zasady przynosi więcej szkody niż pożytku.

**Teoria w pigułce:**
SOLID to pięć zasad sformułowanych przez Roberta C. Martina (Uncle Bob): **S**ingle Responsibility, **O**pen/Closed, **L**iskov Substitution, **I**nterface Segregation, **D**ependency Inversion. Każda ma kontekst, w którym przestaje się opłacać — **YAGNI bije DIP**, gdy nie ma rzeczywistej potrzeby zmiany. Klasyczny anty-przykład: `interface UserService { ... }` z jedyną implementacją `UserServiceImpl` „bo DIP". Interfejs to wtedy martwy kod, który utrudnia czytanie.

### Tabela szybkiego dostępu

| Litera | Zasada | Pytanie do zadawania | Sygnał naruszenia |
| --- | --- | --- | --- |
| **S** | Single Responsibility | „Czy zmiana wymaga modyfikacji w wielu nieoczywistych miejscach?" | Klasa „Manager", „Helper", „Util" |
| **O** | Open/Closed | „Czy dodanie nowej opcji wymaga zmiany istniejącego kodu?" | `switch` / `if-else` po typie |
| **L** | Liskov Substitution | „Czy podklasa zachowuje się tak, jak klasa bazowa?" | `UnsupportedOperationException` w podklasie |
| **I** | Interface Segregation | „Czy klient widzi metody, których nie używa?" | Interfejs z 12+ metodami |
| **D** | Dependency Inversion | „Czy klasa wyższego poziomu zna szczegóły niższego?" | `new` w polach klasy |

### Zadanie wstępne — dyskusja w grupach (10 minut)

Otrzymujesz pięć scenariuszy. Dla każdego zdecyduj: **stosujemy SOLID czy YAGNI**?

1. Mała funkcja shellowa do parsowania jednego pliku CSV w skrypcie uruchamianym raz w tygodniu z crona.
2. Klasa rabatów w sklepie internetowym — obecnie 2 typy klientów (`STANDARD`, `VIP`), ale marketing zapowiada nowe segmenty co kwartał.
3. Klasa `User` w aplikacji webowej, używana przez moduł autoryzacji, administracyjny, raportowy i cleanup.
4. Mała aplikacja konsolowa „kalkulator BMI" z trzema wejściami i jednym wyjściem.
5. Klasa `OrderService` mająca pole `private final MySqlOrderRepository repository = new MySqlOrderRepository()` w produkcyjnym kodzie sklepu.

### Pytania kontrolne

1. Wymień trzy konkretne objawy, po których poznasz, że kod „prosi się o SOLID".
2. Wymień dwa konkretne objawy, po których poznasz, że SOLID byłby tu over-engineeringiem.
3. Dlaczego „YAGNI bije DIP" w pewnych sytuacjach? Podaj przykład.
4. Czy SOLID to sekwencja kroków (S → O → L → I → D) przy projektowaniu nowej klasy? Uzasadnij.

---

## 1. S — Single Responsibility Principle

**Cel:** Rozpoznać klasę, która obsługuje **dwóch lub więcej aktorów biznesowych**, i rozbić ją tak, by każda nowa klasa miała jeden powód do zmiany.

**Teoria w pigułce:**
„Klasa powinna mieć tylko jeden powód do zmiany" — czyli obsługiwać **jednego aktora** (księgowość, marketing, infrastruktura mailowa, raportowanie). Jeśli zmiany od dwóch różnych działów wymuszają modyfikacje tej samej klasy, SRP jest naruszone. Sygnał ostrzegawczy: nazwa z sufiksem `Manager`, `Service`, `Helper`. Test pomocniczy: **„Spróbuj opisać tę klasę jednym zdaniem bez słowa »i«"**.

### Kod (przed — God Class `OrderManager`)

Utwórz plik: `src/main/java/com/example/solid/zad01/OrderManagerBefore.java`

```java
package com.example.solid.zad01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class OrderManagerBefore {

    public static class Cart {
        private final List<CartItem> items;
        public Cart(List<CartItem> items) { this.items = items; }
        public boolean isEmpty() { return items.isEmpty(); }
        public List<CartItem> getItems() { return items; }
    }

    public static class CartItem {
        private final double price;
        private final int quantity;
        public CartItem(double price, int quantity) {
            this.price = price;
            this.quantity = quantity;
        }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
    }

    public static class Order {
        private long id;
        private double total;
        public long getId() { return id; }
        public void setId(long id) { this.id = id; }
        public double getTotal() { return total; }
        public void setTotal(double total) { this.total = total; }
    }

    public Order createOrder(Cart cart) {
        // 1. LOGIKA BIZNESOWA: walidacja koszyka, kalkulacja sumy
        if (cart.isEmpty()) {
            throw new IllegalStateException("Pusty koszyk");
        }
        Order order = new Order();
        double total = 0;
        for (CartItem i : cart.getItems()) {
            total += i.getPrice() * i.getQuantity();
        }
        order.setTotal(total);
        return order;
    }

    public void saveToDatabase(Order order) throws Exception {
        // 2. WARSTWA DANYCH: SQL/JDBC
        Connection c = DriverManager.getConnection("jdbc:postgresql://localhost/shop");
        PreparedStatement ps = c.prepareStatement("INSERT INTO orders(total) VALUES(?)");
        ps.setDouble(1, order.getTotal());
        ps.executeUpdate();
    }

    public void sendConfirmationEmail(Order order) {
        // 3. INFRASTRUKTURA MAILOWA: SMTP
        System.out.println("SMTP: wysyłam potwierdzenie zamówienia #" + order.getId());
    }

    public String generatePdfReport(Order order) {
        // 4. GENEROWANIE RAPORTÓW PDF
        System.out.println("PDF: renderuję raport dla zamówienia #" + order.getId());
        return "/tmp/order-" + order.getId() + ".pdf";
    }
}
```

Cztery odpowiedzialności — cztery powody do zmiany:

| Zmiana | Co dotyka |
| --- | --- |
| Nowa promocja od działu marketingu | `createOrder` |
| Zmiana schematu bazy | `saveToDatabase` |
| Nowy szablon emaila | `sendConfirmationEmail` |
| Nowy format raportu PDF | `generatePdfReport` |

### Kod (po — rozbicie zgodne z SRP)

Utwórz plik: `src/main/java/com/example/solid/zad01/OrderRepository.java`

```java
package com.example.solid.zad01;

public interface OrderRepository {
    void save(OrderManagerBefore.Order order);
}
```

Utwórz plik: `src/main/java/com/example/solid/zad01/EmailService.java`

```java
package com.example.solid.zad01;

public interface EmailService {
    void sendConfirmation(OrderManagerBefore.Order order);
}
```

Utwórz plik: `src/main/java/com/example/solid/zad01/ReportService.java`

```java
package com.example.solid.zad01;

public interface ReportService {
    String generatePdf(OrderManagerBefore.Order order);
}
```

Utwórz plik: `src/main/java/com/example/solid/zad01/OrderService.java`

```java
package com.example.solid.zad01;

import java.util.Objects;

public class OrderService {

    private final OrderRepository repository;
    private final EmailService email;
    private final ReportService report;

    public OrderService(OrderRepository repository,
                        EmailService email,
                        ReportService report) {
        this.repository = Objects.requireNonNull(repository);
        this.email      = Objects.requireNonNull(email);
        this.report     = Objects.requireNonNull(report);
    }

    public OrderManagerBefore.Order createOrder(OrderManagerBefore.Cart cart) {
        if (cart.isEmpty()) {
            throw new IllegalStateException("Pusty koszyk");
        }
        OrderManagerBefore.Order order = new OrderManagerBefore.Order();
        order.setTotal(calculateTotal(cart));
        repository.save(order);
        email.sendConfirmation(order);
        return order;
    }

    private double calculateTotal(OrderManagerBefore.Cart cart) {
        return cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }
}
```

### Krok po kroku

1. Wypisz publiczne metody klasy. Spróbuj opisać klasę jednym zdaniem **bez słowa „i"**.
2. Jeśli musisz powiedzieć „klasa robi A **i** B **i** C" — SRP naruszone.
3. Dla każdej grupy metod zadaj pytanie: kto (jaki aktor / dział biznesowy) zażąda zmiany?
4. Wydziel każdą grupę do osobnej klasy/interfejsu. Nazwa nowej klasy powinna opisywać dokładnie tę jedną odpowiedzialność.
5. `OrderService` zna logikę biznesową, ale **nie wie**, jak działa baza, email czy PDF — to są jego zależności w postaci interfejsów.
6. Test `OrderService` mockuje trzy interfejsy i wystarczy — żaden test nie potrzebuje prawdziwej bazy, SMTP ani biblioteki PDF.

### Typowy błąd

**Mylenie SRP z „klasa ma jedną metodę".** SRP nie oznacza, że klasa może mieć tylko jedną metodę publiczną. Oznacza, że wszystkie metody muszą wspierać **jedną odpowiedzialność**. `UserRepository` z `save`, `findById`, `findAll`, `delete` to OK — wszystkie obsługują jedną odpowiedzialność: dostęp do danych użytkownika.

### Pytania kontrolne

1. Klasa ma 12 metod publicznych. Czy narusza SRP? Od czego zależy odpowiedź?
2. Dlaczego „Spróbuj opisać klasę bez słowa »i«" jest praktycznym testem SRP?
3. Wymień trzy konkretne korzyści testowania, które pojawiły się po rozbiciu `OrderManager` na `OrderService` + 3 interfejsy.
4. Czy klasa `OrderService` po refaktorze nadal nie narusza SRP? Uzasadnij.

---

## 2. O — Open/Closed Principle

**Cel:** Zaprojektować kod, który można rozszerzyć przez **dodanie nowej klasy** bez modyfikowania istniejącej. Zamienić `switch`/`if-else` po typie na polimorfizm (Strategy).

**Teoria w pigułce:**
„Klasa powinna być otwarta na rozszerzenie, zamknięta na modyfikację." Dodanie nowej funkcjonalności = **dopisanie nowego kodu**, nie zmiana istniejącego. Każda modyfikacja działającego kodu niesie ryzyko regresji. Praktyczne narzędzie: wzorzec Strategy — interfejs + wiele implementacji. **Reguła kciuka:** czekaj na **trzeci wariant** zanim wprowadzasz interfejs. Pierwszy `if-else` jest OK, drugi to sygnał, trzeci to refaktor.

### Kod (przed — `PriceCalculator` z gałęziami `switch`)

Utwórz plik: `src/main/java/com/example/solid/zad02/PriceCalculatorBefore.java`

```java
package com.example.solid.zad02;

public class PriceCalculatorBefore {

    public record Order(double total) {}

    public double calculate(Order order, String customerType) {
        return switch (customerType) {
            case "STANDARD"  -> order.total();
            case "VIP"       -> order.total() * 0.85;
            case "WHOLESALE" -> order.total() * 0.70;
            case "EMPLOYEE"  -> order.total() * 0.50;
            default -> throw new IllegalArgumentException("Nieznany typ: " + customerType);
        };
    }
}
```

Co się dzieje, gdy marketing wprowadza nowy typ `STUDENT`?

1. Modyfikujemy `PriceCalculator` — dodajemy gałąź `case "STUDENT"`.
2. Code review tej zmiany jest wymagane.
3. Ryzykujemy regresję w innych typach (np. ktoś zepsuje `WHOLESALE` przy okazji).
4. Wszystkie testy `PriceCalculator` muszą być uruchomione ponownie.

Klasa nie jest **zamknięta na modyfikację** — każda nowa kategoria klienta wymusza zmianę kodu.

### Kod (po — Strategy + polimorfizm)

Utwórz plik: `src/main/java/com/example/solid/zad02/DiscountPolicy.java`

```java
package com.example.solid.zad02;

public interface DiscountPolicy {
    double apply(double total);
    String description();
}
```

Utwórz plik: `src/main/java/com/example/solid/zad02/StandardDiscountPolicy.java`

```java
package com.example.solid.zad02;

public class StandardDiscountPolicy implements DiscountPolicy {
    @Override public double apply(double total) { return total; }
    @Override public String description()       { return "Bez rabatu"; }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad02/VipDiscountPolicy.java`

```java
package com.example.solid.zad02;

public class VipDiscountPolicy implements DiscountPolicy {
    private static final double RATE = 0.15;
    @Override public double apply(double total) { return total * (1 - RATE); }
    @Override public String description()       { return "VIP 15%"; }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad02/WholesaleDiscountPolicy.java`

```java
package com.example.solid.zad02;

public class WholesaleDiscountPolicy implements DiscountPolicy {
    private static final double RATE = 0.30;
    @Override public double apply(double total) { return total * (1 - RATE); }
    @Override public String description()       { return "Hurtownia 30%"; }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad02/EmployeeDiscountPolicy.java`

```java
package com.example.solid.zad02;

public class EmployeeDiscountPolicy implements DiscountPolicy {
    private static final double RATE = 0.50;
    @Override public double apply(double total) { return total * (1 - RATE); }
    @Override public String description()       { return "Pracownik 50%"; }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad02/StudentDiscountPolicy.java`

```java
package com.example.solid.zad02;

// NOWA POLITYKA — dodana BEZ modyfikacji istniejących plików (OCP spełnione)
public class StudentDiscountPolicy implements DiscountPolicy {
    private static final double RATE = 0.20;
    @Override public double apply(double total) { return total * (1 - RATE); }
    @Override public String description()       { return "Zniżka studencka 20%"; }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad02/PriceCalculator.java`

```java
package com.example.solid.zad02;

public class PriceCalculator {

    public record Order(double total) {}

    // Nie wie o konkretnych politykach — tylko o interfejsie
    public double calculate(Order order, DiscountPolicy policy) {
        return policy.apply(order.total());
    }
}
```

### Krok po kroku

1. Znajdź `switch` lub `if-else` na typie/stringu/enumie. To kandydat do OCP.
2. Każda gałąź to przyszła klasa. Wprowadź interfejs reprezentujący „to, co robi gałąź".
3. Każdą gałąź przekształć w klasę implementującą interfejs.
4. Klasa nadrzędna (kontekst, tu `PriceCalculator`) dostaje obiekt typu interfejsu i deleguje.
5. Dodanie nowej opcji = **nowa klasa**, brak zmian w istniejących plikach.
6. Magic numbers (0.85, 0.70, 0.20) trafiają do **stałych w klasach polityki** — każda zmiana rabatu jest izolowana.

### Kiedy NIE stosować OCP

OCP nie zawsze się opłaca. Kosztem są dodatkowe klasy, interfejs, infrastruktura mapy strategii.

| Zastosować OCP | NIE stosować |
| --- | --- |
| Spodziewasz się minimum 3 wariantów algorytmu | Masz 2 warianty na zawsze (np. true/false) |
| Nowe warianty pojawiają się regularnie | Klasa stabilna od 2 lat |
| Zmiana w if-else wymusza pełną regresję | Mały, izolowany komponent |
| Inne klasy chcą znać dostępne warianty | Tylko jedna klasa wie o gałęziach |

### Typowy błąd

**„Otwarta na rozszerzenie" rozumiane jako „udostępnić wszystkie pola publiczne".** OCP nie oznacza udostępniania wnętrzności klasy. Oznacza, że **mechanizm rozszerzania jest zaprojektowany** — przez interfejs, abstrakcyjną klasę, callback. Klasa z publicznymi polami jest „otwarta na zniszczenie", a nie na rozszerzenie.

### Pytania kontrolne

1. Co dokładnie znaczy „otwarte na rozszerzenie, zamknięte na modyfikację"?
2. Pokaż konkretnie na kodzie: ile plików zmieniasz, dodając nowy typ klienta przed refaktorem, a ile po refaktorze?
3. Czy każdy `switch` musi być rozbity na polimorfizm? Wymień co najmniej dwie sytuacje, w których lepiej zostawić `switch`.
4. Jakie są minusy „przedwczesnego" stosowania OCP (interfejs dla jednej implementacji)?

---

## 3. L — Liskov Substitution Principle

**Cel:** Zrozumieć, że podklasa musi być **pełnowartościowym zamiennikiem** klasy bazowej. Rozpoznać podstępne naruszenia: nowe wyjątki, ignorowane settery, wzmocnione pre-warunki.

**Teoria w pigułce:**
„Podklasa musi być zastępowalna za klasę bazową bez zmiany poprawności programu." Jeśli `B extends A`, to każde miejsce w kodzie oczekujące `A` musi działać tak samo z `B`. **Kontrakt** klasy bazowej (jakie metody, z jakimi pre- i post-warunkami) MUSI być zachowany w podklasie. Pierwsza linia obrony: **kompozycja zamiast dziedziczenia** (Effective Java, Item 18). Druga: rekordy / niezmienne obiekty.

### Kod (przed — klasyczny Rectangle/Square)

Utwórz plik: `src/main/java/com/example/solid/zad03/RectangleBefore.java`

```java
package com.example.solid.zad03;

public class RectangleBefore {

    protected int width;
    protected int height;

    public void setWidth(int width)   { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public int getWidth()  { return width; }
    public int getHeight() { return height; }
    public int area()      { return width * height; }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad03/SquareBefore.java`

```java
package com.example.solid.zad03;

// "Kwadrat to specjalny prostokąt" — kuszące matematycznie, ale...
public class SquareBefore extends RectangleBefore {

    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width;   // kwadrat MUSI mieć równe boki
    }

    @Override
    public void setHeight(int height) {
        this.width = height;
        this.height = height;
    }
}
```

Klient zakłada **kontrakt `Rectangle`**:

```java
void verifyRectangle(RectangleBefore r) {
    r.setWidth(5);
    r.setHeight(4);
    // Kontrakt: szerokość = 5, wysokość = 4, pole = 20
    System.out.println("Oczekiwane 20, otrzymane: " + r.area());
}

verifyRectangle(new RectangleBefore());  // 20 — PASS
verifyRectangle(new SquareBefore());     // 16 — FAIL (Square zignorował setWidth)
```

`SquareBefore` **nie jest** poprawnym `RectangleBefore` — łamie kontrakt setterów. Dziedziczenie tworzy fałszywą hierarchię.

### Kod (po — wspólny interfejs, niezmienne rekordy)

Utwórz plik: `src/main/java/com/example/solid/zad03/Shape.java`

```java
package com.example.solid.zad03;

public interface Shape {
    int area();
}
```

Utwórz plik: `src/main/java/com/example/solid/zad03/Rectangle.java`

```java
package com.example.solid.zad03;

public record Rectangle(int width, int height) implements Shape {

    public Rectangle {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Wymiary muszą być dodatnie");
        }
    }

    @Override
    public int area() {
        return width * height;
    }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad03/Square.java`

```java
package com.example.solid.zad03;

public record Square(int side) implements Shape {

    public Square {
        if (side <= 0) {
            throw new IllegalArgumentException("Bok musi być dodatni");
        }
    }

    @Override
    public int area() {
        return side * side;
    }
}
```

Niemutowalne rekordy + wspólny interfejs. Brak dziedziczenia = brak problemu LSP.

### LSP w prawdziwym życiu — naruszenia, które łatwo przeoczyć

#### Naruszenie 1: Podklasa rzuca nowy typ wyjątku

Utwórz plik: `src/main/java/com/example/solid/zad03/LoggerLspBreak.java`

```java
package com.example.solid.zad03;

import java.io.IOException;

class FileLogger {
    public void log(String message) {
        // może rzucić RuntimeException jeśli plik jest zablokowany
    }
}

class NetworkLogger extends FileLogger {
    @Override
    public void log(String message) {
        // PUŁAPKA: rzuca IOException, na które klient FileLogger nie był przygotowany
        // — w prawdziwym kodzie wymusza checked exception lub łapanie Exception
        throw new RuntimeException(new IOException("Sieć niedostępna"));
    }
}
```

#### Naruszenie 2: Podklasa wymaga więcej, niż klasa bazowa

```java
class Bird {
    public void move() { /* spaceruje albo lata */ }
}

class Penguin extends Bird {
    @Override
    public void move() {
        if (waterAvailable()) {
            swim();
        } else {
            throw new IllegalStateException("Pingwin nie chodzi po lądzie bez wody!");
        }
    }

    private boolean waterAvailable() { return false; }
    private void swim() {}
}

// Klient zakłada że Bird.move() zawsze działa — Penguin łamie to założenie
```

#### Naruszenie 3: Podklasa zmienia post-warunek

```java
class IntegerStack {
    /** Po push(x), peek() == x */
    public void push(int x) { /* ... */ }
}

class CountingStack extends IntegerStack {
    @Override
    public void push(int x) {
        // PUŁAPKA: jeśli wartość już była, nie wkładamy ponownie — peek() != x
        if (!contains(x)) super.push(x);
    }

    private boolean contains(int x) { return false; }
}
```

### Krok po kroku — jak unikać naruszeń LSP

1. Sprawdź każdą podklasę: czy każda metoda zachowuje się **dokładnie** tak, jak w klasie bazowej?
2. Jeśli podklasa nadpisuje setter w sposób ignorujący parametr — to złamanie kontraktu.
3. Jeśli podklasa rzuca nowy typ wyjątku, którego klient bazowy się nie spodziewa — złamanie kontraktu.
4. Jeśli podklasa wymaga więcej (non-null tam, gdzie baza akceptowała null) — wzmocnienie pre-warunku, naruszenie.
5. **Pierwsza linia obrony:** zamień modyfikowalne obiekty na rekordy / niezmienne. Brak setterów = brak naruszeń.
6. **Druga linia:** wspólny **interfejs** zamiast wspólnej **klasy bazowej**. Brak dziedziczenia = brak problemu LSP.
7. **Trzecia linia:** kompozycja zamiast dziedziczenia.

### Kompozycja zamiast dziedziczenia

```java
// Zamiast: class Square extends Rectangle
class SquareByComposition {
    private final Rectangle inner;   // kompozycja — Square ZAWIERA Rectangle

    public SquareByComposition(int side) {
        this.inner = new Rectangle(side, side);
    }

    public int area() { return inner.area(); }
}
```

`Square` używa `Rectangle`, ale **nim nie jest**. Klient `Rectangle` nie dostanie `Square'a` w sposób niespodziewany.

### Typowy błąd

**„IS-A relationship" w języku naturalnym nie zawsze przekłada się na dziedziczenie.** Kwadrat „jest" prostokątem matematycznie. Pingwin „jest" ptakiem biologicznie. Ale **modele obiektowe nie muszą odzwierciedlać świata rzeczywistego** — muszą odzwierciedlać **kontrakty zachowań**.

### Ćwiczenie grupowe (5 minut)

Dla każdej pary zdecyduj: bezpieczne dziedziczenie czy naruszenie LSP?
1. `Bird` / `Eagle`
2. `Bird` / `Penguin`
3. `BankAccount` / `SavingsAccount`
4. `Animal` / `Dog`
5. `Vehicle` / `ElectricCar`
6. `Vehicle` / `Tank` (gąsienice zamiast kół)

### Pytania kontrolne

1. Dlaczego „kwadrat to specjalny prostokąt" działa w matematyce, ale łamie LSP w kodzie?
2. Wymień trzy typowe naruszenia LSP, które łatwo przeoczyć w code review.
3. Co znaczy „faworyzuj kompozycję nad dziedziczeniem"? Pokaż konkretnie na klasie `SquareByComposition`.
4. Jak rekordy w Javie pomagają unikać naruszeń LSP?

---

## 4. I — Interface Segregation Principle

**Cel:** Pisać **wąskie interfejsy** dopasowane do konkretnego klienta, zamiast jednego „tłustego" interfejsu, który nikt nie używa w całości.

**Teoria w pigułce:**
„Klient nie powinien być zmuszony zależeć od metod, których nie używa." Lepsze 5 wąskich interfejsów (po 2–3 metody) niż jeden interfejs z 15 metodami. Klasa konkretna może implementować kilka wąskich interfejsów jednocześnie. Klient zna tylko ten interfejs, który mu jest potrzebny — typ chroni przed głupotą w runtime.

### Kod (przed — Fat Interface)

Utwórz plik: `src/main/java/com/example/solid/zad04/UserRepositoryBefore.java`

```java
package com.example.solid.zad04;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryBefore {

    // Operacje CRUD (wszyscy potrzebują)
    Optional<User> findById(Long id);
    List<User> findAll();
    User save(User user);
    void delete(Long id);

    // Operacje używane TYLKO przez moduł autoryzacji
    Optional<User> findByEmail(String email);
    void updatePassword(Long id, String hash);
    void recordFailedLogin(Long id);

    // Operacje używane TYLKO przez moduł administracyjny
    List<User> findByRole(String role);
    void grantAdmin(Long id);
    void banUser(Long id);

    // Operacje używane TYLKO przez cron usuwający martwe sesje
    List<User> findWithExpiredSessions();
    void purgeInactiveUsers();
}
```

Utwórz plik: `src/main/java/com/example/solid/zad04/User.java`

```java
package com.example.solid.zad04;

public record User(Long id, String email, String role) {}
```

Konsekwencje grubego interfejsu:

- **`ReadOnlyUserRepository` musi udawać** — implementacja `save()`, `delete()` rzuca `UnsupportedOperationException`. To dodatkowo narusza **LSP**.
- **Test każdej klasy** używającej `UserRepository` widzi wszystkie 12 metod, choć potrzebuje tylko jednej.
- **Atrapa** szerokiego interfejsu musi udostępniać wszystkie metody, nawet nieużywane przez testowany kod.
- **Zmiana sygnatury** w sekcji admin powoduje rekompilację wszystkich klientów.

### Kod (po — segregacja interfejsów)

Utwórz plik: `src/main/java/com/example/solid/zad04/UserReader.java`

```java
package com.example.solid.zad04;

import java.util.List;
import java.util.Optional;

public interface UserReader {
    Optional<User> findById(Long id);
    List<User> findAll();
}
```

Utwórz plik: `src/main/java/com/example/solid/zad04/UserWriter.java`

```java
package com.example.solid.zad04;

public interface UserWriter {
    User save(User user);
    void delete(Long id);
}
```

Utwórz plik: `src/main/java/com/example/solid/zad04/UserAuthRepository.java`

```java
package com.example.solid.zad04;

import java.util.Optional;

public interface UserAuthRepository {
    Optional<User> findByEmail(String email);
    void updatePassword(Long id, String hash);
    void recordFailedLogin(Long id);
}
```

Utwórz plik: `src/main/java/com/example/solid/zad04/UserAdminRepository.java`

```java
package com.example.solid.zad04;

import java.util.List;

public interface UserAdminRepository {
    List<User> findByRole(String role);
    void grantAdmin(Long id);
    void banUser(Long id);
}
```

Utwórz plik: `src/main/java/com/example/solid/zad04/UserCleanupRepository.java`

```java
package com.example.solid.zad04;

import java.util.List;

public interface UserCleanupRepository {
    List<User> findWithExpiredSessions();
    void purgeInactiveUsers();
}
```

Utwórz plik: `src/main/java/com/example/solid/zad04/DatabaseUserRepository.java`

```java
package com.example.solid.zad04;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

// Jedna implementacja konkretna — implementuje TYLE interfejsów, ile potrzeba
public class DatabaseUserRepository
        implements UserReader, UserWriter, UserAuthRepository,
                   UserAdminRepository, UserCleanupRepository {

    @Override public Optional<User> findById(Long id) { return Optional.empty(); }
    @Override public List<User> findAll() { return Collections.emptyList(); }
    @Override public User save(User user) { return user; }
    @Override public void delete(Long id) { }
    @Override public Optional<User> findByEmail(String email) { return Optional.empty(); }
    @Override public void updatePassword(Long id, String hash) { }
    @Override public void recordFailedLogin(Long id) { }
    @Override public List<User> findByRole(String role) { return Collections.emptyList(); }
    @Override public void grantAdmin(Long id) { }
    @Override public void banUser(Long id) { }
    @Override public List<User> findWithExpiredSessions() { return Collections.emptyList(); }
    @Override public void purgeInactiveUsers() { }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad04/UserDisplayService.java`

```java
package com.example.solid.zad04;

import java.util.List;

public class UserDisplayService {

    private final UserReader reader;

    public UserDisplayService(UserReader reader) {
        this.reader = reader;
    }

    public List<User> listAll() {
        return reader.findAll();
        // Klient widzi TYLKO findById / findAll — nie zna admin, auth ani cleanup
    }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad04/AuthService.java`

```java
package com.example.solid.zad04;

import java.util.Optional;

public class AuthService {

    private final UserAuthRepository authRepo;

    public AuthService(UserAuthRepository authRepo) {
        this.authRepo = authRepo;
    }

    public Optional<User> login(String email, String passwordHash) {
        return authRepo.findByEmail(email);
        // AuthService nie widzi banUser ani purgeInactiveUsers
    }
}
```

### Krok po kroku

1. Znajdź interfejs z więcej niż 4–5 metodami.
2. Pogrupuj metody **według klientów** — nie według nazw. Pytanie: które klasy używają których metod razem?
3. Każda grupa = jeden wąski interfejs. Nazwa interfejsu powinna pasować do roli klienta (`UserReader`, `UserAuthRepository`).
4. Implementacja konkretna implementuje tyle interfejsów, ile potrzeba (`implements UserReader, UserWriter, ...`).
5. Klienci zależą tylko od interfejsu, który ich obchodzi — wstrzykiwanego przez konstruktor.
6. **Test:** jeśli implementacja rzuca `UnsupportedOperationException` w którejkolwiek metodzie — interfejs był za szeroki.

### Typowy błąd

**Tworzenie interfejsów z jedną metodą dla każdej klasy.** Drugie ekstremum — zamiast jednego Fat Interface mamy 12 jednometodowych. To nie jest cel ISP. Cel to **grupowanie metod według klienta**. Jeśli `AuthService` używa zawsze 3 metod razem — to jeden interfejs `UserAuthRepository`, nie trzy osobne.

### Pytania kontrolne

1. Skąd wiesz, że interfejs jest „za gruby"? Wymień dwa konkretne sygnały.
2. Czy ISP oznacza, że każdy interfejs ma mieć dokładnie jedną metodę? Uzasadnij.
3. Jak ISP łączy się z LSP? Pokaż na przykładzie atrapy szerokiego interfejsu rzucającej `UnsupportedOperationException`.
4. Klasa `DatabaseUserRepository` implementuje 5 wąskich interfejsów. Czy to OK? Co by było problemem?

---

## 5. D — Dependency Inversion Principle

**Cel:** Pisać moduły wysokiego poziomu (logikę biznesową), które zależą od **abstrakcji**, nie od konkretnej implementacji infrastruktury. Wstrzykiwać zależności przez konstruktor.

**Teoria w pigułce:**
„Moduły wyższego poziomu nie powinny zależeć od modułów niższego poziomu. Oba powinny zależeć od **abstrakcji**." Wysoki poziom = logika biznesowa (`OrderService`). Niski poziom = szczegóły infrastruktury (`MySqlOrderRepository`, `SmtpEmailService` — czyli klasa wysyłająca emaile protokołem **SMTP**). W praktyce: zamiast `new MySqlOrderRepository()` w polu — `OrderRepository repo` jako parametr konstruktora. Mówimy wtedy o **wstrzykiwaniu zależności** (ang. *Dependency Injection*, **DI**). To umożliwia testowanie (mock), wymianę implementacji i porządkuje kierunek zależności.

### Kod (przed — twarda zależność na konkretnej implementacji)

Utwórz plik: `src/main/java/com/example/solid/zad05/OrderServiceBefore.java`

```java
package com.example.solid.zad05;

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
```

Konsekwencje:

- Nie podmienisz `MySqlOrderRepository` na PostgreSQL bez modyfikacji `OrderService`.
- W teście **OrderService** nie zmokujesz `repository` — wymusza prawdziwą bazę MySQL.
- Dwa moduły są ściśle splątane — zmiana w SMTP wymaga rekompilacji `OrderService`.

### Kod (po — wstrzykiwanie przez interfejs)

Utwórz plik: `src/main/java/com/example/solid/zad05/OrderRepository.java`

```java
package com.example.solid.zad05;

import java.util.Optional;

public interface OrderRepository {
    void save(String customer, double amount);
    Optional<String> findById(Long id);
}
```

Utwórz plik: `src/main/java/com/example/solid/zad05/EmailService.java`

```java
package com.example.solid.zad05;

public interface EmailService {
    void send(String to, String body);
}
```

Utwórz plik: `src/main/java/com/example/solid/zad05/MySqlOrderRepository.java`

```java
package com.example.solid.zad05;

import java.util.Optional;

public class MySqlOrderRepository implements OrderRepository {
    @Override
    public void save(String customer, double amount) {
        System.out.println("MySQL INSERT: " + customer + "/" + amount);
    }
    @Override
    public Optional<String> findById(Long id) {
        return Optional.empty();
    }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad05/PostgresOrderRepository.java`

```java
package com.example.solid.zad05;

import java.util.Optional;

public class PostgresOrderRepository implements OrderRepository {
    @Override
    public void save(String customer, double amount) {
        System.out.println("POSTGRES INSERT: " + customer + "/" + amount);
    }
    @Override
    public Optional<String> findById(Long id) {
        return Optional.empty();
    }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad05/InMemoryOrderRepository.java`

```java
package com.example.solid.zad05;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryOrderRepository implements OrderRepository {

    private final Map<Long, String> store = new HashMap<>();
    private long sequence = 0;

    @Override
    public void save(String customer, double amount) {
        store.put(++sequence, customer + "/" + amount);
    }

    @Override
    public Optional<String> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad05/SmtpEmailService.java`

```java
package com.example.solid.zad05;

public class SmtpEmailService implements EmailService {

    private final String host;
    private final int port;

    public SmtpEmailService(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void send(String to, String body) {
        System.out.println("SMTP[" + host + ":" + port + "] -> " + to + ": " + body);
    }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad05/ConsoleEmailService.java`

```java
package com.example.solid.zad05;

public class ConsoleEmailService implements EmailService {
    @Override
    public void send(String to, String body) {
        System.out.println("KONSOLA -> " + to + ": " + body);
    }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad05/OrderService.java`

```java
package com.example.solid.zad05;

import java.util.Objects;

public class OrderService {

    private final OrderRepository repository;
    private final EmailService email;

    // Konstruktor wstrzykuje zależności (Dependency Injection — DI)
    public OrderService(OrderRepository repository, EmailService email) {
        this.repository = Objects.requireNonNull(repository);
        this.email      = Objects.requireNonNull(email);
    }

    public void placeOrder(String customer, double amount) {
        repository.save(customer, amount);
        email.send(customer, "Zamówienie przyjęte na " + amount + " PLN");
    }
}
```

Złożenie aplikacji (Composition Root):

```java
// W produkcji — w main lub klasie Bootstrap
OrderRepository repo  = new MySqlOrderRepository();
EmailService    email = new SmtpEmailService("smtp.gmail.com", 587);
OrderService    svc   = new OrderService(repo, email);

// W teście — fake/mock, bez bazy i SMTP
OrderRepository fakeRepo  = new InMemoryOrderRepository();
EmailService    fakeEmail = new ConsoleEmailService();
OrderService    svc2      = new OrderService(fakeRepo, fakeEmail);
```

### Co dokładnie jest „odwrócone"?

**Przed (klasyczna zależność):**

```
OrderService ──── używa ───> MySqlOrderRepository
       │                              ▲
       │   (modyfikacja MySQL         │
       │    zmusza modyfikacji        │
       │    OrderService)             │
```

**Po DIP:**

```
OrderService ─── używa ───> OrderRepository (interfejs)
                                  ▲
                                  │ implementuje
                                  │
                       MySqlOrderRepository
```

Strzałka zależności od konkretu do abstrakcji została odwrócona. Implementacja zależy od interfejsu (zdefiniowanego po stronie wysokopoziomowej), a nie odwrotnie. Logika biznesowa jest chroniona przed zmianami w infrastrukturze.

### Krok po kroku

1. Znajdź w klasie pola inicjalizowane przez `new ConcretClass()`. To „twarda zależność" — kandydat do DIP.
2. Wprowadź interfejs reprezentujący kontrakt zależności.
3. Klasa wysokiego poziomu zależy od interfejsu, nie od konkretu.
4. Konkretna implementacja `implements` interfejs.
5. Zależności wstrzykuj przez **konstruktor**, nie setter. `Objects.requireNonNull` chroni przed `null`.
6. Złożenie obiektów (kto z kim) odbywa się w jednym miejscu — w metodzie `main` lub osobnej klasie `Bootstrap`. Mówimy o tym **Composition Root** („korzeń kompozycji"). W tej karcie składamy wszystko ręcznie — w realnych projektach istnieją frameworki (np. Spring), które to automatyzują, ale ich znajomość **nie jest tu wymagana**: ręczne składanie pokazuje całą mechanikę DI bez magii.

### Typowy błąd

**Wstrzykiwanie zależności przez settery zamiast konstruktora:**

```java
class OrderServiceBad {
    private OrderRepository repository;

    public void setRepository(OrderRepository r) { this.repository = r; }
    // Bez wywołania settera repository == null → NullPointerException (NPE) w runtime
}
```

**Konstruktorowe DI** (wstrzykiwanie przez konstruktor) gwarantuje, że obiekt jest **kompletny w momencie utworzenia**. **Setterowe DI** (wstrzykiwanie przez setter) tworzy okno, w którym obiekt jest tylko częściowo zainicjalizowany. **Zawsze preferuj konstruktorowe DI.**

### Pytania kontrolne

1. Co dokładnie jest „odwrócone" w Dependency Inversion?
2. Dlaczego konstruktorowe DI jest lepsze niż setterowe? Pokaż konkretny scenariusz, w którym setter zawodzi.
3. Jak DIP łączy się z testowalnością? Pokaż konkretną korzyść na przykładzie `OrderService`.
4. Kiedy DIP jest over-engineeringiem? Wskazówka: YAGNI.
5. Czym różni się **klasyczna zależność** od **odwróconej**? Narysuj strzałkę zależności w obu wariantach.

---

## 6. Mini-projekt ZL-15 — Refaktoryzacja God Class `UserManager`

**Cel:** Połączyć wszystkie pięć zasad SOLID w jednej refaktoryzacji. Dostajesz monolityczny `UserManager` i rozbijasz go etapami: DIP → SRP → OCP → orkiestracja.

**Teoria w pigułce:**
Realny refaktor nie zaczyna się od „przepiszmy wszystko". Idziesz **małymi krokami**, każdy zostawia kod w stanie kompilującym się i przechodzącym testy. Refaktor God Class jako **big-bang** to częsta przyczyna porzuconych projektów. Tu pokażemy 5 kroków + złożenie aplikacji.

### Kod startowy — God Class

Utwórz plik: `src/main/java/com/example/solid/zad06/UserManagerBefore.java`

```java
package com.example.solid.zad06;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManagerBefore {

    private final List<Map<String, Object>> users = new ArrayList<>();

    public Map<String, Object> registerUser(String name, String email, String role) {

        // 1. WALIDACJA (powinna być w UserValidator)
        if (name == null || name.isBlank()) {
            throw new RuntimeException("Brak imienia");
        }
        if (email == null || !email.contains("@")) {
            throw new RuntimeException("Zły email");
        }
        if (email.length() > 100) {
            throw new RuntimeException("Email za długi");
        }

        // 2. DOSTĘP DO DANYCH (powinna być w UserRepository)
        for (Map<String, Object> u : users) {
            if (email.equalsIgnoreCase((String) u.get("email"))) {
                throw new RuntimeException("Email już istnieje: " + email);
            }
        }

        // 3. LOGIKA TWORZENIA (właściwa odpowiedzialność serwisu)
        Map<String, Object> user = new HashMap<>();
        user.put("id", System.nanoTime());
        user.put("name", name);
        user.put("email", email.toLowerCase());
        user.put("role", role != null ? role : "USER");
        user.put("createdAt", LocalDateTime.now());
        users.add(user);

        // 4. POWIADOMIENIE EMAIL (powinna być w EmailService)
        System.out.println("Wysyłam email do: " + email);
        System.out.println("Treść: Witamy w serwisie, " + name + "!");

        return user;
    }
}
```

Naruszone zasady:

| Zasada | Naruszenie |
| --- | --- |
| **SRP** | 4 odpowiedzialności w jednej klasie (walidacja, dane, logika, powiadomienie) |
| **OCP** | Dodanie nowej reguły walidacji = zmiana `registerUser` |
| **LSP** | `Map<String, Object>` to ucieczka od typowanego modelu |
| **ISP** | Brak interfejsów do segregacji |
| **DIP** | Twarda zależność na `ArrayList`, `System.out.println` zamiast abstrakcji |

### Docelowa architektura

```
src/main/java/com/example/solid/zad06/
 ├─ User.java                       // record (model domeny)
 ├─ UserRepository.java             // interfejs (DIP)
 ├─ InMemoryUserRepository.java     // implementacja
 ├─ EmailService.java               // interfejs (DIP)
 ├─ ConsoleEmailService.java        // implementacja
 ├─ ValidationRule.java             // interfejs (Strategy / OCP)
 ├─ UserValidator.java              // lista reguł
 └─ UserService.java                // orkiestrator (SRP)
```

### Krok 1 — DIP: wprowadzamy interfejsy i model

Utwórz plik: `src/main/java/com/example/solid/zad06/User.java`

```java
package com.example.solid.zad06;

import java.time.LocalDateTime;

public record User(long id,
                   String name,
                   String email,
                   String role,
                   LocalDateTime createdAt) {}
```

Utwórz plik: `src/main/java/com/example/solid/zad06/UserRepository.java`

```java
package com.example.solid.zad06;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
}
```

Utwórz plik: `src/main/java/com/example/solid/zad06/EmailService.java`

```java
package com.example.solid.zad06;

public interface EmailService {
    void sendWelcome(User user);
}
```

### Krok 2 — SRP: wydzielamy klasy infrastruktury

Utwórz plik: `src/main/java/com/example/solid/zad06/InMemoryUserRepository.java`

```java
package com.example.solid.zad06;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> store = new LinkedHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public Optional<User> findByEmail(String email) {
        return store.values().stream()
                .filter(u -> u.email().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public User save(User user) {
        long id = sequence.incrementAndGet();
        User saved = new User(id, user.name(), user.email(),
                user.role(), user.createdAt());
        store.put(id, saved);
        return saved;
    }
}
```

Utwórz plik: `src/main/java/com/example/solid/zad06/ConsoleEmailService.java`

```java
package com.example.solid.zad06;

public class ConsoleEmailService implements EmailService {

    @Override
    public void sendWelcome(User user) {
        System.out.printf("EMAIL → %s: Witamy w serwisie, %s!%n",
                user.email(), user.name());
    }
}
```

### Krok 3 — OCP: walidator jako lista reguł (Strategy)

Utwórz plik: `src/main/java/com/example/solid/zad06/ValidationRule.java`

```java
package com.example.solid.zad06;

@FunctionalInterface
public interface ValidationRule {
    void validate(User user);
}
```

Utwórz plik: `src/main/java/com/example/solid/zad06/UserValidator.java`

```java
package com.example.solid.zad06;

import java.util.List;

public class UserValidator {

    private final List<ValidationRule> rules;

    public UserValidator(List<ValidationRule> rules) {
        this.rules = List.copyOf(rules);
    }

    public void validate(User user) {
        for (ValidationRule rule : rules) {
            rule.validate(user);
        }
    }
}
```

Konkretne reguły jako lambdy (dodajesz/usuwasz bez zmiany `UserValidator`):

```java
ValidationRule notBlankName = u -> {
    if (u.name() == null || u.name().isBlank())
        throw new IllegalArgumentException("Imię jest wymagane");
};

ValidationRule validEmail = u -> {
    if (u.email() == null || !u.email().contains("@"))
        throw new IllegalArgumentException("Niepoprawny email: " + u.email());
};

ValidationRule emailLength = u -> {
    if (u.email() != null && u.email().length() > 100)
        throw new IllegalArgumentException("Email za długi (max 100): " + u.email());
};
```

### Krok 4 — Serwis, który tylko orkiestruje

Utwórz plik: `src/main/java/com/example/solid/zad06/UserService.java`

```java
package com.example.solid.zad06;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserService {

    private final UserRepository repository;
    private final EmailService email;
    private final UserValidator validator;

    public UserService(UserRepository repository,
                       EmailService email,
                       UserValidator validator) {
        this.repository = Objects.requireNonNull(repository);
        this.email      = Objects.requireNonNull(email);
        this.validator  = Objects.requireNonNull(validator);
    }

    public User registerUser(String name, String emailAddress, String role) {

        // 1. Tworzymy obiekt domenowy (id nadany przez repo)
        User candidate = new User(
                0L,
                name,
                emailAddress != null ? emailAddress.toLowerCase() : null,
                role != null ? role : "USER",
                LocalDateTime.now()
        );

        // 2. Walidacja przez UserValidator (Strategy)
        validator.validate(candidate);

        // 3. Sprawdzenie unikalności
        if (repository.findByEmail(candidate.email()).isPresent()) {
            throw new IllegalStateException("Email już zajęty: " + candidate.email());
        }

        // 4. Zapis
        User saved = repository.save(candidate);

        // 5. Powiadomienie
        email.sendWelcome(saved);

        return saved;
    }
}
```

### Krok 5 — Złożenie aplikacji

Utwórz plik: `src/main/java/com/example/solid/zad06/App.java`

```java
package com.example.solid.zad06;

import java.util.List;

public class App {

    public static void main(String[] args) {

        UserRepository repository = new InMemoryUserRepository();
        EmailService email = new ConsoleEmailService();

        // Reguły walidacji jako lambdy
        ValidationRule notBlankName = u -> {
            if (u.name() == null || u.name().isBlank())
                throw new IllegalArgumentException("Imię jest wymagane");
        };
        ValidationRule validEmail = u -> {
            if (u.email() == null || !u.email().contains("@"))
                throw new IllegalArgumentException("Niepoprawny email: " + u.email());
        };
        ValidationRule emailLength = u -> {
            if (u.email() != null && u.email().length() > 100)
                throw new IllegalArgumentException("Email za długi (max 100): " + u.email());
        };

        UserValidator validator = new UserValidator(List.of(
                notBlankName, validEmail, emailLength));

        UserService service = new UserService(repository, email, validator);

        User u1 = service.registerUser("Anna Nowak", "anna@test.pl", null);
        System.out.println("Zarejestrowano: " + u1);

        // Dodanie nowej reguły = nowa lambda, BEZ ZMIANY UserService (OCP)
        ValidationRule noAdminEmail = u -> {
            if (u.email() != null && u.email().startsWith("admin@"))
                throw new IllegalArgumentException("Email admin@* zabroniony");
        };

        UserValidator strictValidator = new UserValidator(List.of(
                notBlankName, validEmail, emailLength, noAdminEmail));
        UserService strictService = new UserService(repository, email, strictValidator);

        try {
            strictService.registerUser("Hakier", "admin@xyz.pl", "ADMIN");
        } catch (IllegalArgumentException ex) {
            System.out.println("Zablokowano: " + ex.getMessage());
        }
    }
}
```

### Bilans refaktoryzacji

| Przed | Po |
| --- | --- |
| 1 klasa, 4 odpowiedzialności | 7 klas / interfejsów, każdy z jedną odpowiedzialnością |
| `Map<String, Object>` jako model | Rekord `User` z typami |
| Walidacja zaszyta w if-else | Walidator z listą reguł (Strategy + OCP) |
| `System.out.println` w środku logiki | Interfejs `EmailService` (DIP) |
| Test wymaga ręcznego `Map.put(...)` | Test wstrzykuje fake repository i sprawdza interakcje |
| Dodanie reguły = modyfikacja `registerUser` | Dodanie reguły = nowa lambda w `App` |

### Krok po kroku — jak przeprowadzić refaktor w realnym kodzie

1. **Zacznij od testu**: napisz test, który sprawdza działanie obecnego `UserManager` na typowym scenariuszu. Test będzie siatką bezpieczeństwa.
2. Każdy z 5 kroków refaktoru = **jeden commit**. Po każdym kroku test musi przechodzić.
3. Kolejność jest istotna: najpierw model (`User` record), bo wszystko od niego zależy.
4. Walidator wyciągasz przed zapisaniem do repo — „złe wejście" = „nie warto liczyć".
5. Strategy wprowadzasz dopiero, gdy MASZ wiele wariantów walidacji. Pierwsza reguła to nie powód — czekaj na trzecią.
6. DIP (interfejsy `UserRepository`, `EmailService`) wprowadzasz, gdy chcesz testowalność / wymienialność. W projekcie eksperymentalnym może być zbędne.
7. Orkiestrator (`UserService`) na końcu — łączy wszystko, ale sam już prawie nic nie robi.

### Typowy błąd

**Refaktoryzacja całej klasy w jednym commicie.** Powyższe 5 kroków powinno być **5 osobnymi commitami** (lub PR-ami). Każdy krok dodaje jedną zasadę i zostawia kod w stanie kompilującym się i przechodzącym testy. Refaktor jako big-bang to częsta przyczyna porzuconych refaktoryzacji w realnym kodzie.

### Pytania kontrolne

1. Co konkretnie zyskałeś, zamieniając `Map<String, Object>` na rekord `User`?
2. Wymień trzy zmiany biznesowe, które byłyby trudne w wersji przed refaktorem, a są łatwe po.
3. Gdzie konkretnie zostały zastosowane: SRP, OCP, DIP w docelowej architekturze? Wskaż konkretne klasy / interfejsy.
4. Klasa `ValidationRule` to interfejs z jedną metodą. Czy to nie narusza zasady „nie twórz interfejsu dla jednej implementacji" (YAGNI)?
5. Jeśli właściciel sklepu mówi „dodajemy regułę: email nie może kończyć się na .ru" — ile plików musisz zmienić w wersji po refaktorze? A w wersji przed?
6. Co byłoby trudne dodać w wersji God Class, a łatwe po refaktoryzacji? Wymień co najmniej cztery rzeczy.
7. Dlaczego refaktor „wszystko naraz w jednym PR" jest złym pomysłem? Co stracisz?

---

## Podsumowanie

Karta pokrywa pięć zasad SOLID na praktycznych przykładach biznesowych:

| Sekcja | Przykład | Refaktor |
| --- | --- | --- |
| **SRP** | God Class `OrderManager` | `OrderService` + 3 interfejsy infrastruktury |
| **OCP** | `PriceCalculator` z `switch` po typie klienta | `DiscountPolicy` (Strategy) + osobne klasy polityk |
| **LSP** | `Square extends Rectangle` | Wspólny interfejs `Shape` + niezmienne rekordy |
| **ISP** | Fat Interface `UserRepository` z 12 metodami | 5 wąskich interfejsów (`UserReader`, `UserAuthRepository`, …) |
| **DIP** | `OrderService` z `new MySqlOrderRepository()` w polu | Wstrzykiwanie `OrderRepository` przez konstruktor |
| **Mini-projekt** | God Class `UserManager` | 7 klas/interfejsów, 5 kroków refaktoru, każdy = 1 commit |

Każda sekcja zawiera: cel, teorię w pigułce, kod „przed" i „po", instrukcję krok po kroku, typowy błąd kursantów oraz pytania kontrolne. Przykłady są biznesowe (zamówienia, rabaty, użytkownicy, raporty) — żadnych `Foo`/`Bar`.

**Pamiętaj:** SOLID to filtry decyzji projektowych, nie checklist. Stosuj, gdy kod zaczyna się psuć. Nie stosuj na siłę — **YAGNI bije DIP**, gdy nie ma rzeczywistej potrzeby zmiany.
