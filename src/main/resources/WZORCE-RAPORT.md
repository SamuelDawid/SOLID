# Wzorce projektowe — pytania kontrolne z odpowiedziami

## 1. Singleton

**1. Dlaczego konstruktor Singletona musi być `private`? Co by się stało, gdyby był `public`?**

Ponieważ klasa ma mieć tylko jeden obiekt, więc nie powinniśmy móc tworzyć go przez `new` — pracujemy na raz utworzonej instancji albo inicjalizujemy ją przy pierwszym użyciu. Gdyby konstruktor był `public`, nie byłby to już Singleton, bo można by utworzyć wiele obiektów tej klasy.

**2. Wytłumacz, dlaczego DCL wymaga słowa kluczowego `volatile`. Jaki konkretnie problem (z modelu pamięci) to naprawia?**

Operacja `new` składa się z trzech kroków: alokacji pamięci, wywołania konstruktora i przypisania referencji — w tej kolejności. `volatile` tę kolejność gwarantuje; bez niego JIT lub procesor mógłby ją zmienić.

**3. Wymień co najmniej dwa powody, dla których enum jest bezpieczniejszą implementacją Singletona niż klasyczna klasa z polem static.**

Serializacja — deserializacja enuma zwraca dokładnie tę samą instancję. Refleksja — `Constructor.newInstance` na enumie rzuca `IllegalArgumentException`. Bezpieczeństwo wątkowe jest gwarantowane przez JVM, bez dodatkowej logiki. Instancja jest leniwa — powstaje przy pierwszym użyciu enuma.

**4. Singleton klasyczny (`getInstance()`) vs „bean singleton" w kontenerze DI — gdzie leży różnica?**

Klasyczny Singleton sam pilnuje swojej jedyności w skali JVM i używa się go statycznie. „Bean singleton" to zwykła klasa, której jedyność — w skali kontenera — zapewnia i którą wstrzykuje kontener DI, co usuwa globalny stan i daje testowalność.

**5. Wymień 3 sytuacje, w których Singleton jest anty-wzorcem.**

Testy mogą być utrudnione, bo wiele frameworków testujących polega na dziedziczeniu przy tworzeniu atrap obiektów, a konstruktor Singletona jest prywatny i nadpisanie metod statycznych jest niemożliwe.

Kod korzystający z globalnego Singletona staje się silnie powiązany, a przepływ danych jest mniej czytelny niż przy jawnym przekazywaniu zależności.

Instancja utworzona zgodnie z Singletonem może przechowywać dane modyfikowane w trakcie działania programu (mutowalny stan globalny).

**6. Kiedy wybrałbyś EAGER, a kiedy LAZY z DCL? Podaj przykład biznesowy dla każdego.**

Wszystko zależy od tego, czy obiekt jest potrzebny od razu, czy dopiero w trakcie działania programu. EAGER stosujemy, gdy tworzenie obiektu nie zużywa dużo pamięci ani czasu procesora, gdy instancja jest potrzebna przy każdym uruchomieniu aplikacji — i dostajemy bezpieczeństwo wątkowe w prosty sposób. LAZY stosujemy, gdy obiekt jest kosztowny — np. parsowanie dużych plików, kosztowne połączenie z bazą danych lub alokacja dużej ilości pamięci — a nie zawsze jest używany; dzięki temu aplikacja uruchamia się szybciej, odkładając tworzenie ciężkich obiektów na później.

**7. Klasa `AppConfigEager` ma instancję tworzoną przy ładowaniu klasy. Jeśli klasa nigdy nie zostanie zreferencjonowana w aplikacji, czy konstruktor zostanie wywołany?**

Nie. „Eager" znaczy „od razu, gdy klasa zostanie zainicjalizowana", ale JVM inicjalizuje klasy leniwie — dopiero przy pierwszym użyciu (np. `new` lub dostęp do statycznego pola). Jeśli klasa nigdy nie zostanie zreferencjonowana, konstruktor się nie wykona.

---

## 2. Factory Method

**1. Dlaczego fabryka musi zwracać interfejs/abstrakcyjny typ, a nie konkretną klasę?**

Fabryka zwraca interfejs, bo klient zna tylko ten interfejs, a nie jego konkretną implementację.

**2. Czym różni się Factory Method od bezpośredniego użycia konstruktora `new`?**

Przy `new` klient musi znać konstruktor; fabryka ukrywa tworzenie obiektu — podajemy parametr i dostajemy abstrakcję, nie wiedząc, którą klasę i jak zbudowano.

**3. Wymień 3 statyczne metody fabrykujące z JDK i powiedz, jakie korzyści daje ich istnienie zamiast `new`.**

`List.of` — zwraca podtyp klasy, którego nie znamy. `Optional.of` — pozwala tworzyć obiekty metodami o różnych, opisowych nazwach, czego konstruktor nie umożliwia. `Integer.valueOf` — może zwrócić instancję z cache zamiast zawsze alokować nowy obiekt.

**4. Co to znaczy, że fabryka może być „rozszerzalna w runtime"? Jak to osiągnąć (rejestr fabryk)?**

Oznacza to, że mamy mapę, w której pod kluczami przechowujemy typy `Notification`. W trakcie działania programu sprawdzamy w rejestrze (mapie), czy dany typ istnieje; jeśli tak — zwracamy obiekt, a jeśli nie — tworzymy go w trakcie działania i dodajemy do rejestru.

**5. Kiedy Factory Method jest przesadą?**

Przesadą jest tworzenie fabryki dla 1–2 typów — lepiej wtedy użyć zwykłego konstruktora.

**6. Co jest lepsze: `Integer.valueOf(42)` czy `new Integer(42)`? Dlaczego (JDK to celowo deprecuje)?**

`valueOf` zwraca wartość z cache — czyli już istniejącą — zamiast tworzyć nowy obiekt.

---

## 3. Abstract Factory

**1. Czym różni się Abstract Factory od Factory Method?**

Factory Method tworzy tylko jeden typ produktu, natomiast Abstract Factory tworzy całą rodzinę spokrewnionych produktów (przez wiele metod) i gwarantuje ich spójność.

**2. Co to znaczy „rodzina obiektów" w Abstract Factory? Podaj przykład z UI lub bazą danych.**

Fabryka motywu ciemnego (Dark theme) zwróci `Button`, `Checkbox` i `Scrollbar` w ciemnym odcieniu, a fabryka motywu jasnego (Light theme) zrobi to samo, ale w jasnym odcieniu.

**3. Co się stanie, jeśli klient sam stworzy `new PdfHeader()` i połączy go z `HtmlBody()`? Dlaczego Abstract Factory tego nie pozwala?**

Powstanie type mismatch — uszkodzony raport. Abstract Factory chroni przed tym, bo wszystkie obiekty tworzy jedna fabryka, która dodatkowo ukrywa sposób ich powstawania, więc klient nigdy nie wie o `PdfHeader`.

**4. Dlaczego dodanie nowego komponentu (np. „logo") do rodziny w Abstract Factory jest droższe niż dodanie nowej rodziny?**

Bo musimy dodać 1 nowy interfejs + 3 implementacje + zmodyfikować 3 fabryki + 1 wywołanie — i to jest właśnie główna wada tego wzorca.

**5. Wymień przykład Abstract Factory z JDK.**

`DocumentBuilderFactory`.

**6. Jaki jest podstawowy „test" sensu Abstract Factory — czy potrzebujesz tworzyć zestaw spójnych obiektów?**

Tak, potrzebujesz. Zestaw spójnych obiektów pokazuje, ile ich jest, i pozwala zdecydować, czy opłaca się pisać Abstract Factory.

---

## 4. Builder

**1. Dlaczego konstruktor klasy docelowej (`Email`) jest `private`?**

Bo tylko Builder może utworzyć obiekt, więc obiekt nigdy nie jest zbudowany tylko w połowie i nigdy nie jest później modyfikowany.

**2. Czemu walidacja musi być w `build()` (lub w konstruktorze klasy docelowej), a nie w setterach Buildera?**

Bo walidacja w jednym miejscu od razu daje informację, czy przy budowie wszystko jest poprawne. Gdyby była w setterach, obiekt mógłby być w połowie utworzony i nie dałoby się sprawdzić walidacji dla pól, które jeszcze nie zostały ustawione.

**3. Wymień dwie kategorie pól w Builderze: wymagane i opcjonalne. Jak każda jest obsługiwana?**

Pole wymagane to takie, dla którego dostaniemy wyjątek, jeśli go zabraknie. Pole opcjonalne to takie, które — jeśli go nie ustawimy — dostanie wartość domyślną i można je pominąć.

**4. Co znaczy „defensywna kopia" listy `attachments` i dlaczego jest potrzebna?**

Defensywna kopia to nowa, niezależna kopia listy (a nie referencja do tego samego obiektu) — inaczej moglibyśmy zmodyfikować obiekt, który ma być niezmienny.

**5. Dlaczego każdy setter Buildera zwraca `this`? Co się stanie, gdy zwróci `void`?**

Dzięki temu możemy zapisać `from(...).subject(...).build()`. Gdyby setter zwracał `void`, nic by nie zwracał i łańcuch wywołań by się rozpadł.

**6. Kiedy preferować Lombok `@Builder` nad ręczną implementacją, a kiedy odwrotnie?**

Gdy mamy tzw. pure boilerplate — same pola bez walidacji — Lombok jest w porządku. Gdy potrzebujemy prawdziwej walidacji albo wartości domyślnych obliczanych automatycznie, lepsza jest implementacja ręczna.

**7. Wymień 2 Buildery z JDK lub bibliotek.**

`StringBuilder`, `Stream.builder()`, `HttpRequest.newBuilder()`.

**8. Czy record może mieć Builder? Co Builder dodaje do recordu, którego record sam nie ma?**

Tak, rekord może mieć Builder. Builder dodaje opcjonalne parametry, wartości domyślne i czytelną konstrukcję, gdy pól jest dużo.

---

## 5. Prototype

**1. Wytłumacz różnicę między płytką (shallow) a głęboką (deep) kopią. Podaj scenariusz, kiedy płytka jest pułapką.**

Płytka kopia kopiuje bezpośrednio pola prymitywne, ale referencje do obiektów nadal wskazują ten sam zagnieżdżony obiekt co w oryginale. Głęboka kopia kopiuje również te zagnieżdżone obiekty, więc kopia i oryginał nie współdzielą żadnego mutowalnego stanu. Płytka kopia bywa pułapką, gdy np. dwie instancje dokumentu współdzielą tę samą listę — wtedy zmiana zawartości współdzielonej listy jest widoczna w obu.

**2. Dlaczego Joshua Bloch rekomenduje copy constructor zamiast `Cloneable`/`clone()`?**

`Cloneable` jest słabo zaprojektowany: zwraca `Object`, który trzeba rzutować z powrotem, rzuca sprawdzany wyjątek `CloneNotSupportedException` i domyślnie daje płytką kopię. Copy constructor albo statyczne `copyOf` pozwala uniknąć tych problemów.

**3. Czym przykład `ServerConfig.withHost(...)` różni się od `DocumentTemplate.copy()` koncepcyjnie?**

`DocumentTemplate.copy()` duplikuje dokładny stan obiektu do nowej, niezależnej instancji, dzięki czemu duplikat można zmieniać bez wpływu na oryginał. `ServerConfig.withHost(...)` działa na obiekcie niezmiennym, w którym nie można zmienić pola, więc zwraca nową instancję identyczną poza jedną zmienioną wartością.

**4. Gdzie w popularnych frameworkach Javy spotkasz Prototype?**

W Springu — zakres (scope) `prototype`: kontener tworzy nową instancję za każdym razem, gdy bean jest potrzebny.

**5. Czy record (Java 14+) potrzebuje Prototype? Jaką jego namiastkę daje sam record?**

Nie, rekord nie potrzebuje Prototype, bo jest niezmienny — zmianę jednego pola osiąga się przez konstruktor. Rekord daje też kanoniczny/kompaktowy konstruktor oraz dekonstrukcję przez pattern matching.

**6. Dlaczego dla niemutowalnej konfiguracji `withXxx` zwraca nową instancję, a nie modyfikuje obecnej?**

Bo obiekt jest niezmienny — ma pola `final` i nie ma setterów — więc jedyną opcją jest zwrócenie nowej instancji ze zmodyfikowaną wartością.

**7. Wymień scenariusz biznesowy, w którym Prototype jest tańszy niż klasyczny `new` + settery.**

Gdy obiekt jest bardzo złożony/kosztowny w utworzeniu, a potrzebujemy kilku wariantów. Wtedy możemy zbudować go raz, a następnie skopiować i zmienić to, co chcemy.

---

## 6. Adapter

**1. Czym różni się object adapter (kompozycja) od class adapter (dziedziczenie)? Który Java preferuje i dlaczego?**

Adapter obiektowy przechowuje referencję do adaptowanego obiektu i implementuje interfejs docelowy; każda metoda deleguje do adaptowanego obiektu i tłumaczy parametry/wyniki. Adapter klasowy rozszerza adaptowany obiekt i implementuje interfejs docelowy. Java woli adapter obiektowy, bo nie ma wielodziedziczenia klas, a kompozycja pozwala adaptować dowolną instancję i swobodnie ją podmieniać.

**2. Wymień dwa przykłady Adaptera z JDK i opisz, co i do czego adapteruje.**

`InputStreamReader` — tłumaczy strumień bajtów na znaki, używając charsetu. `Arrays.asList` — tłumaczy zwykłą tablicę na interfejs `List`, dzięki czemu możemy używać tablicy w miejscu listy. `Collections.enumeration` — tłumaczy `Collection`/`Iterator` na stary interfejs `Enumeration`.

**3. Adapter zmienia interfejs, ale czy zmienia zachowanie? Jaka jest jego intencja?**

Nie — adapter zmienia interfejs, ale nie zmienia zachowania. Jego zadaniem jest połączyć dwa niekompatybilne interfejsy w jeden działający.

**4. Czemu w `StripeAdapter` mamy konwersję PLN → grosze, a nie po prostu `(long) amountPln`?**

Bo pieniądze mają część po przecinku, a `(long) amountPln` ją obcina; do tego liczby zmiennoprzecinkowe bywają niedokładne (np. 19.99 może być zapisane jako 19.9899...). Dlatego konwertujemy na grosze przez `Math.round(amountPln * 100)`, żeby nie tracić części po przecinku.

**5. Co się stanie, jeśli `LegacyStripeApi` zmieni sygnaturę `charge(...)` w nowej wersji? Co musisz zmienić w klientach?**

Tylko adapter. Klient zależy od interfejsu `PaymentProcessor`, który się nie zmienia — całe powiązanie ze Stripe znajduje się w `StripeAdapter`.

**6. Kiedy Adapter jest anty-wzorcem?**

Gdy oba interfejsy są niemal identyczne i różnią się nieznacznie. Wtedy lepiej dostosować jeden z interfejsów, niż dodawać kolejną warstwę.

**7. Czy Adapter to to samo co Facade? Jaka jest różnica intencji?**

Nie. Adapter konwertuje istniejący interfejs na inny, oczekiwany przez klienta, aby umożliwić współpracę dwóch niekompatybilnych części. Facade zapewnia nowy, prostszy interfejs do złożonego podsystemu wielu klas.

---

## 7. Decorator

**1. Wytłumacz, czym dekorator różni się od proxy (na poziomie intencji, nie struktury).**

Dekorator dodaje zachowanie do obiektu, a proxy kontroluje dostęp do obiektu.

**2. Czy kolejność dekoratorów ma znaczenie? Podaj przykład, w którym `A(B(x))` daje inny wynik niż `B(A(x))`.**

Tak, ma znaczenie. Np. `Encrypt(Compress(x))` działa dobrze, ale po odwróceniu tych operacji prawie nic nie zostanie skompresowane, bo zaszyfrowane dane wyglądają losowo i nie nadają się do kompresji.

**3. Wymień strumień z `java.io`, który jest klasycznym dekoratorem. Jakie zachowanie dodaje?**

`BufferedReader` — opakowuje `Reader`/`InputStream` i dodaje buforowanie.

**4. Dlaczego zamiast 256 klas wystarczy 1 klasa bazowa + N dekoratorów?**

Dekoratory komponują się w czasie wykonywania, więc N dekoratorów daje wszystkie 2^N kombinacji przy N klasach.

**5. Co by się stało, gdyby `MilkDecorator` rozszerzał `Espresso` zamiast implementować `Coffee`?**

Dekorowałby tylko `Espresso`, a nie `Americano` ani żadną inną kawę. Dekorowanie na interfejsie `Coffee` pozwala opakować dowolną kawę.

**6. Czy dekorator może dziedziczyć po dekorowanym obiekcie? Jakie są tego konsekwencje?**

Może — na poziomie klas — ale wtedy wiąże dekorator z jednym typem i nie pozwala opakować dowolnych implementacji.

**7. Jak dekorator ma się do zasady „kompozycja > dziedziczenie"?**

Preferowana jest kompozycja względem interfejsu. Zgodnie z zasadą „kompozycja > dziedziczenie" zachowanie dodaje się przez opakowanie (kompozycję) w czasie wykonywania, a nie przez tworzenie podklasy dla każdej kombinacji.

---

## 8. Facade

**1. Czym fasada różni się od adaptera? Oba ukrywają coś za sobą — czym?**

Adapter przekształca jeden niekompatybilny interfejs na inny, kompatybilny. Facade daje jeden prostszy interfejs zamiast skomplikowanego systemu. Adapter ukrywa niekompatybilny interfejs, a facade ukrywa złożoność systemu.

**2. Dlaczego `OrderFacade.placeOrder(...)` ma rollback płatności, gdy magazyn nie ma towaru?**

Bo `OrderFacade` organizuje wieloetapową transakcję; jeśli późniejszy etap się nie powiedzie, wcześniejszy sukces (płatność) musi zostać cofnięty — w przeciwnym razie klient zostanie obciążony bez powodu.

**3. Jakie elementy aplikacji powinny być za fasadą, a jakie nie?**

Złożone podsystemy z kilkoma skoordynowanymi klasami powinny być za fasadą. Proste, jednometodowe klasy — nie.

**4. Czy fasada może mieć swoją własną logikę biznesową? Czym fasada różni się od zwykłej klasy „Service"?**

Fasada głównie koordynuje podsystemy i deleguje, a Service implementuje logikę domenową. To różne role; gdy fasada jest przeładowana własną logiką, robi się z niej god class.

**5. Wymień przykład fasady z JDK lub biblioteki Java.**

SLF4J (logger); `java.net.URL`/`URLConnection` — ukrywa złożoność sieci.

**6. Co znaczy „god class" w kontekście fasady i jak tego uniknąć?**

Gdy fasada zamiast delegować zaczyna implementować logikę biznesową. Można tego uniknąć, dzieląc ją na skoncentrowane fasady, a logikę biznesową zostawiając serwisom.

**7. Jak fasada wspiera zasadę Demeter (Law of Demeter)? Czy zmniejsza, czy zwiększa sprzężenie?**

Zmniejsza je. Klienci komunikują się tylko z fasadą zamiast korzystać z łańcuchów typu `a.getB().getC().doX()`; fasada staje się jedynym bezpośrednim współpracownikiem.

---

## 9. Proxy (Static + Dynamic Proxy)

**1. Czym Proxy różni się od Decorator? (Jedna odpowiedź dotyczy intencji — jakiej?)**

Celem proxy jest kontrola dostępu, a dekorator dodaje zachowanie.

**2. Wyjaśnij, dlaczego Dynamic Proxy wymaga interfejsu. Co zrobiłbyś, gdyby twoja klasa nie miała interfejsu?**

`java.lang.reflect.Proxy` generuje w czasie wykonywania klasę implementującą dany interfejs, więc obiekt musi go udostępniać. Bez interfejsu używamy proxy opartego na podklasie (subclass-based proxy, np. CGLIB/ByteBuddy).

**3. Dlaczego wywołanie `this.metoda()` z wnętrza klasy omija Proxy w nowoczesnych frameworkach?**

Bo proxy opakowuje obiekt z zewnątrz, a wywołanie `this` idzie bezpośrednio do prawdziwego obiektu i pomija wrapper, więc logika proxy się nie uruchamia.

**4. Wymień 3 cross-cutting concerns, które naturalnie pasują do Proxy.**

Logowanie, cache, autoryzacja, audyt.

**5. Jakie 3 elementy musisz przekazać do `Proxy.newProxyInstance(...)`?**

`ClassLoader`, tablicę interfejsów do zaimplementowania oraz `InvocationHandler`.

**6. Co robi `method.invoke(target, args)` wewnątrz `InvocationHandler.invoke`? Dlaczego nie wywołujesz target bezpośrednio?**

Refleksyjnie wywołuje rzeczywistą, przechwyconą metodę, przekazując argumenty. Refleksja jest konieczna, bo handler jest generyczny i nie zna metody w czasie kompilacji.

**7. Co znaczy „stackowanie" proxy? Podaj przykład sensownej kolejności.**

Opakowywanie kilku proxy, z których każdy wprowadza jeden aspekt. Sensowna kolejność: auth → caching → logging.

---

## 10. Composite

**1. Czym różni się liść od composite w tym wzorcu?**

Liść nie ma dzieci, a composite ma dzieci i implementuje ten sam interfejs, delegując do nich operacje.

**2. Dlaczego klient nie potrzebuje wiedzieć, czy obiekt to plik czy folder?**

Bo oba implementują ten sam interfejs, więc klient wywołuje tę samą metodę jednolicie.

**3. Wymień przykład Composite z biblioteki UI Javy (Swing/JavaFX).**

Swing `Container`/`JComponent` — np. `JPanel`, który zawiera wiele komponentów.

**4. Jak wzorzec radzi sobie z metodami typu `add(child)` — czy powinny być w interfejsie, czy tylko w composite?**

Tradycyjnie `add(child)` powinno znajdować się w composite — to kompromis między przejrzystością (transparency) a bezpieczeństwem typów (type safety).

**5. Co się stanie, jeśli struktura ma cykl (A zawiera B, B zawiera A)? Jak temu zapobiec?**

Nieskończona rekurencja / przepełnienie stosu. Można temu zapobiec, wymuszając strukturę drzewa.

**6. Jak Composite ma się do struktury DOM w HTML / XML?**

DOM to drzewo composite — element zawiera węzły potomne (child nodes).

**7. Wymień scenariusz biznesowy, w którym Composite jest naturalny.**

System plików, schemat organizacyjny, zagnieżdżone menu, zagnieżdżony układ UI.

---

## 11. Strategy (z lambdami w Java 8+)

**1. Czym Strategy różni się od Factory Method?**

Factory tworzy obiekty, a Strategy wykonuje wymienny algorytm — jeden zwraca instancję, drugi hermetyzuje uruchamiane przez nas zachowanie.

**2. Dlaczego Java 8+ ułatwia Strategy?**

Strategia to zwykle interfejs jednometodowy (funkcyjny), więc przekazujemy lambdę zamiast całej klasy.

**3. Wyjaśnij, dlaczego dispatch table (Map) jest lepsza od switch. Co możesz w runtime zmienić w mapie, czego nie zmienisz w switch?**

Mapa umożliwia dodawanie/zastępowanie/usuwanie wpisów w czasie wykonywania, a `switch` jest ustalany w czasie kompilacji i trzeba go edytować oraz rekompilować, żeby dodać nowy przypadek.

**4. Wymień klasyczny przykład Strategy w JDK.**

`Comparator` przekazywany do `Collections.sort`/`List.sort`.

**5. Co znaczy „kompozycja strategii"? Podaj przykład.**

Łączenie małych strategii w jedną większą.

**6. Czy `Comparator` to Strategy? Uzasadnij.**

Tak. Zawiera wymienny algorytm porównania przekazywany do kontekstu, który go wykorzystuje, nie znając konkretnej logiki.

**7. Kiedy Strategy jest przesadą?**

Gdy można go zastąpić prostym `if/else`, np. dwa stałe warianty.

**8. Czy strategia powinna trzymać stan?**

Najlepiej nie — czysty algorytm powinien być bezstanowy.

---

## 12. Observer (PropertyChangeSupport + ręcznie)

**1. Wytłumacz, dlaczego publisher (`OrderService`) nie powinien znać konkretnych listenerów.**

Aby zachować separację — wydawca zależy wyłącznie od interfejsu nasłuchującego, więc subskrybentów można dodawać i usuwać bez zmiany wydawcy.

**2. Dlaczego pętla `publish` ma try/catch wokół każdego listenera? Co się stanie bez tego?**

Bo wyjątek w jednym ze słuchaczy zatrzymałby pętlę i reszta powiadomień by nie przeszła. `try/catch` izoluje błędy.

**3. Co to jest `CopyOnWriteArrayList` i dlaczego pasuje do EventBus?**

Bezpieczna dla wątków lista, która kopiuje swoją tablicę przy każdym zapisie. Pasuje, bo liczba odczytów znacznie przewyższa liczbę zapisów.

**4. Czym `PropertyChangeSupport` różni się od ręcznego EventBus? Gdzie używałbyś każdego?**

`PropertyChangeSupport` jest wbudowany w JDK do powiadamiania o zmianach właściwości beanów; własny EventBus obsługuje wiele typów zdarzeń domenowych w całej aplikacji.

**5. Wymień klasyczny problem memory leak w Observer i jak go uniknąć.**

Występuje, gdy obserwator rejestruje się w obiekcie obserwowanym (Subject), ale zapomina się wyrejestrować po zakończeniu swojego cyklu życia.

**6. Jak Observer ma się do event-driven architecture i message brokerów (Kafka, RabbitMQ)?**

To technologie służące do budowy systemów luźno powiązanych, w których komponenty komunikują się bez bezpośredniej zależności.

**7. Jak przetestować, że listener X obsługuje event Y?**

Sprawdzić (np. mockiem), że zdarzenie Y zostało obsłużone przez słuchacza X — że odpowiednia metoda została wywołana.

**8. Co odróżnia Observer od Mediator?**

W Observerze wydawca nie zna subskrybentów; Mediator zna wszystkich uczestników i koordynuje ich interakcje.

---

## 13. Command (z Undo)

**1. Czym Command różni się od Strategy?**

Strategia hermetyzuje wymienny algorytm podłączony do kontekstu. Command hermetyzuje całe żądanie/akcję (odbiorca + parametry) jako obiekt, dzięki czemu można je przechowywać, kolejkować, rejestrować i cofać.

**2. Wymień 4 elementy wzorca Command.**

Odbiorca (wykonuje pracę), Polecenie (akcja + odbiorca), Wywołujący (uruchamia), Klient (tworzy/konfiguruje).

**3. Czemu makro (`MacroCommand`) cofa w odwrotnej kolejności?**

Bo akcje są uporządkowane, więc aby cofnięcie było poprawne, trzeba je wykonywać od ostatniej operacji (LIFO).

**4. Jak komenda zapamiętuje stan potrzebny do undo? Wymień strategię z `DeleteCommand`.**

Memento — przechowuje informacje potrzebne do cofnięcia operacji. `DeleteCommand` zapisuje usunięty element i jego pozycję, a przy cofnięciu wstawia go z powrotem.

**5. Jak `Runnable` ma się do Command?**

`Runnable` to Command — jego funkcyjna postać, bez możliwości cofania (undo).

**6. Wymień przykład Command w GUI Javy.**

Swing `Action`/`AbstractAction` powiązane z przyciskami i elementami menu.

**7. Co się stanie, jeśli klient wywoła `undo` 100 razy z pustym stosem?**

Nic się nie stanie — stos jest pusty.

**8. Kiedy NIE należy używać Command?**

Prosty CRUD bez cofania, historii, kolejkowania ani rejestrowania.

**9. Jak Command pasuje do kolejkowania (queue) i retry?**

Akcja jest niezależnym obiektem, więc może trafić do kolejki, zostać uruchomiona później lub w innym miejscu i uruchomiona ponownie przy błędzie (retry).

---

## 14. Template Method

**1. Co znaczy „template method" — czy to klasa, metoda, czy oba?**

Template method oznacza metodę w abstrakcyjnej klasie bazowej, która definiuje stały szkielet algorytmu i wywołuje nadpisywane kroki. Wzorzec używa klasy bazowej i tej metody.

**2. Dlaczego metoda template (np. `importData`) jest często `final`?**

Aby zablokować strukturę i kolejność kroków, żeby podklasy nie mogły ich zmieniać.

**3. Co to jest hook w Template Method? Podaj przykład.**

Krok z domyślną (często pustą) implementacją, którą podklasy mogą nadpisać, ale nie muszą.

**4. Czym Template Method różni się od Strategy?**

Metoda szablonowa zmienia kroki przez dziedziczenie, a strategia zmienia cały algorytm przez kompozycję.

**5. Wymień klasyczny przykład Template Method w JDK.**

`HttpServlet` (`service()` to szablon, nadpisujemy `doGet`/`doPost`); `AbstractList` (implementujemy `get`/`size`).

**6. Czy podklasa może zmienić kolejność kroków w algorytmie? Dlaczego nie?**

Nie — kolejność znajduje się w metodzie szablonowej; podklasy jedynie wypełniają treść kroków.

**7. Jak Template Method ma się do zasady „kompozycja > dziedziczenie"?**

Gdy dziedziczenie jest wymagane, baza jest właścicielem szkieletu, a podklasy go rozszerzają. Strategia to alternatywa oparta na kompozycji, zapewniająca elastyczność w czasie wykonywania.

**8. Co odróżnia metodę `abstract` od metody z domyślną implementacją (hook) w klasie bazowej?**

Metoda `abstract` musi zostać zaimplementowana, a hook ma wartość domyślną i może być nadpisany.

---

## 15. Iterator (własny + integracja z Iterable)

**1. Wymień dwa interfejsy potrzebne, by twoja kolekcja działała w for-each.**

`Iterable<T>` i `Iterator<T>`.

**2. Jakie dwie metody ma `Iterator<T>`? Co każda robi?**

`hasNext()` (czy istnieje kolejny element?) i `next()` (zwraca następny i przesuwa się dalej). Opcjonalnie `remove()`.

**3. Dlaczego konwencja mówi „każde wywołanie `iterator()` to nowy, świeży iterator"?**

Dzięki temu niezależne iteracje w tym samym czasie nie kolidują ze sobą.

**4. Jak iterator może ukrywać złożoną strukturę (drzewo)? Co robi `Tree.iterator()`?**

Ujawnia elementy pojedynczo w wybranej kolejności, ukrywając sposób przeglądania. `Tree.iterator()` przeszukuje drzewo wewnętrznie, dzięki czemu klient iteruje bez znajomości struktury.

**5. Czy iterator musi wskazywać na istniejącą strukturę?**

Nie — może generować w locie. `RangeIterator` oblicza kolejną liczbę w `next()` bez przechowywania danych.

**6. Co to jest `ConcurrentModificationException` i kiedy się pojawia?**

Wyjątek rzucany, gdy kolekcja jest strukturalnie modyfikowana podczas iteracji.

**7. Jak iterator współgra ze Stream w Javie?**

Oba generują elementy leniwie; można je ze sobą łączyć.

**8. Czy `for (int i = 0; i < list.size(); i++)` to też Iterator?**

Nie, to pętla indeksowana. Iterator nie zna indeksu — zna tylko `hasNext` i `next` — więc działa na strukturach bez dostępu swobodnego.

**9. Wymień klasyczną sytuację, w której musisz napisać własny Iterator.**

Iterowanie niestandardowej/niekolekcyjnej struktury (drzewo, graf, podzielony na strony interfejs API, generowana sekwencja), dla której nie ma gotowej kolekcji JDK.

---

## 16. State

**1. Czym State różni się od Strategy?**

Warianty strategii są niezależne i wybierane przez klienta. Warianty stanu to cykl życia obiektu i przejścia między nimi w zależności od akcji; zachowanie zmienia się wraz ze zmianą stanu wewnętrznego.

**2. Dlaczego context (`Order`) nie ma ani jednego `if/else` w metodach `pay/ship`?**

Zachowanie jest delegowane do bieżącego obiektu stanu; każdy stan implementuje `pay`/`ship` po swojemu — polimorfizm zastępuje warunki.

**3. Kto decyduje o przejściu między stanami — Context czy State?**

Zazwyczaj sam stan (każdy stan zna swojego następcę i wywołuje `setState` kontekstu). Kontekst też może to kontrolować, ale GoF umieszcza to w stanach.

**4. Wyjaśnij, dlaczego setter `setState(...)` w `Order` jest `package-private`, a nie `public`.**

Aby tylko stany (z tego samego pakietu) mogły zmieniać stan zamówienia — kod zewnętrzny nie może wymusić nielegalnego przejścia. Hermetyzuje to maszynę stanów.

**5. Wymień przykład State w JDK.**

`Thread.State` (NEW, RUNNABLE, BLOCKED, WAITING, TERMINATED) — dozwolone zachowanie zależy od bieżącego stanu.

**6. Jak State pomaga w testowaniu?**

Każdy stan jest osobną klasą, którą można testować w izolacji.

**7. Czy `RefundedState.pay()` powinien zwracać do `RefundedState` czy do nowego stanu „refundowane drugi raz"?**

To decyzja projektowa. Jeśli drugi zwrot jest bezsensowny — zostań w `RefundedState`; jeśli ma wyraźne znaczenie — utwórz nowy stan.

**8. Kiedy NIE używać State?**

Dla 2 stanów wystarczy wartość logiczna (boolean); dla ~3 stanów ze stabilną logiką — enum. Pełne klasy stanów opłacają się przy wielu stanach i bogatym, nasyconym przejściami zachowaniu.

---

## 17. Chain of Responsibility

**1. Co decyduje, czy handler obsłuży żądanie, czy przekaże dalej?**

Każdy handler sprawdza, czy żądanie pasuje do jego odpowiedzialności. Jeśli może je obsłużyć — obsługuje (i może je zatrzymać); w przeciwnym razie przekazuje je następnemu.

**2. Co się stanie, gdy handler ZAPOMNI wywołać `passToNext`?**

Łańcuch w tym miejscu się zrywa — żądania, których nie obsłużono, są po prostu porzucane i nigdy nie docierają do kolejnych handlerów.

**3. Wyjaśnij, dlaczego kolejność handlerów ma znaczenie. Podaj przykład.**

Wcześniejsze handlery mogą zatrzymać lub przekształcić żądanie. Przykład: handler uwierzytelniania musi działać przed logiką biznesową (najpierw odrzucić niezweryfikowane); albo konkretny filtr przed filtrem ogólnym.

**4. Czym Chain of Responsibility różni się od Pipeline (lista)? Kiedy które wybrać?**

W łańcuchu każdy handler decyduje, czy obsłużyć żądanie i czy przekazać je dalej — przetwarzanie może zostać przerwane wcześniej. Pipeline uruchamia każdy etap po kolei, a każdy przekształca dane. Łańcuch = jeden z kilku, który obsługuje; pipeline = każdy krok przetwarza.

**5. Wymień klasyczny przykład Chain of Responsibility w bibliotece Java.**

Łańcuch filtrów serwletów (`FilterChain.doFilter`) — każdy filtr obsługuje żądanie lub przekazuje je do następnego.

**6. Jak handler może powiedzieć „nie obsługuję, ktoś inny niech tym się zajmie"?**

Nie obsługuje żądania i wywołuje następny handler, przekazując je w dół łańcucha.

**7. Co odróżnia Chain od Decorator? Oba „opakowują" — czym?**

Dekorator zawsze dodaje zachowanie i zawsze deleguje (uruchamia się każda warstwa). W łańcuchu handler może zatrzymać żądanie — przetwarzanie kończy się na tym, kto je obsłuży.

**8. Jak przetestować pojedynczy handler w izolacji?**

Ustaw jego następnik jako mock/stub, wyślij żądanie, które powinien obsłużyć, i potwierdź jego działanie; następnie wyślij żądanie, którego nie powinien obsłużyć, i sprawdź, czy zostało przekazane dalej (czy wywołano mockowany następnik).

**9. Kiedy NIE używać Chain of Responsibility?**

Dla 2 prostych warunków wystarczy `if/else`. Opłaca się przy wielu handlerach, dynamicznej kolejności lub dodawaniu/usuwaniu w czasie wykonywania.

**10. Jak Chain ma się do middleware'u w nowoczesnych frameworkach webowych?**

Middleware opiera się na tej samej idei — żądanie przechodzi przez uporządkowaną serię, z których każdy element może je obsłużyć, zmodyfikować, zewrzeć (short-circuit) lub przekazać dalej (Express, ASP.NET, filtry serwletów).