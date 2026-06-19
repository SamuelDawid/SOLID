package org.example.patterns.templatemethod;

import java.util.ArrayList;
import java.util.List;

public class CsvDataImporter extends DataImporter {

    @Override
    protected List<Record> parse(String raw) {
        System.out.println("[PARSE] CSV");
        List<Record> result = new ArrayList<>();
        for (String line : raw.split("\n")) {
            String[] cols = line.split(",");
            result.add(new Record(cols[0], Integer.parseInt(cols[1])));
        }
        return result;
    }
}
