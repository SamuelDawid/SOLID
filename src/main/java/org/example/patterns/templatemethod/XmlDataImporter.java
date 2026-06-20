package org.example.patterns.templatemethod;

import java.util.ArrayList;
import java.util.List;

public class XmlDataImporter extends DataImporter {

    @Override
    protected String read(String filePath) {
        System.out.println("[READ XML] " + filePath);
        return "<users><user name='Anna' age='30'/><user name='Jan' age='25'/></users>";
    }

    @Override
    protected List<Record> parse(String raw) {
        System.out.println("[PARSE] XML (uproszczone)");
        List<Record> result = new ArrayList<>();
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(
                "<user name='([^']+)' age='(\\d+)'/>");
        java.util.regex.Matcher m = p.matcher(raw);
        while (m.find()) {
            result.add(new Record(m.group(1), Integer.parseInt(m.group(2))));
        }
        return result;
    }

    @Override
    protected List<Record> validate(List<Record> records) {
        List<Record> base = super.validate(records);
        return base.stream().filter(r -> r.name().length() >= 2).toList();
    }
}
