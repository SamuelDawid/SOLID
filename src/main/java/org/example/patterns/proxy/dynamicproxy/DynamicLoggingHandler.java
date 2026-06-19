package org.example.patterns.proxy.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * DYNAMIC PROXY — InvocationHandler obsługuje WSZYSTKIE metody interfejsu.
 * Nie musisz ręcznie pisać proxy dla każdej klasy — jeden handler załatwia całość.
 */
public class DynamicLoggingHandler implements InvocationHandler {

    private final Object target;

    public DynamicLoggingHandler(Object target) { this.target = target; }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long start = System.nanoTime();
        System.out.println("[DYNAMIC-LOG] " + method.getName() + " — start");
        try {
            Object result = method.invoke(target, args);
            System.out.printf("[DYNAMIC-LOG] %s — %dms — %s%n",
                    method.getName(),
                    (System.nanoTime() - start) / 1_000_000,
                    result);
            return result;
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw e.getCause();
        }
    }
}
