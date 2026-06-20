package org.example.patterns.proxy;

import org.example.patterns.proxy.dynamicproxy.DynamicProxyFactory;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== STATYCZNY PROXY: logging + caching ===");
        UserService real    = new DatabaseUserService();
        UserService cached  = new CachingProxy(real);
        UserService logged  = new LoggingProxy(cached);

        logged.findById(5L);
        System.out.println();
        logged.findById(5L);

        System.out.println("\n=== DYNAMIC PROXY ===");
        UserService dynamicProxy = DynamicProxyFactory.wrap(
                new DatabaseUserService(), UserService.class);

        dynamicProxy.findById(42L);
        dynamicProxy.save("Anna");
    }
}
