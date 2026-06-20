package org.example.solid.zad04;

import java.util.List;

public class UserDisplayService {

    private final UserReader reader;

    public UserDisplayService(UserReader reader) {
        this.reader = reader;
    }

    public List<User> listAll() {
        return reader.findAll();
        // Klient widzi TYLKO findById / findAll — nie zna admin, auth ani cleanup
    }
}
