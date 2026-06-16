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

