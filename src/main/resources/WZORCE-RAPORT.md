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
Poniewaz Factory Method jest uzywany kiedy nie wiem dokladnie jakie typy obiektow pojawia sie w programie i jake beda miedzy nimi zaleznosci.
2. Czym różni się Factory Method od bezpośredniego użycia konstruktora `new`?
Poniewaz zadaniem kreatora nie jest tworzenie nowych produktow, tylko implementacja kluczowej logiki biznesowej
3. Wymień 3 statyczne metody fabrykujące z JDK i powiedz, jakie korzyści daje ich istnienie zamiast `new`.

4. Co to znaczy, że fabryka może być „rozszerzalna w runtime"? Jak to osiągnąć (rejestr fabryk)?
5. Kiedy Factory Method jest **przesadą**? (Wskazówka: dla 1-2 typów bez wariantowości.)
6. Co jest lepsze: `Integer.valueOf(42)` czy `new Integer(42)`? Dlaczego (JDK to celowo deprecuje)?