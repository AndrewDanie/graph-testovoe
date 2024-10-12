package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        var startTime = System.nanoTime();

        if (args.length == 0) {
            System.out.println("Не введен относительный путь к файлу");
            return;
        }

        var solution = new Solution();
        solution.loadData(args[0]);
        solution.solve();
        solution.saveResult("output.txt");
        System.out.println("Время выполнения программы (мс): " + (float)(System.nanoTime() - startTime)/1000000);
    }
}