package org.example.patterns.prototype;

import java.util.Map;
import java.util.HashMap;

/**
 * Drugi przykład — Prototype z metodą withXxx().
 * Klasa niemutowalna — każde wywołanie withXxx zwraca NOWĄ instancję.
 * To wzorzec znany z record'ów (Java 14+).
 */
public class ServerConfig {

    private final String host;
    private final int port;
    private final Map<String, String> headers;

    public ServerConfig(String host, int port, Map<String, String> headers) {
        this.host = host;
        this.port = port;
        this.headers = Map.copyOf(headers);
    }

    public ServerConfig withHost(String host) {
        return new ServerConfig(host, this.port, this.headers);
    }

    public ServerConfig withPort(int port) {
        return new ServerConfig(this.host, port, this.headers);
    }

    public ServerConfig withHeader(String name, String value) {
        Map<String, String> merged = new HashMap<>(this.headers);
        merged.put(name, value);
        return new ServerConfig(this.host, this.port, merged);
    }

    public String getHost()                       { return host; }
    public int getPort()                          { return port; }
    public Map<String, String> getHeaders()       { return headers; }

    @Override public String toString() {
        return "ServerConfig{host=%s, port=%d, headers=%s}".formatted(host, port, headers);
    }
}
