# Odpowiedzi na pytania z SOLID

## 0. Wprowadzenie — kiedy NIE stosować SOLID

**1.** Mała funkcja shellowa do parsowania jednego pliku CSV w skrypcie uruchamianym raz w tygodniu z crona. → **YAGNI**

Jeżeli ta funkcja robi tylko jedną rzecz, to nie ma tu potrzeby stosowania zasad SOLID. To tylko parser, którego rzadko ktoś będzie ulepszał albo rozbudowywał.

**2.** Klasa rabatów w sklepie internetowym — obecnie 2 typy klientów (`STANDARD`, `VIP`), ale marketing zapowiada nowe segmenty co kwartał. → **SOLID**

Tutaj SOLID to dobry wybór, ponieważ jest to klasa, która będzie rozbudowywana na bieżąco.

**3.** Klasa `User` w aplikacji webowej, używana przez moduł autoryzacji, administracyjny, raportowy i cleanup. → **SOLID**

Ta klasa jest używana przez wiele modułów — tutaj SOLID, aby nie stała się God Objectem.

**4.** Mała aplikacja konsolowa „kalkulator BMI" z trzema wejściami i jednym wyjściem. → **YAGNI**

Ta apka będzie robić to samo non stop, czyli liczyć BMI. SOLID byłby tu over-engineeringiem.

**5.** Klasa `OrderService` mająca pole `private final MySqlOrderRepository repository = new MySqlOrderRepository()` w produkcyjnym kodzie sklepu. → **SOLID**

Tutaj trzeba zastosować DIP, ponieważ będzie ciężko testować taki serwis.

---

### Pytania kontrolne

**1. Wymień trzy konkretne objawy, po których poznasz, że kod „prosi się o SOLID".**

Trudność testowania; rosnące `if/else` lub `switch` po typie, gdzie dokładamy `case` przy każdym nowym wariancie; klasa ma pola i metody z różnymi działaniami, a zmiana jednej rzeczy zmusza do grzebania w niezwiązanym kodzie.

**2. Wymień dwa konkretne objawy, po których poznasz, że SOLID byłby tu over-engineeringiem.**

Tworzymy interfejs na zapas, choć jest i będzie dokładnie jedna implementacja; albo kod jest krótki, jednorazowy, a abstrakcja miałaby więcej linii niż to, co opakowuje.

**3. Dlaczego „YAGNI bije DIP" w pewnych sytuacjach? Podaj przykład.**

Ponieważ DIP to dodanie dodatkowego interfejsu, wstrzykiwanie i dodanie zależności. A jeżeli implementacja jest i będzie tylko jedna, to dodajemy abstrakcję do abstrakcji.

**4. Czy SOLID to sekwencja kroków (S → O → L → I → D) przy projektowaniu nowej klasy? Uzasadnij.**

Nie. SOLID to narzędzie, a każda litera pokazuje jakąś zasadę, którą można by było zastosować. Nie jest to konieczne, ale w niektórych miejscach zalecane.

---

## 1. S — Single Responsibility Principle

**1. Klasa ma 12 metod publicznych. Czy narusza SRP? Od czego zależy odpowiedź?**

Jeżeli wszystkie metody służą jednej odpowiedzialności, to SRP nie jest naruszone. Czyli jeden powód do zmiany / jeden aktor decyduje o tym, czy klasa narusza SRP — a nie liczba metod.

**2. Dlaczego „Spróbuj opisać klasę bez słowa »i«" jest praktycznym testem SRP?**

Ponieważ sprawdza, ile odpowiedzialności ma klasa. Jeżeli klasa robi nowy order *i* zarządza dodaniem go do bazy, to SRP jest naruszone.

**3. Wymień trzy konkretne korzyści testowania, które pojawiły się po rozbiciu `OrderManager` na `OrderService` + 3 interfejsy.**

Łatwiej zrobić mocki i wstrzyknąć je do klasy; testujemy tylko `OrderService` zamiast 3 różnych, niezależnych funkcji jak generowanie PDF; nie musimy testować Repository w `OrderService`.

**4. Czy klasa `OrderService` po refaktorze nadal nie narusza SRP? Uzasadnij.**

Nie narusza. Klasa jest używana do tworzenia zamówienia — nie wie, jak działa baza danych, jak wysłać email ani jak wygenerować PDF. Jeden powód do zmiany == SRP spełnione.

---

## 2. O — Open/Closed Principle

**1. Co dokładnie znaczy „otwarte na rozszerzenie, zamknięte na modyfikację"?**

Otwarte na rozszerzenie oznacza — tak jak w naszym przykładzie z `DiscountPolicy` — że dodajemy nową klasę dla każdego rodzaju rabatu. W przyszłości, jeżeli chcemy dodać Valentine's Day discount, wystarczy stworzyć klasę i zaimplementować interfejs `DiscountPolicy`. Zamknięte na modyfikację oznacza, że istniejąca klasa `PriceCalculator` i inne klasy zostają nietknięte, więc nie trzeba ich testować ponownie.

**2. Pokaż konkretnie na kodzie: ile plików zmieniasz, dodając nowy typ klienta przed refaktorem, a ile po refaktorze?**

Przed refaktorem modyfikuję jeden działający plik. Po refaktorze tworzę jeden nowy plik i zero plików zmodyfikowanych.

**3. Czy każdy switch musi być rozbity na polimorfizm? Wymień co najmniej dwie sytuacje, w których lepiej zostawić switch.**

Nie. Można zostawić `switch`, kiedy nie trzeba go edytować za każdym razem, gdy dochodzi nowy wariant (zbiór wariantów jest stały), albo gdy logika jest prosta i jednorazowa.

**4. Jakie są minusy „przedwczesnego" stosowania OCP (interfejs dla jednej implementacji)?**

Abstrakcja w abstrakcji. Jeżeli dodamy OCP, ale będzie tylko jedna implementacja, to dostajemy martwy kod i gorszą czytelność — dodatkowy plik, który nic nie wnosi, a żeby sprawdzić, co się dzieje, musimy szukać implementacji interfejsu.

---

## 3. L — Liskov Substitution Principle

Klasyfikacja relacji:

| Relacja | Werdykt |
|---|---|
| Bird / Eagle | dziedziczenie |
| Bird / Penguin | LSP |
| BankAccount / SavingsAccount | LSP |
| Animal / Dog | dziedziczenie |
| Vehicle / ElectricCar | LSP |
| Vehicle / Tank (gąsienice zamiast kół) | dziedziczenie |

**1. Dlaczego „kwadrat to specjalny prostokąt" działa w matematyce, ale łamie LSP w kodzie?**

Ponieważ w matematyce każdy kwadrat to prostokąt, ale nie każdy prostokąt to kwadrat — czyli relacja *is-a* nie zawsze przekłada się na dziedziczenie. Modele obiektowe nie muszą odzwierciedlać świata rzeczywistego — muszą odzwierciedlać kontrakty zachowań. W kodzie `Rectangle` ma kontrakt `setWidth` i `setHeight`, które są niezależne. `Square` go nie dotrzymuje, bo ustawienie szerokości wymusza wysokość.

**2. Wymień trzy typowe naruszenia LSP, które łatwo przeoczyć w code review.**

1. Nowy typ wyjątku — podklasa rzuca wyjątek, którego klient klasy bazowej się nie spodziewa.
2. Wzmocniony warunek wstępny — podklasa wymaga więcej niż klasa bazowa.
3. Osłabiony warunek końcowy — po metodzie nie zachodzi gwarancja, którą dawała klasa bazowa.

**3. Co znaczy „faworyzuj kompozycję nad dziedziczeniem"? Pokaż konkretnie na klasie `SquareByComposition`.**

Zamiast dziedziczyć po `Rectangle`, dodajemy go jako pole `final` — czyli robimy relację *has-a* zamiast *is-a*. Pozwala to zapisać działanie klasy w inny sposób. Oznacza to, że `Square` nie jest typem `Rectangle` i nie zostanie wstawiony tam, gdzie oczekujemy `Rectangle` — czyli nie zostanie złamany żaden kontrakt.

**4. Jak rekordy w Javie pomagają unikać naruszeń LSP?**

Rekordy mają pola `private final` i nie mają setterów. A więc jak już utworzymy obiekt, to nie jesteśmy go w stanie zmienić i musi on przejść przez walidację konstruktora.

---

## 4. I — Interface Segregation Principle

**1. Skąd wiesz, że interfejs jest „za gruby"? Wymień dwa konkretne sygnały.**

Kiedy każdy dział używa tylko 2–3 metod z wielu wymuszanych przez interfejs, oraz kiedy w którejkolwiek metodzie rzucany jest `UnsupportedOperationException`.

**2. Czy ISP oznacza, że każdy interfejs ma mieć dokładnie jedną metodę? Uzasadnij.**

Nie. ISP oznacza, że każdy interfejs powinien mieć metody, które są używane i przeznaczone dla danego działu — a nie robienie 12 interfejsów z pojedynczą metodą.

**3. Jak ISP łączy się z LSP? Pokaż na przykładzie atrapy szerokiego interfejsu rzucającej `UnsupportedOperationException`.**

ISP wymusza implementację podobnie jak dziedziczenie — czyli pojawia się nowy typ wyjątku: podklasa rzuca wyjątek, którego klient klasy bazowej się nie spodziewa. Np. `UserRepository` zmusza `ReadOnlyUserRepo` do zaimplementowania `save()` / `delete()`, które nie są obsługiwane.

**4. Klasa `DatabaseUserRepository` implementuje 5 wąskich interfejsów. Czy to OK? Co by było problemem?**

Klasa implementuje tyle interfejsów, ile potrzebuje — w tym przypadku potrzebuje wszystkich, aby zarządzać repository. Problem pojawiłby się, gdyby np. `UserDisplayService` brał całość `DatabaseUserRepository` — wtedy zamiast widzieć tylko opcje `UserReader`, widzi wszystkie 12.

---

## 5. D — Dependency Inversion Principle

**1. Co dokładnie jest „odwrócone" w Dependency Inversion?**

Odwrócona jest zależność, w której klasa biznesowa współpracuje z repo. W pierwszym przypadku tworzyliśmy repo wewnątrz klasy biznesowej, a w drugim dodaliśmy interfejs, który wymuszał implementację na Repository. Czyli wysoki poziom zależy od abstrakcji, a nie od konkretu.

**2. Dlaczego konstruktorowe DI jest lepsze niż setterowe? Pokaż konkretny scenariusz, w którym setter zawodzi.**

Wstrzykiwanie konstruktorowe jest robione raz i sprawdzane przez konstruktor, więc nie da się przekazać `null`. Przy setterze możemy podać obiekt, którego nie chcemy (np. obiekt zostaje niezainicjalizowany lub nadpisany w trakcie życia obiektu).

**3. Jak DIP łączy się z testowalnością? Pokaż konkretną korzyść na przykładzie `OrderService`.**

Do `OrderService` możemy podać bazę danych, która może się różnić — np. H2 in-memory database do testów, a potem zamienić ją na SQL albo NoSQL. I wszystko będzie działać również w mocku.

**4. Kiedy DIP jest over-engineeringiem? (Wskazówka: YAGNI)**

Kiedy każdy interfejs miałby np. tylko 1 małą metodę. Samo formowanie interfejsów i ich dziedziczenie kosztuje wtedy więcej kodu niż napisanie 2–3 małych metod.

**5. Czym różni się klasyczna zależność od odwróconej? Narysuj strzałkę zależności w obu wariantach.**

```
Klasyczna zależność:   Klasa biznesowa ──używa──▶ Repository

Odwrócona zależność:   Klasa biznesowa ──używa──▶ Interfejs ◀──implementuje── Repository
```

---

## 6. Mini Projekt

**1. Co konkretnie zyskałeś, zamieniając `Map<String, Object>` na rekord `User`?**

Niemodyfikowalny obiekt `User`, który ma swoje konkretne pola, których nie można nadpisać. Record dodaje nam też bezpieczeństwo typów.

**2. Wymień trzy zmiany biznesowe, które byłyby trudne w wersji przed refaktorem, a są łatwe po.**

Dodanie nowej reguły walidacji, wymiana bazy danych, wymiana kanału powiadomień.

**3. Gdzie konkretnie zostały zastosowane SRP, OCP, DIP w docelowej architekturze? Wskaż konkretne klasy / interfejsy.**

- **SRP** — `UserService`, `UserValidator`, `InMemoryRepo`
- **OCP** — `ValidationRule` + `UserValidator`
- **DIP** — interfejs `UserRepository`

**4. Klasa `ValidationRule` to interfejs z jedną metodą. Czy to nie narusza zasady „nie twórz interfejsu dla jednej implementacji" (YAGNI)?**

Nie, ponieważ zasada YAGNI dotyczy liczby implementacji, a nie liczby metod.

**5. Jeśli właściciel sklepu mówi „dodajemy regułę: email nie może kończyć się na .ru" — ile plików musisz zmienić w wersji po refaktorze? A w wersji przed?**

Po refaktorze dodajemy nową `Rule` do listy w `UserValidator` i tyle. A przed refaktorem musimy dodać zasadę do walidacji w `UserManager` — czyli modyfikujemy działający kod.

**6. Co byłoby trudne dodać w wersji God Class, a łatwe po refaktoryzacji? Wymień co najmniej cztery rzeczy.**

Nowa reguła walidacji, wymiana bazy, wymiana powiadomień, izolowany test.

**7. Dlaczego refaktor „wszystko naraz w jednym PR" jest złym pomysłem? Co stracisz?**

Ponieważ jeżeli coś źle zrefaktorujemy, to musimy wrócić do wersji sprzed całego refaktoru zamiast do konkretnego commita — co często marnuje czas, bo może 3 z 5 rzeczy były zrobione dobrze.