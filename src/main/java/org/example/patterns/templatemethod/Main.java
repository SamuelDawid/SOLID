package org.example.patterns.templatemethod;

public class Main {
    public static void main(String[] args) {

        DataImporter csv  = new CsvDataImporter();
        DataImporter json = new JsonDataImporter();
        DataImporter xml  = new XmlDataImporter();

        csv.importData("data.csv");
        System.out.println();
        json.importData("data.json");
        System.out.println();
        xml.importData("data.xml");
    }
}
