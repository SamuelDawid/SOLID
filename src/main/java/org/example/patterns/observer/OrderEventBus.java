package org.example.patterns.observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * EventBus — ręczna implementacja Observer.
 *
 * CopyOnWriteArrayList chroni przed ConcurrentModificationException,
 * gdy listener rejestruje/wyrejestrowuje się w trakcie publish().
 *
 * Try/catch wokół każdego listenera — błąd jednego NIE zatrzymuje pozostałych.
 */
public class OrderEventBus {

    private final List<OrderEventListener> listeners = new CopyOnWriteArrayList<>();

    public void subscribe(OrderEventListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(OrderEventListener listener) {
        listeners.remove(listener);
    }

    public void publish(OrderEvent event) {
        System.out.printf("[BUS] publikuję %s [%s]%n",
                event.getClass().getSimpleName(), event.orderId());
        for (OrderEventListener listener : listeners) {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                System.err.printf("[BUS] Listener %s rzucił błąd: %s%n",
                        listener.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
}
