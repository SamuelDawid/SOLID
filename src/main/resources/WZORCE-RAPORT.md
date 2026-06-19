### 1. Singleton

1. Dlaczego konstruktor Singletona musi być `private`? Co by się stało, gdyby był `public`?
   Poniewaz mamy tylko jeden obiekt tej klasy wiec nie powinnismy byc w stanie zrobic new ... tylko dzialac na
   stworzonym raz obiekcie albo zainicjonac go pierwszy razy.
   Gdyby byl public to nie bylby to singleton poniewaz powstalo by wielke obiektow tego rodzaju.
2. Wytłumacz, dlaczego DCL **wymaga** słowa kluczowego `volatile`. Jaki konkretnie problem (z modelu pamięci) to
   naprawia?
   Dzieki volotile wiemy ze operacja new to trzy kroki czyli alokuj pamiec, wywolaj kostruktor i przypisz reference w
   takie kolejnosci. Volotile to gwaratuje inaczej JIT albo procesor moze mizenic kolejnosc.
3. Wymień co najmniej dwa powody, dla których enum jest bezpieczniejszą implementacją Singletona niż klasyczna klasa z
   polem static.
   Serializacja - deserializacja enuma daje dokladnie te sama instancje, refleksja - Construktor.newInstance na enumie
   rzuca IllegalArgumentException, thread-safty gwarantowana przez JVm, bez zadnej dodatkowej logiki, lazy instancja
   powstaje przy pierwszym dotkieciu enuma.
4. *Pytanie dodatkowe (jeśli znasz Spring — opcjonalne).* Singleton klasyczny (`getInstance()`) vs „bean singleton" w
   kontenerze DI — gdzie leży różnica? Co jest singletonem „w czym"? Jeśli nie znasz Springa, pomiń.
   klasyczny singleton sam pilnuje jedynosci w skali JVM i uzywa sie go statycznie, a bean singleton to zwykla klasa
   ktorej jedynosc w skali kontenera zapewnia i wskrzykuj kontener DI co usuwa globalny stan i daje testowalnosc.
5. Wymień 3 sytuacje, w których Singleton jest **anty-wzorcem**. (Wskazówka: testy, mutowalny stan, ukryta zależność
   uniemożliwiająca wstrzykiwanie przez konstruktor.)
   testy moga ulec utrudnieniu poniewaz wiele frameworkow testujacych polega na dziedziczeniu przy produkcji atrap
   obiektow, a konstruktor klasy Singleton jest prywatny a nadpisane statycznych metod jest niemozliwe.
   Kod korzystajacy z globalnego singletona staje sie silnie powiazany, a przeplyw danych jest mniej czytalny niz przy
   jawnym przekazywaniu zaleznosci.
   Instancja klasy stworzona zgodnie z singetonenem posiada dane ktore moga ulegac modyfikacji w trakcie dzialanie
   programu.
6. Kiedy wybrałbyś EAGER, a kiedy LAZY z DCL? Podaj przykład biznesowy dla każdego.
   Wszystkie zalezy czy obekt jest potrzebny odrazy przy iniclizacji czy podczas uruchomienia programu. Eger uzywamy
   kiedy tworzenie obiektu nie zauzywa duzo pamieci ani czasu procesora, gdy instancja bedzie wymagana za kazdym razem
   gdy aplikacja zostanie uruchomiona i dostajemy bezpieczenstwo watkow w prosty sposob.
   Lazy gdy pracujemy na parsowaniu duzych plikow czy nawiazania kosztownego polaczenia z baza danych albo alokacji
   duzej ilosci pamiecie a obiekt nie zawsze jest uzywany. Dzieki czemu aplikacja uruchamia sie szybciej odkladajac
   tworzenie ciezkich obiektow na pozniej.
7. Klasa `AppConfigEager` ma instancję tworzoną przy ładowaniu klasy. Jeśli klasa nigdy nie zostanie zreferencjonowana w
   aplikacji, czy konstruktor zostanie wywołany?
   Nie Eager znaczy od razy gdy klasa zostanie zainicjalizowana, ale JVM laduje klasy leniwie czyli przy inicjalizacji
   new.

### 2. Wzorzec Factory Method

1. Dlaczego fabryka **musi** zwracać interfejs/abstrakcyjny typ, a nie konkretną klasę?
   Zwraca interface poniewaz klijent zna tylko ten interface a nie jego implementacje.
2. Czym różni się Factory Method od bezpośredniego użycia konstruktora `new`?
   przy new klijet musi znac konstruktor, gdzie fabryka ukrywa tworzenie obiektu. Podajemy paramet i dostajemy abstracje
   nie wiedza ktora klase i jak ja zbudowano.
3. Wymień 3 statyczne metody fabrykujące z JDK i powiedz, jakie korzyści daje ich istnienie zamiast `new`.
   List.of - zwraca podtyp klasy ktorej nie znamy / Optional.of - Mozemy zrobic kilka obiektow o takich samych nazwach
   gdzie konstruktor nam nie pozwoli / Integer.valueOf - Moze zwrocic instacje z cache zamiast zawsze alokowac nowy
   obiekt.
4. Co to znaczy, że fabryka może być „rozszerzalna w runtime"? Jak to osiągnąć (rejestr fabryk)?
   To znaczy ze jezeli mamy mape gdzie przechowujemy typ notification z jakis kluczem, to w runtime ktos sie pyta czy
   mamy w rejestrze(mapie) notification typu .... jezeli tak to zwracamy jezeli nie to tworzymy obiekt w runtime i
   dodajemy go do rejestu.
5. Kiedy Factory Method jest **przesadą**? (Wskazówka: dla 1-2 typów bez wariantowości.)
   Przesada jest tworzenie Fabryki dla 1-2 typow - lepiej wtedy uzyc zwyklego konstruktora.
6. Co jest lepsze: `Integer.valueOf(42)` czy `new Integer(42)`? Dlaczego (JDK to celowo deprecuje)?
   valueOf zwraca cashed wartosc czyli wartosc ktora juz instnieje zamiast tworzyc nowy obiekt.

### 3. Wzorzec Abstract Factory

1. Czym różni się Abstract Factory od Factory Method?
   Factory method tworzy tylko jeden typ produktu, gdzie abstact factory tworzy nam cala rodzine prodoktow ze soba
   spokrewionych poprzez wiele metod i gwaratuje spojnosc.
2. Co to znaczy „rodzina obiektów" w Abstract Factory? Podaj przykład z UI lub bazą danych.
   Dark-theme faktory poda nam button, checkbox, scrollbar wszystko w charnym odcieniu, gdzie light theme factory zrobi
   to samo ale w jasnym odcieniu.
3. Co się stanie, jeśli klient sam stworzy `new PdfHeader()` i połączy go z `HtmlBody()`? Dlaczego Abstract Factory tego
   nie pozwala?
   Powstanie type mismatch - broken report. Abstract Factory chroni nas przed tym poniewaz wszystkie obiekty stworzone
   sa przez jedna fabryke, ktora rowniej ukrywa implementacje w jaki spsob powstal taki obiekt wiec klijent nigdy by nie
   wiedzial o PDFHEADER.
4. Dlaczego dodanie nowego komponentu (np. „logo") do rodziny w Abstract Factory jest droższe niż dodanie nowej rodziny?
   Poniewaz musimy 1 nowy interfejs + 3 implementacje + 3 modyfikacje fabryk + 1 wywołanie to wlasnie glowna wada tego
   wzorca.
5. Wymień przykład Abstract Factory z JDK lub bibliotek Jakarta EE (zestaw standardów do dużych aplikacji serwerowych w
   Javie — *znajomość Jakarta EE nie jest wymagana, wystarczy przykład z JDK*).
   DocumentBuilderFactory
6. Jaki jest podstawowy „test" sensu Abstract Factory — czy potrzebujesz tworzyć **zestaw spójnych** obiektów?
   Tak potrzebujesz poniewaz zestaw spojnych obiektow pokaze nam ile ich jest i wtedy bedziemy mogli podjac decyzje czy
   oplaca sie pisac Abstract Factory

### 4. Wzorzec Builder

1. Dlaczego konstruktor klasy docelowej (`Email`) jest `private`?
   Poniewaz tylko builder moze stworzyc obiekt wiec nigdy nie jest zbodowany tylko w polowie i nigdy zmieniany ponownie.
2. Czemu walidacja **musi** być w `build()` (lub w konstruktorze klasy docelowej), a nie w setterach Buildera?
   Poniewaz walidacja w jednym miescu odrazu nam daje feedback czy wszystko jest ok przy budowie. Jezeli byloby to
   zrobione w setterach to obiekt mogl by byc w polowie stworzony i nie jestesmy w stanie
   sprawdzic walidacji dla pol ktore nie sa stworzone.
3. Wymień dwie kategorie pól w Builderze: wymagane i opcjonalne. Jak każda jest obsługiwana?
   wymagane pole to takie gdzie dostaniemy wyjatek jezeli go brakuje, a opcjonalne to takie pole gdzie jezeli go nie
   dopiszemy to dostaniemy default value i mozemy go pominac.
4. Co znaczy „defensywna kopia" listy `attachments` i dlaczego jest potrzebna?
   Defensywna kopia znaczy nowa kopia tworzona na podstawie referencji do tego samego obiektu, inaczej bysmy mogli
   modyfikowac immutable object.
5. Dlaczego każdy setter Buildera zwraca `this`? Co się stanie, gdy zwróci `void`?
   Dizeki temu mozemy zrobic from-subject-build bez this void nic nie zwraca i nasz lancuch przepada.
6. Kiedy preferować Lombok `@Builder` nad ręczną implementacją, a kiedy odwrotnie?
   Kiedy mamy tak zwany pure boilerplate czyli same pola bez validacji, wtedy lombook jest ok. Jednak kiedy potrzebujemy
   prawdziwej validacji, albo wartosci domyslne ktore obliczane sa automatycznie.
7. Wymień 2 Buildery z JDK lub bibliotek (`StringBuilder`, `HttpClient`, ...).
   StringBuilder, Stream.builder(),HttpRequest.newBuilder().
8. Czy record może mieć Builder? Co Builder dodaje do record'a, którego record sam nie ma?
   Tak rekord moze miec builder, dzieki temu nasz obiekt dodatkowo ma opcjonalne parametry, default values, i czytelny
   konstruktor kiedy mamy duzo pol.

### 5. Wzorzec Prototype

1. Wytłumacz różnicę między płytką (shallow) a głęboką (deep) kopią. Podaj scenariusz, kiedy płytka jest pułapką.
   Plytka kopia kopiuje bezposrednio prymitywne pola ale referencje pol/obiektow dalej prowadza do tego samego
   zagniezdzonego obiektu jak w originale.
   Gleboka kopia takze kopiuje te zagniezdzone pola/obiekty wiec nie ma powiazania pomiedzy oryginalem a kopia. Czyli
   jezeli cos sotanie zmienione w oryginale to plytka kopia sie zmieni a gleboka bedzie miec to samo value co mialo
   przed zmiana.
   Plytka kopia moze byc plapka kiedy mamy na przyklad dwie instancje dokumentu ktore maja te samo liste. Wtedy kiedy
   zmienimy obiekt do ktorego oba referuja to wtedy zmieni sie wartosc w obu.
2. Dlaczego Joshua Bloch rekomenduje **copy constructor** zamiast `Cloneable`/`clone()`?
   Cloneable jest slabo zaprogramowane, zwraca obiekt ktorego trzeba castowac spowrotem, rzuca checked
   CloneNotSupportedException i do tego podaje shallow copy by default. Gdzie kopiowanie konstruktora
   albo statyczne copyof zapobiega tym problemom.
3. Czym przykład `ServerConfig.withHost(...)` różni się od `DocumentTemplate.copy()` koncepcyjnie?
   Documenttemplate.copy duplikuje dokladny stan obiektu do nowej niezaleznej instancji dzieki czemu mozna zmienic
   duplikat bez wplywu na oryginal.
   ServerConfig.withHost dziala na immutable obiekcie na ktorym nie mozemy zmienic pola wiec zwraca nowa instancje
   identycznego obiektu z jedna nowa zmienna.
4. *Pytanie dodatkowe, opcjonalne.* Gdzie w popularnych frameworkach Javy spotkasz Prototype? (Wskazówka: tzw. „scope" —
   *zakres życia* obiektu w kontenerze; jeśli nie używałeś żadnego frameworka, pomiń.)
   w springu bean scope prototype - kontener tworzy nowa isntancje za kazdym razem kiedy bean jest potrzebny.
5. Czy record (Java 14+) potrzebuje Prototype? Jaką jego namiastkę daje sam record (kompaktowy konstruktor,
   deconstruction)?
   Nie rekord nie potrzebuje prototypu poniwaz jest immuable wiec zmiane tylko jednego pola mozna osiagnac poprzez
   konstuktor. ORaz posiada canonical/compactcostructor i deconstruction poprzez pattern matching.
6. Dlaczego dla niemutowalnej konfiguracji `withXxx` zwraca **nową** instancję, a nie modyfikuje obecnej?
   Poniewaz object jest immutable ma finalne pola i nie ma setterow. Wiec jedyna opcja jest zwrocenie nowej instancji z
   zmodyfikowana zmienna.
7. Wymień scenariusz biznesowy, w którym Prototype jest tańszy niż klasyczny `new` + setery.
   Kiedy obiekt jest bardzo zlozony/drogi do stworzenia i potrzebujemy kilka wariantow. Wtedy mozemy go zbudowac raz i
   potem skopiowac i pozmienic co chcemy.

### 6. Wzorzec Adapter

1. Czym różni się **object adapter** (kompozycja) od **class adapter** (dziedziczenie)? Który Java preferuje i dlaczego?
Adapter obiektu przechowuje referencje do adaptowanego obiektow i implementuje interfejs docelowy, kazda metoda deleguje do adaptowanego obiektu i tlumaczy parametry/wyniki.
Klasa adapter rozszerza dzialanosc adaptowanego obiektu i implementuje interfejs docelowy. Java woli adapter obiektu poniewaz nie ma dziedziczoenia wieloklasowego a adapter poznwala na adaptacje dowolnej instancji i swobodna jej zmiane.
2. Wymień dwa przykłady Adaptera z JDK i opisz, co i do czego adapteruje.
   InputStreamReader - tlumaczy byte stream do characters przez uzycie charset.
   Arrays.asList - tlumaczy zwykla tablice na interface List dzieki czemu mozemy uzywac tablicy w miejsce Listy.
   Collections.enumeration - tlumaczy Collection/Iterator do starego Enumeration interfejsu.
3. Adapter zmienia **interfejs**, ale czy zmienia **zachowanie**? Jaka jest jego intencja?
   Nie adapter zmienia interface ale nie zminia zachowania. Ma za zadanie polaczyc dwa niekompatybilne interfejsy w jeden dzialajacy. 
4. Czemu w `StripeAdapter` mamy konwersję PLN → grosze, a nie po prostu `(long) amountPln`? (Wskazówka: błąd
   floating-point, `Math.round`.)
   Poniewaz pieniadze maja zmienne po przecinku a java czesto gubi liczby zmiennoprzecinko np 19.99 moze byc zapisane jako 19.9899, wiec conwertuje grosze uzywajac Math.round wiec nie tracimy pieniedzy po przecinku.
5. Co się stanie, jeśli `LegacyStripeApi` zmieni sygnaturę `charge(...)` w nowej wersji? Co musisz zmienić w
   klientach? (Tylko adapter — klient `PaymentProcessor` jest izolowany.)
   Tylko adapter - klient jest zalezny od interfejsu PaymentProcessor ktory sie nie zmienia. Cale polaczenie odbywa sie w StripeAdapter.
6. Kiedy Adapter jest **anty-wzorcem**? (Wskazówka: dla małych różnic, lepiej dostosować jeden z interfejsów. Adapter to
   overkill dla 1 metody.)
   Kiedy oba interfejsu sa niemal identyczne i roznia sie nieznacznie. Wtedy lepsze jest dostosowanie jednego interjesu niz dodanie kolejnej warsty.
7. Czy Adapter to to samo co Facade? Jaka jest różnica intencji?
   Nie, adapter konwertuje instniejacy interfejs na inny, oczekiwany przez klienta aby umozliwic wspolprace dwoch niekompatyblinych czesci. Facade zapewnia nowy prostszy interfejs w zlozonym podsystemie wielu klas.

## 7. Wzorzec Decorator

1. Wytłumacz, czym dekorator różni się od **proxy** (na poziomie intencji, nie struktury).
Dekorator dodaje zachowanie do obiektu, proxy kontroluje dostep do obiektu.
2. Czy kolejność dekoratorów ma znaczenie? Podaj przykład, w którym `A(B(x))` daje inny wynik niż `B(A(x))`.
Tak ma znaczenie np. Encrypt(Compress(x)) dziala dobrze ale jezeli odwrocimy te operacje bo pranie nic nie zostanie skompresowane poniewaz ekryptowane dane wygladaja randomowo i nie nadaja sie do kompresi.
3. Wymień strumień z `java.io`, który jest klasycznym dekoratorem. Jakie zachowanie dodaje?
 BufferedReader - opakowywuje Reader/InputStream i dodaje buffering.
4. Dlaczego zamiast 256 klas (`CoffeeWithMilkAndSugarAndCream...`) wystarczy 1 klasa bazowa + N dekoratorów?
 Dekoraotry komponuja sie w czasie wykonywania wiec N dekoratorow daje wszystkie 2^N kombinacji z N klasami.
5. Co by się stało, gdyby `MilkDecorator` rozszerzał `Espresso` zamiast implementować `Coffee` (przez
   `CoffeeDecorator`)? (Wskazówka: nie zadziała dla `Americano`.)
   dekorowalby tylko espresso, a nie americano ani zadna inna kawe. Dekorowanie na interfejsie kawy pozwala na owiniecie nim dowolnej kawy.
6. Czy dekorator może dziedziczyć po dekorowanym obiekcie? Jakie są tego konsekwencje?
   Moze na poziomie klas, ale wtedy wiaze dekorator z jednym typem, nie moze opakowac dowolnych implementacji.
7. Jak dekorator ma się do zasady „kompozycja > dziedziczenie"?
   Preferowana jest kompozycja wzgledem interfejsu. „kompozycja > dziedziczenie” — zachowanie jest dodawane poprzez opakowanie (kompozycję) w czasie wykonywania, a nie przez podklasowanie dla każdej kombinacji.


## 8. Wzorzec Facade

1. Czym fasada różni się od adaptera? Oba ukrywają coś za sobą — czym?
2. Dlaczego `OrderFacade.placeOrder(...)` ma **rollback** płatności, gdy magazyn nie ma towaru?
3. Jakie elementy aplikacji powinny być za fasadą, a jakie nie? (Wskazówka: złożone podsystemy z kilkoma krokami, NIE
   prosta klasa z jedną metodą.)
4. Czy fasada może mieć swoją własną logikę biznesową? Czym fasada różni się od zwykłej klasy „Service" (klasy
   serwisowej, która zawiera logikę domenową)? (Wskazówka: fasada *orkiestruje* podsystemy, serwis *implementuje*
   logikę. To są dwie różne role, choć implementacja klasą może wyglądać podobnie.)
5. Wymień przykład fasady z JDK lub biblioteki Java.
6. Co znaczy „god class" w kontekście fasady i jak tego uniknąć?
7. Jak fasada wspiera zasadę Demeter (Law of Demeter)? Czy zmniejsza, czy zwiększa sprzężenie?

## 9. Wzorzec Proxy (Static + Dynamic Proxy)

1. Czym Proxy różni się od Decorator? (Jedna odpowiedź dotyczy intencji — jakiej?)
2. Wyjaśnij, dlaczego Dynamic Proxy wymaga **interfejsu**. Co zrobiłbyś, gdyby twoja klasa nie miała interfejsu?
3. Dlaczego wywołanie `this.metoda()` z wnętrza klasy **omija** Proxy w nowoczesnych frameworkach?
4. Wymień 3 *cross-cutting concerns* (czyli funkcjonalności potrzebne w wielu klasach naraz — typu logowanie, cache,
   autoryzacja, audyt, pomiar czasu), które naturalnie pasują do Proxy.
5. Jakie 3 elementy musisz przekazać do `Proxy.newProxyInstance(...)`?
6. Co robi `method.invoke(target, args)` wewnątrz `InvocationHandler.invoke`? Dlaczego nie wywołujesz target
   bezpośrednio?
7. Co znaczy „stackowanie" proxy? Podaj przykład sensownej kolejności (logging vs caching vs auth).

## 10. Wzorzec Composite

1. Czym różni się **liść** od **composite** w tym wzorcu?
2. Dlaczego klient nie potrzebuje wiedzieć, czy obiekt to plik czy folder?
3. Wymień przykład Composite z biblioteki UI Javy (Swing/JavaFX).
4. Jak wzorzec radzi sobie z metodami typu `add(child)` — czy powinny być w interfejsie, czy tylko w composite?
5. Co się stanie, jeśli struktura ma cykl (A zawiera B, B zawiera A)? Jak temu zapobiec?
6. Jak Composite ma się do struktury DOM w HTML / XML?
7. Wymień scenariusz biznesowy, w którym Composite jest naturalny.

## 11. Wzorzec Strategy (z lambdami w Java 8+)

1. Czym Strategy różni się od Factory Method? (Wskazówka: Factory **tworzy** obiekty, Strategy **wykonuje** algorytm.)
2. Dlaczego Java 8+ ułatwia Strategy? (Wskazówka: interfejs funkcyjny + lambda = jedna linia.)
3. Wyjaśnij, dlaczego dispatch table (Map) jest lepsza od switch. Co możesz w runtime zmienić w mapie, czego nie
   zmienisz w switch?
4. Wymień klasyczny przykład Strategy w JDK.
5. Co znaczy „kompozycja strategii"? Podaj przykład.
6. Czy `Comparator` to Strategy? Uzasadnij.
7. Kiedy Strategy jest **przesadą**? (Wskazówka: dla 2 wariantów bez planowanego wzrostu.)
8. Czy strategia powinna trzymać stan? (Wskazówka: jeśli tak, traci „funkcyjność" — wtedy bliżej do Command.)

## 12. Wzorzec Observer (PropertyChangeSupport + ręcznie)

1. Wytłumacz, dlaczego publisher (`OrderService`) nie powinien znać konkretnych listenerów.
2. Dlaczego pętla `publish` ma try/catch wokół każdego listenera? Co się stanie bez tego?
3. Co to jest `CopyOnWriteArrayList` i dlaczego pasuje do EventBus?
4. Czym `PropertyChangeSupport` różni się od ręcznego EventBus? Gdzie używałbyś każdego?
5. Wymień klasyczny problem memory leak w Observer i jak go uniknąć.
6. Jak Observer ma się do event-driven architecture i message brokerów (Kafka, RabbitMQ)?
7. Jak przetestować, że listener X obsługuje event Y?
8. Co odróżnia Observer od Mediator? (Wskazówka: Mediator wie o wszystkich, Observer nie.)

## 13. Wzorzec Command (z Undo)

1. Czym Command różni się od Strategy? Oba kapsułują „coś do zrobienia" — czym konkretnie się różnią?
2. Wymień 4 elementy wzorca Command (Receiver, Command, Invoker, Client).
3. Czemu makro (`MacroCommand`) cofa w **odwrotnej** kolejności?
4. Jak komenda zapamiętuje stan potrzebny do undo? Wymień strategię z `DeleteCommand`.
5. Jak `Runnable` ma się do Command?
6. Wymień przykład Command w GUI Javy.
7. Co się stanie, jeśli klient wywoła `undo` 100 razy z pustym stosem?
8. Kiedy NIE należy używać Command? (Wskazówka: prosty CRUD bez undo/historii.)
9. Jak Command pasuje do kolejkowania (queue) i retry?

## 14. Wzorzec Template Method

1. Co znaczy „template method" — czy to klasa, metoda, czy oba?
2. Dlaczego metoda template (np. `importData`) jest często `final`?
3. Co to jest **hook** w Template Method? Podaj przykład z naszego kodu.
4. Czym Template Method różni się od Strategy? Oba pozwalają „zmienić algorytm" — gdzie różnica?
5. Wymień klasyczny przykład Template Method w JDK (`HttpServlet`, `AbstractList`).
6. Czy podklasa może zmienić **kolejność** kroków w algorytmie? Dlaczego nie?
7. Jak Template Method ma się do zasady „kompozycja > dziedziczenie"? (Wskazówka: tu dziedziczenie jest wymagane.)
8. Co odróżnia metodę `abstract` od metody z domyślną implementacją (hook) w klasie bazowej?

## 15. Wzorzec Iterator (własny + integracja z Iterable)

1. Wymień dwa interfejsy potrzebne, by twoja kolekcja działała w for-each.
2. Jakie dwie metody ma `Iterator<T>`? Co każda robi?
3. Dlaczego konwencja mówi „każde wywołanie `iterator()` to nowy, świeży iterator"?
4. Jak iterator może ukrywać złożoną strukturę (drzewo)? Co robi `Tree.iterator()`?
5. Czy iterator musi wskazywać na istniejącą strukturę? (Wskazówka: `RangeIterator` generuje w locie.)
6. Co to jest `ConcurrentModificationException` i kiedy się pojawia?
7. Jak iterator współgra ze Stream w Javie?
8. Czy `for (int i = 0; i < list.size(); i++)` to też Iterator? (Wskazówka: nie, to pętla indeksowana. Iterator nie zna
   „indeksu".)
9. Wymień klasyczną sytuację, w której musisz napisać własny Iterator (a nie użyć kolekcji JDK).

## 16. Wzorzec State

1. Czym State różni się od Strategy? Oba „wymieniają zachowanie" — gdzie różnica?
2. Dlaczego context (`Order`) nie ma ani jednego `if/else` w metodach `pay/ship`?
3. Kto decyduje o przejściu między stanami — Context czy State?
4. Wyjaśnij, dlaczego setter `setState(...)` w `Order` jest `package-private`, a nie `public`.
5. Wymień przykład State w JDK (`Thread.State`).
6. Jak State pomaga w testowaniu? (Wskazówka: każdy stan testowany osobno, bez przygotowania całej historii.)
7. Czy `RefundedState.pay()` powinien zwracać do `RefundedState` czy do nowego stanu „refundowane drugi raz"? (Filozofia
   projektu — od ciebie zależy.)
8. Kiedy NIE używać State? (Wskazówka: dla 2 stanów wystarczy boolean, dla 3 stanów ze stabilną logiką — enum.)

## 17. Wzorzec Chain of Responsibility

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

