package org.example.patterns.cor;

import java.util.Map;

public record HttpRequest(String path, String token, String body,
                          Map<String, String> headers) {

    public boolean hasToken() { return token != null && !token.isBlank(); }
    public boolean isJson()   { return "application/json".equals(headers.get("Content-Type")); }
}
