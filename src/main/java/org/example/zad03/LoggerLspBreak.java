package org.example.zad03;

import java.io.IOException;

class FileLogger {
    public void log(String message) {
        // może rzucić RuntimeException jeśli plik jest zablokowany
    }
}

class NetworkLogger extends FileLogger {
    @Override
    public void log(String message) {
        // PUŁAPKA: rzuca IOException, na które klient FileLogger nie był przygotowany
        // — w prawdziwym kodzie wymusza checked exception lub łapanie Exception
        throw new RuntimeException(new IOException("Sieć niedostępna"));
    }
}
