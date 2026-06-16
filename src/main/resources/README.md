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