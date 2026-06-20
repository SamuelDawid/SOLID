package org.example.patterns.templatemethod;

import java.util.ArrayList;
import java.util.List;

public class JsonDataImporter extends DataImporter {

    @Override
    protected String read(String filePath) {
        System.out.println("[READ JSON] " + filePath);
        return "[{\"name\":\"Anna\",\"age\":30},{\"name\":\"Jan\",\"age\":25}]";
    }

    @Override
    protected List<Record> parse(String raw) {
        System.out.println("[PARSE] JSON (uproszczone)");
        List<Record> result = new ArrayList<>();
        String stripped = raw.replaceAll("[\\[\\]{}\"]", "");
        for (String entry : stripped.split(",(?=name:)")) {
            String[] parts = entry.split(",");
            String name = parts[0].split(":")[1];
            int age = Integer.parseInt(parts[1].split(":")[1]);
            result.add(new Record(name, age));
        }
        return result;
    }

    @Override
    protected void onError(Exception e) {
        System.err.println("[JSON IMPORTER] Specjalna obsługa błędu: " + e.getMessage());
    }
}
