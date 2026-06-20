package org.example.patterns.composite;

public class Main {
    public static void main(String[] args) {

        // 1) System plików
        FolderNode root = new FolderNode("root");
        root.add(new FileNode("readme.txt", 200));
        FolderNode src = new FolderNode("src");
        src.add(new FileNode("Main.java", 1500));
        src.add(new FileNode("Utils.java", 800));
        root.add(src);

        FolderNode tests = new FolderNode("tests");
        tests.add(new FileNode("MainTest.java", 1100));
        root.add(tests);

        root.print("");
        System.out.println("\nRozmiar całości: " + root.getSize() + " B");

        // 2) Struktura organizacyjna
        System.out.println("\n=== Struktura organizacyjna ===");
        Employee cto = new Employee("CTO Marek", 25000);
        cto.addReport(new Employee("Dev Anna", 10000));
        cto.addReport(new Employee("Dev Jan", 11000));

        Employee teamLead = new Employee("TL Kasia", 16000);
        teamLead.addReport(new Employee("Junior Tomek", 6500));
        teamLead.addReport(new Employee("Junior Ola", 6500));
        cto.addReport(teamLead);

        cto.print("");
        System.out.printf("%nCałkowity koszt: %.2f PLN, rozmiar zespołu: %d osób%n",
                cto.totalSalaryCost(), cto.teamSize());
    }
}
