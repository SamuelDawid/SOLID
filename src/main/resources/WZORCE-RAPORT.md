### 1. Singleton

1. Dlaczego konstruktor Singletona musi być `private`? Co by się stało, gdyby był `public`?
Poniewaz mamy tylko jeden obiekt tej klasy wiec nie powinnismy byc w stanie zrobic new ... tylko dzialac na stworzonym raz obiekcie albo zainicjonac go pierwszy razy.
Gdyby byl public to nie bylby to singleton poniewaz powstalo by wielke obiektow tego rodzaju. 
2. Wytłumacz, dlaczego DCL **wymaga** słowa kluczowego `volatile`. Jaki konkretnie problem (z modelu pamięci) to naprawia?
Dzieki volotile wiemy ze operacja new to trzy kroki czyli alokuj pamiec, wywolaj kostruktor i przypisz reference w takie kolejnosci. Volotile to gwaratuje inaczej JIT albo procesor moze mizenic kolejnosc.
3. Wymień co najmniej dwa powody, dla których enum jest bezpieczniejszą implementacją Singletona niż klasyczna klasa z polem static.
Serializacja - deserializacja enuma daje dokladnie te sama instancje, refleksja - Construktor.newInstance na enumie rzuca IllegalArgumentException, thread-safty gwarantowana przez JVm, bez zadnej dodatkowej logiki, lazy instancja powstaje przy pierwszym dotkieciu enuma.
4. *Pytanie dodatkowe (jeśli znasz Spring — opcjonalne).* Singleton klasyczny (`getInstance()`) vs „bean singleton" w kontenerze DI — gdzie leży różnica? Co jest singletonem „w czym"? Jeśli nie znasz Springa, pomiń.
klasyczny singleton sam pilnuje jedynosci w skali JVM i uzywa sie go statycznie, a bean singleton to zwykla klasa ktorej jedynosc w skali kontenera zapewnia i wskrzykuj kontener DI co usuwa globalny stan i daje testowalnosc.
5. Wymień 3 sytuacje, w których Singleton jest **anty-wzorcem**. (Wskazówka: testy, mutowalny stan, ukryta zależność uniemożliwiająca wstrzykiwanie przez konstruktor.)
testy moga ulec utrudnieniu poniewaz wiele frameworkow testujacych polega na dziedziczeniu przy produkcji atrap obiektow, a konstruktor klasy Singleton jest prywatny a nadpisane statycznych metod jest niemozliwe.
Kod korzystajacy z globalnego singletona staje sie silnie powiazany, a przeplyw danych jest mniej czytalny niz przy jawnym przekazywaniu zaleznosci.
Instancja klasy stworzona zgodnie z singetonenem posiada dane ktore moga ulegac modyfikacji w trakcie dzialanie programu. 
6. Kiedy wybrałbyś EAGER, a kiedy LAZY z DCL? Podaj przykład biznesowy dla każdego.
Wszystkie zalezy czy obekt jest potrzebny odrazy przy iniclizacji czy podczas uruchomienia programu. Eger uzywamy kiedy tworzenie obiektu nie zauzywa duzo pamieci ani czasu procesora, gdy instancja bedzie wymagana za kazdym razem gdy aplikacja zostanie uruchomiona i dostajemy bezpieczenstwo watkow w prosty sposob.
Lazy gdy  pracujemy na parsowaniu duzych plikow czy nawiazania kosztownego polaczenia z baza danych albo alokacji duzej ilosci pamiecie a obiekt nie zawsze jest uzywany. Dzieki czemu aplikacja uruchamia sie szybciej odkladajac tworzenie ciezkich obiektow na pozniej.
7. Klasa `AppConfigEager` ma instancję tworzoną przy ładowaniu klasy. Jeśli klasa nigdy nie zostanie zreferencjonowana w aplikacji, czy konstruktor zostanie wywołany?
Nie Eager znaczy od razy gdy klasa zostanie zainicjalizowana, ale JVM laduje klasy leniwie czyli przy inicjalizacji new.

### 2. Wzorzec Factory Method

1. Dlaczego fabryka **musi** zwracać interfejs/abstrakcyjny typ, a nie konkretną klasę?
Zwraca interface poniewaz klijent zna tylko ten interface a nie jego implementacje.
2. Czym różni się Factory Method od bezpośredniego użycia konstruktora `new`?
przy new klijet musi znac konstruktor, gdzie fabryka ukrywa tworzenie obiektu. Podajemy paramet i dostajemy abstracje nie wiedza ktora klase i jak ja zbudowano.
3. Wymień 3 statyczne metody fabrykujące z JDK i powiedz, jakie korzyści daje ich istnienie zamiast `new`.
List.of - zwraca podtyp klasy ktorej nie znamy / Optional.of - Mozemy zrobic kilka obiektow o takich samych nazwach gdzie konstruktor nam nie pozwoli / Integer.valueOf - Moze zwrocic instacje z cache zamiast zawsze alokowac nowy obiekt.
4. Co to znaczy, że fabryka może być „rozszerzalna w runtime"? Jak to osiągnąć (rejestr fabryk)?
To znaczy ze jezeli mamy mape gdzie przechowujemy typ notification z jakis kluczem, to w runtime ktos sie pyta czy mamy w rejestrze(mapie) notification typu .... jezeli tak to zwracamy jezeli nie to tworzymy obiekt w runtime i dodajemy go do rejestu.
5. Kiedy Factory Method jest **przesadą**? (Wskazówka: dla 1-2 typów bez wariantowości.)
Przesada jest tworzenie Fabryki dla 1-2 typow - lepiej wtedy uzyc zwyklego konstruktora.
6. Co jest lepsze: `Integer.valueOf(42)` czy `new Integer(42)`? Dlaczego (JDK to celowo deprecuje)?
valueOf zwraca cashed wartosc czyli wartosc ktora juz instnieje zamiast tworzyc nowy obiekt. 

### 3. Wzorzec Abstract Factory

1. Czym różni się Abstract Factory od Factory Method?
Factory method tworzy tylko jeden typ produktu, gdzie abstact factory tworzy nam cala rodzine prodoktow ze soba spokrewionych poprzez wiele metod i gwaratuje spojnosc.
2. Co to znaczy „rodzina obiektów" w Abstract Factory? Podaj przykład z UI lub bazą danych.
Dark-theme faktory poda nam button, checkbox, scrollbar wszystko w charnym odcieniu, gdzie light theme factory zrobi to samo ale w jasnym odcieniu.
3. Co się stanie, jeśli klient sam stworzy `new PdfHeader()` i połączy go z `HtmlBody()`? Dlaczego Abstract Factory tego nie pozwala?
Powstanie type mismatch - broken report. Abstract Factory chroni nas przed tym poniewaz wszystkie obiekty stworzone sa przez jedna fabryke, ktora rowniej ukrywa implementacje w jaki spsob powstal taki obiekt wiec klijent nigdy by nie wiedzial o PDFHEADER. 
4. Dlaczego dodanie nowego komponentu (np. „logo") do rodziny w Abstract Factory jest droższe niż dodanie nowej rodziny?
Poniewaz musimy 1 nowy interfejs + 3 implementacje + 3 modyfikacje fabryk + 1 wywołanie to wlasnie glowna wada tego wzorca.
5. Wymień przykład Abstract Factory z JDK lub bibliotek Jakarta EE (zestaw standardów do dużych aplikacji serwerowych w Javie — *znajomość Jakarta EE nie jest wymagana, wystarczy przykład z JDK*).
   DocumentBuilderFactory
6. Jaki jest podstawowy „test" sensu Abstract Factory — czy potrzebujesz tworzyć **zestaw spójnych** obiektów?
Tak potrzebujesz poniewaz zestaw spojnych obiektow pokaze nam ile ich jest i wtedy bedziemy mogli podjac decyzje czy oplaca sie pisac Abstract Factory

### 4. Wzorzec Builder

1. Dlaczego konstruktor klasy docelowej (`Email`) jest `private`?
Poniewaz tylko builder moze stworzyc obiekt wiec nigdy nie jest zbodowany tylko w polowie i nigdy zmieniany ponownie. 
2. Czemu walidacja **musi** być w `build()` (lub w konstruktorze klasy docelowej), a nie w setterach Buildera?
Poniewaz walidacja w jednym miescu odrazu nam daje feedback czy wszystko jest ok przy budowie. Jezeli byloby to zrobione w setterach to obiekt mogl by byc w polowie stworzony i nie jestesmy w stanie
sprawdzic walidacji dla pol ktore nie sa stworzone.
3. Wymień dwie kategorie pól w Builderze: wymagane i opcjonalne. Jak każda jest obsługiwana?
wymagane pole to takie gdzie dostaniemy wyjatek jezeli go brakuje, a opcjonalne to takie pole gdzie jezeli go nie dopiszemy to dostaniemy default value i mozemy go pominac.  
4. Co znaczy „defensywna kopia" listy `attachments` i dlaczego jest potrzebna?
Defensywna kopia znaczy nowa kopia tworzona na podstawie referencji do tego samego obiektu, inaczej bysmy mogli modyfikowac immutable object.
5. Dlaczego każdy setter Buildera zwraca `this`? Co się stanie, gdy zwróci `void`?
Dizeki temu mozemy zrobic from-subject-build bez this void nic nie zwraca i nasz lancuch przepada.
6. Kiedy preferować Lombok `@Builder` nad ręczną implementacją, a kiedy odwrotnie?
Kiedy mamy tak zwany pure boilerplate czyli same pola bez validacji, wtedy lombook jest ok. Jednak kiedy potrzebujemy prawdziwej validacji, albo wartosci domyslne ktore obliczane sa automatycznie.
7. Wymień 2 Buildery z JDK lub bibliotek (`StringBuilder`, `HttpClient`, ...).
StringBuilder, Stream.builder(),HttpRequest.newBuilder().
8. Czy record może mieć Builder? Co Builder dodaje do record'a, którego record sam nie ma?
Tak rekord moze miec builder, dzieki temu nasz obiekt dodatkowo ma opcjonalne parametry, default values, i czytelny konstruktor kiedy mamy duzo pol.

### 5. Wzorzec Prototype

1. Wytłumacz różnicę między płytką (shallow) a głęboką (deep) kopią. Podaj scenariusz, kiedy płytka jest pułapką.
2. Dlaczego Joshua Bloch rekomenduje **copy constructor** zamiast `Cloneable`/`clone()`?
3. Czym przykład `ServerConfig.withHost(...)` różni się od `DocumentTemplate.copy()` koncepcyjnie?
4. *Pytanie dodatkowe, opcjonalne.* Gdzie w popularnych frameworkach Javy spotkasz Prototype? (Wskazówka: tzw. „scope" — *zakres życia* obiektu w kontenerze; jeśli nie używałeś żadnego frameworka, pomiń.)
5. Czy record (Java 14+) potrzebuje Prototype? Jaką jego namiastkę daje sam record (kompaktowy konstruktor, deconstruction)?
6. Dlaczego dla niemutowalnej konfiguracji `withXxx` zwraca **nową** instancję, a nie modyfikuje obecnej?
7. Wymień scenariusz biznesowy, w którym Prototype jest tańszy niż klasyczny `new` + setery.