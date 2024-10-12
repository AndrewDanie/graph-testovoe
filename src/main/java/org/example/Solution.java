package org.example;

import java.io.*;
import java.util.*;

public class Solution {
    private ArrayList<String> lines = new ArrayList<>();
    private List<List<Integer>> resultList = new ArrayList<>();

    private record Row(int index, List<String> values) {}
    private List<Row> matrix = new ArrayList<>();
    private int cols = 0;

    public void solve() {
        // Сортируем матрицу для построения графа за O(кол-во values) вместо O(maxLineLength * lines)
        matrix = matrix.stream()
                .sorted((row1, row2) -> row2.values().size() - row1.values().size())
                .toList();

        // Инициализация графа
        List<Set<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            graph.add(new HashSet<>());
        }
        // Проверяем все колонки на повторяющиеся значения, строим граф
        for (int j = 0; j < matrix.getFirst().values().size(); j++) {
            var colMap = new HashMap<String, List<Integer>>();

            var i = 0;
            while (i < matrix.size() && matrix.get(i).values().size() > j) {
                var value = matrix.get(i).values().get(j);
                if (!value.equals("\"\"")) {
                    if (!colMap.containsKey(value)) {
                        var array = new ArrayList<Integer>();
                        array.add(i);
                        colMap.put(value, array);

                    } else {
                        var existedCols = colMap.get(value);
                        var firstNodeIdx = existedCols.getLast();
                        existedCols.add(i);

                        graph.get(firstNodeIdx).add(i);
                        graph.get(i).add(firstNodeIdx);
                    }
                }
                i++;
            }
        }

        // Находим все компоненты связности
        var stackDFS = new ArrayList<Integer>();
        var toVisitNodes = new HashSet<Integer>();
        var visitedNodes = new HashSet<Integer>();

        for (int i = 0; i < cols; i++) {
            if (!visitedNodes.contains(i)) {
                stackDFS.add(i);
                toVisitNodes.add(i);
                var resultGroup = new ArrayList<Integer>();

                while (!stackDFS.isEmpty()) {
                    var nodeIdx = stackDFS.removeLast();
                    toVisitNodes.remove(nodeIdx);
                    visitedNodes.add(nodeIdx);
                    resultGroup.add(nodeIdx);
                    for (var ngbr : graph.get(nodeIdx)) {
                        if (!visitedNodes.contains(ngbr) && !toVisitNodes.contains(ngbr)) {
                            stackDFS.add(ngbr);
                            toVisitNodes.add(ngbr);
                        }
                    }
                }
                resultList.add(resultGroup);
            }
        }
        // Сортируем результат перед выводом
        resultList = resultList.stream()
                .map(group -> group.stream().sorted().toList())
                .sorted((arr1, arr2) -> arr2.size() - arr1.size())
                .toList();


    }

    /**
     * Загрузка данных в память и валидация
     */
    public void loadData(String filePathStr)  throws IOException {
        var filePath = new File(filePathStr);
        if (!filePath.isAbsolute()) {
            filePathStr = new File("").getAbsolutePath().concat("/").concat(filePathStr);
        }
        try (var fReader = new FileReader(filePathStr);
             var bfReader = new BufferedReader(fReader)) {

            var hashDict = new HashMap<Integer, Integer>();
            String line;

            while ((line = bfReader.readLine()) != null) {
                var items = line.split(";");
                var goodRowFlag = true;
                for (var item : items) {
                    if (!item.matches("\"\\d*\\.*\\d*\"")) {
                        goodRowFlag = false;
                        break;
                    }
                }
                if (goodRowFlag) {
                    var itemlist = Arrays.stream(items).toList();
                    var linehash = line.hashCode();
                    if (!(hashDict.containsKey(linehash) && line.equals(lines.get(hashDict.get(linehash))))) {
                        hashDict.put(linehash, cols);
                        matrix.add(new Row(cols, itemlist));
                        cols++;
                        lines.add(line);
                    }
                }
            }
        }
    }

    /**
     * Запись результата в файл
     */
    public void saveResult(String filePath) throws IOException {

        try (var fWriter = new FileWriter(filePath);
             var bfWriter = new BufferedWriter(fWriter)) {

            var counter = 0;
            for (var integers : resultList) {
                if (integers.size() > 1) counter++;
            }
            System.out.println("Количество групп с более чем одним элементом: " + counter);
            bfWriter.write(Integer.toString(counter));
            bfWriter.newLine();

            for (int i = 0; i < resultList.size(); i++) {

                bfWriter.write("Группа " + (i + 1));
                bfWriter.newLine();
                for (var idx : resultList.get(i)) {
                    bfWriter.write(lines.get(matrix.get(idx).index()));
                    bfWriter.newLine();
                }
                bfWriter.newLine();
            }
        }
    }

    /**
     * Очистка памяти для прогона тестов
     */
    public void clear() {
        lines = new ArrayList<>();
        resultList = new ArrayList<>();
        matrix = new ArrayList<>();
        cols = 0;
    }

    /**
     * Для тестов
     */
    public String getResultText() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < resultList.size(); i++) {
            builder.append("Группа ")
                    .append(i + 1)
                    .append('\r');
            for (var idx : resultList.get(i)) {
                builder.append(lines.get(matrix.get(idx).index()))
                        .append('\r');
            }
            builder.append('\r');
        }
        return builder.toString();
    }
}