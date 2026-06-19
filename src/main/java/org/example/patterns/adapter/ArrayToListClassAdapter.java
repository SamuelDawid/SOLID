package org.example.patterns.adapter;

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
