package org.example.patterns.decorator;

public class Main {
    public static void main(String[] args) {

        // 1) Czyste espresso
        Coffee espresso = new Espresso();
        printCoffee(espresso);

        // 2) Americano z mlekiem
        Coffee americanoMleko = new MilkDecorator(new Americano());
        printCoffee(americanoMleko);

        // 3) Espresso z mlekiem, cukrem i bitą śmietaną
        Coffee zlozona = new WhippedCreamDecorator(
                            new SugarDecorator(
                                new MilkDecorator(
                                    new Espresso())));
        printCoffee(zlozona);

        // 4) Americano z dwiema porcjami cukru
        Coffee podwojnyCukier = new SugarDecorator(new SugarDecorator(new Americano()));
        printCoffee(podwojnyCukier);

        // 5) Z syropem waniliowym
        Coffee waniliowa = new SyrupDecorator(new MilkDecorator(new Espresso()), "wanilia");
        printCoffee(waniliowa);
    }

    private static void printCoffee(Coffee c) {
        System.out.printf("%-50s %.2f PLN%n", c.description(), c.cost());
    }
}
