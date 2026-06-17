Odpowiedzi na pytania z SOLID 
### 0. Wprowadzenie — kiedy NIE stosować SOLID
1. Mała funkcja shellowa do parsowania jednego pliku CSV w skrypcie uruchamianym raz w tygodniu z crona. YAGNI
Jezeli ta funkcja to tylko jedna rzecz to nie jest tutaj potrzeba zasada SOLID, to tylko parser ktorego zadko ktos bedzie ulepszal albo rozbudowywal. 
2. Klasa rabatów w sklepie internetowym — obecnie 2 typy klientów (`STANDARD`, `VIP`), ale marketing zapowiada nowe segmenty co kwartał. SOLID
   Tutaj SOLID to dobry wybor poniewaz jest to klasa ktora bedzie rozbudowywana na bierzaco
3. Klasa `User` w aplikacji webowej, używana przez moduł autoryzacji, administracyjny, raportowy i cleanup. SOLID
   Ta klasa jest uzywana przez wiele modolow tutaj SOLID aby klasa nie zostala klasa GOD Object.
4. Mała aplikacja konsolowa „kalkulator BMI" z trzema wejściami i jednym wyjściem. YAGNi
   Ta apka bedzie robic to samo non stop, czyli liczyc BMI solid to over-engineering tutaj
5. Klasa `OrderService` mająca pole `private final MySqlOrderRepository repository = new MySqlOrderRepository()` w produkcyjnym kodzie sklepu. SOLID
   Tutaj trzeba zastosowac DIP, powniewaz bedzie ciezko testowac taki service.

### Pytania kontrolne

1. Wymień trzy konkretne objawy, po których poznasz, że kod „prosi się o SOLID".
   Trudnosc testowania, kiedy rosnie if/else lub switch po type i dkoladamy case przy kazdym nowym wariancie, klasa ma poli i metody z roznymi dzialaniami a zmiana jednej rzeczy zmusza do grzebanie w niezwiazanym kodzie
2. Wymień dwa konkretne objawy, po których poznasz, że SOLID byłby tu over-engineeringiem.
   tworzy interface na zapas choc jest i bedzie dkoladnie jedna implemenacja, albo kod jest krotki, jednorazowy a abstrakcja mialaby wiecej lini niz to co opakowywuje. 

3. Dlaczego „YAGNI bije DIP" w pewnych sytuacjach? Podaj przykład.
   Poniewaz DIP to dodanie dodatkowego interfajsu, wstrzykiwanie, dodanie zaleznosci gdzie pozniej. A jezeli implementacja bedzie i jest tylko jedna to dodajemy abstakcje do abstakcji.

4. Czy SOLID to sekwencja kroków (S → O → L → I → D) przy projektowaniu nowej klasy? Uzasadnij.
   Nie Solid to narzedzie a zada literka pokazuje jakias zasade ktora mozna by bylo zastosowac. Nie jest to koniecznie ale w niektoch miejscach zalecane 

### 1. S — Single Responsibility Principle
1. Wypisz publiczne metody klasy. Spróbuj opisać klasę jednym zdaniem **bez słowa „i"**.
metody publiczne klasy to createOrder i calculate Total.
OrderService to klasa ktora pozwala nam zarzadzac creacja nowych Orders i uzywa independencji injection dla repository, email, report service.
2. Jeśli musisz powiedzieć „klasa robi A **i** B **i** C" — SRP naruszone.
3. Dla każdej grupy metod zadaj pytanie: kto (jaki aktor / dział biznesowy) zażąda zmiany?
4. Wydziel każdą grupę do osobnej klasy/interfejsu. Nazwa nowej klasy powinna opisywać dokładnie tę jedną odpowiedzialność.
5. `OrderService` zna logikę biznesową, ale **nie wie**, jak działa baza, email czy PDF — to są jego zależności w postaci interfejsów.
6. Test `OrderService` mockuje trzy interfejsy i wystarczy — żaden test nie potrzebuje prawdziwej bazy, SMTP ani biblioteki PDF.


1. Klasa ma 12 metod publicznych. Czy narusza SRP? Od czego zależy odpowiedź?
Jezeli wszystkie metody sluza do jednej odpowiedzialnosci to SRP nie jest naruszone. Czyli jeden powod do miany/jeden aktor decyduje o tym czy klasa narusza SRP a nie ilosc moetod.
2. Dlaczego „Spróbuj opisać klasę bez słowa »i«" jest praktycznym testem SRP?
Poniewaz sprawdza ile odpowiedzialnosci ma klasa, jezeli mamy robi nowy order i zarzadza dodaniem go do bazy to SRP jest naruszone. 
3. Wymień trzy konkretne korzyści testowania, które pojawiły się po rozbiciu `OrderManager` na `OrderService` + 3 interfejsy.
Latwiej zrobic mocki i je wstrzyknac do klasy, testowanie tylko OrderService zamiast 3 roznych niezalznych funkcji jak generowanie PDF. Nie musimy testowac Repository w Order Service.
4. Czy klasa `OrderService` po refaktorze nadal nie narusza SRP? Uzasadnij.
Nie, klasas nie narusza SRP poniewaz jest uzywana do tworzenia zamowienia. Nie wie jak dziala baza danych, jak wyslac email ani wygenerowac PDF. Jeden powod do zmiany == SRP spelnione.

### 2. O — Open/Closed Principle

1. Co dokładnie znaczy „otwarte na rozszerzenie, zamknięte na modyfikację"?
otwarte na rozszerzenie oznacza tak ja w naszym przykladzie z discountPolicy, dodajemy nowa klase do kazdego rodzaju discountu, w przyszlosci jezeli chcemy dodac Valentines Day discount to wystarczy stworzyc klase
i zaimplementowac DiscountPolicy interface. Zamkniete na modyfikacje chodzi o to ze instniejaca klasa PriceCalculator i inne klasy zostaja nietkiete wiec nie trzeba ich testowac ponownie. 
2. Pokaż konkretnie na kodzie: ile plików zmieniasz, dodając nowy typ klienta przed refaktorem, a ile po refaktorze?
Zmieniam tylko jeden plik w obu przypadkach. Jednak po refaktorze tworzymy nowy plik i zero plikow zmodyfikowancych a przez jeden plik jest modyfikowany.
3. Czy każdy switch musi być rozbity na polimorfizm? Wymień co najmniej dwie sytuacje, w których lepiej zostawić switch.
Nie kiedy nie trzeba go edytowac za kazdym razem gdy dochodzi nowy wariant.
4. Jakie są minusy „przedwczesnego" stosowania OCP (interfejs dla jednej implementacji)?
Abstakcja w abstracji, jezeli dodamy OCP ale bedzie tylko jedna implemenatacja, czyli dostajemy martwy kod i gorsza czytelnosc, dodatkowy plik ktory nic nie wnosi a zeby sprawdzic co sie dzieje musimy
sprawdzac implementacji interfejsu.

### 3. L — Liskov Substitution Principle
Bird / Eagle - dziedziczenie
Bird / Penguin - LSP
BankAccount / SavingsAccount - LSP
Animal / Dog - dziedziczenie 
Vehicle / ElectricCar - LSP
Vehicle / Tank (gąsienice zamiast kół) - dziedziczenie

1. Dlaczego „kwadrat to specjalny prostokąt" działa w matematyce, ale łamie LSP w kodzie?
Poniewaz w matematyce kazdy kwadrat to prostokat ale nie kazdy prostokat to kwadrat - czyli jak jest napisane is-a relationship nie zawsze przeklada sie na dziedziczenie. Modele obiektowe nie musza odzwierciedlac swiata rzeczytistego musza odzwierciedlac kontrakty zachaowan.
Czyli w kodzie Rectangle ma kontrakt setWidth i setHeigh ktore sa niezalezne. Square go nie dotrzymuje bo ustawienie szerokosci wymusza wysokosc.
2. Wymień trzy typowe naruszenia LSP, które łatwo przeoczyć w code review.
pierwsze - nowy typ wykatku - podklasa rzuca wyjatek ktorego klient klasy bazowej sie nei spodziewa.
drugie - wzmocniowy warunek wstepny - podklasa wymaga wiecej niz klasa bazowa.
trzecie - oslabiony warunek koncowy - po metodzie nie zachodzi gwarancja ktora dawala klasa bazowa.
3. Co znaczy „faworyzuj kompozycję nad dziedziczeniem"? Pokaż konkretnie na klasie SquareByComposition.
Zamiast dziedziczyc Rectangle dodajemy go jako pole final czyli robimy has -a relation zamiast is -a relation. Co nam pozwala na zapisanie dzilnosci klasy w inny sposob.
Co oznacza ze Square nie jest typem Rectangle i nie zostanie wstawiony tam gdzie oczekujemy rectangle czyli nie zostanie zlamany zaden kontrakt.
4. Jak rekordy w Javie pomagają unikać naruszeń LSP?
Rekordy maja pola final private i nie maja setterow. A wiec jak juz zrobimy jakis obiekt to nie jestesmy go w stanie zmienic i musi przejsc przez walidacje konstruktora.

### 4. I — Interface Segregation Principle

1. Skąd wiesz, że interfejs jest „za gruby"? Wymień dwa konkretne sygnały.
Kiedy kazdy department uzywa 2-3 metotod z wielu wymuszanych przez interface oraz kirdy kiedy rzucany jest UnsupportedOperationException w jakiejkolwiek metodzie.
2. Czy ISP oznacza, że każdy interfejs ma mieć dokładnie jedną metodę? Uzasadnij.
Nie - ISP oznacza ze kazdy Interface powinien miec metody ktore sa uzywane i przeznaczne lda danego dzialu a nie robienie 12 interfacow z pojedyncza metoda.
3. Jak ISP łączy się z LSP? Pokaż na przykładzie atrapy szerokiego interfejsu rzucającej `UnsupportedOperationException`.
Poniewaz ISP wymusza implementacje podobnie jak dziedziczenie, czyli mamy nowy typ wyjatku - podklasa rzuca wyjatek ktore klient klasy bazowej sie nie spodziewal.
czyli np UserRepository zmusza ReadOnlyUserRepo do zaimplementowania save() /delete() ktory nie jest obslugiwany. 
4. Klasa `DatabaseUserRepository` implementuje 5 wąskich interfejsów. Czy to OK? Co by było problem?
Klasa implemetuje tyle interfacow ile potrzebuje, w tym przypadku potrzebuje wszystkich aby zarzadzac repository. Problem moze byc jezeli mamy np UserDisplayService ktory zabira calosc DatabaseUserRepo.
wtedy zamiast widzic tylko UserReader opcji widzi wszystkie 12.

### 5. D — Dependency Inversion Principle

1. Co dokładnie jest „odwrócone" w Dependency Inversion?
Odwrocona jest zaleznosc w ktorej klasa biznesowa wspolpracuje z repo. W pierwszym przypadku tworzylismy repo w klasie biznesowaj a w drugim przypadku dodawalismy interface ktory wymuszal implementacje
na Repository. 
2. Dlaczego konstruktorowe DI jest lepsze niż setterowe? Pokaż konkretny scenariusz, w którym setter zawodzi.
Z karty pracy wynika na to ze konstruktorow jest robione raz i sprawdzane przez konstruktor wiec nie da sie dodac null objectu. A przy setterze mozemy dodaj jakis obiekt ktorego nie chcemy.
3. Jak DIP łączy się z testowalnością? Pokaż konkretną korzyść na przykładzie `OrderService`.
No na OrderService mozemy dodaj baze danych ktora moze sie roznic moze to byc h2 inmemory database do tesotwo, potem mozemy ja zamienic na SQL albo na No-SQL. I wszystko bedzie dzialac w Mocku.
4. Kiedy DIP jest over-engineeringiem? Wskazówka: YAGNI.
Kiedy kazdy interface bedzie mial np tylko 1 mala metode. takze samo formowanie interfacow i ich dziedziczenie bedzie kosztowac nas wiecej kodu niz napisane 2-3 malych metod
5. Czym różni się **klasyczna zależność** od **odwróconej**? Narysuj strzałkę zależności w obu wariantach.
Klasyczna zaleznosc - uzywa -> Repository
Odwrocona zaleznosc - uzywa -> interface -> implementujeRepository