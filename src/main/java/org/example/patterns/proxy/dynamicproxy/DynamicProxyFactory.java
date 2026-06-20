package org.example.patterns.proxy.dynamicproxy;

import java.lang.reflect.Proxy;

public class DynamicProxyFactory {

    @SuppressWarnings("unchecked")
    public static <T> T wrap(T target, Class<T> iface) {
        return (T) Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class<?>[] { iface },
                new DynamicLoggingHandler(target)
        );
    }
}
