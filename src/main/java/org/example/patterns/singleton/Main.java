package org.example.patterns.singleton;

import java.lang.reflect.InvocationTargetException;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        System.out.println("=== EAGER ===");
        AppConfigEager c1 = AppConfigEager.getInstance();
        AppConfigEager c2 = AppConfigEager.getInstance();
        System.out.println("c1 == c2? " + (c1 == c2));    // true
        System.out.println("DB URL: " + c1.getDbUrl());

        System.out.println("\n=== LAZY DCL ===");
        HeavyServiceDCL h1 = HeavyServiceDCL.getInstance();   // pierwsze wywołanie inicjalizuje
        HeavyServiceDCL h2 = HeavyServiceDCL.getInstance();   // drugie zwraca istniejące
        System.out.println("h1 == h2? " + (h1 == h2));
        h1.doExpensiveWork();

        System.out.println("\n=== ENUM ===");
        String conn = DatabasePool.INSTANCE.borrow();
        System.out.println("Wziąłem: " + conn);
        System.out.println("Wolnych: " + DatabasePool.INSTANCE.availableCount());
        DatabasePool.INSTANCE.giveBack(conn);
        System.out.println("Po zwrocie: " + DatabasePool.INSTANCE.availableCount());

        var ctor = AppConfigEager.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        AppConfigEager smuggled = ctor.newInstance();   // tworzymy DRUGĄ instancję!
        System.out.println(AppConfigEager.getInstance() == smuggled);   // false!

    }
}
