package ru.uzaretskaya.cafe.utils.statistic;

import ru.uzaretskaya.cafe.Cafe;
import ru.uzaretskaya.cafe.Cashier;
import ru.uzaretskaya.cafe.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainManager implements Manager {
    private final Cafe cafe;
    private final int minutes;

    public MainManager(Cafe cafe, int minutes) {
        this.cafe = cafe;
        this.minutes = minutes;
    }

    public void run() {
        while (true) {
            if (cafe.isCafeClosed()) return;
            sleepForMinutes(minutes);
            readStatistic();
        }
    }

    public void readStatistic() {
        getTheBestCashier();
        getTheHungriestUser();
    }

    private void getTheBestCashier() {
        List<String[]> lines = readFile(cafe.getFilenameForCashierStatistic());
        if (lines == null) return;

        Map<String, StatisticInfo> map = new HashMap<>();
        for (String[] line : lines) {
            String currentKey = line[0];
            int currentCount = Integer.parseInt(line[1]);
            double currentSum = Double.parseDouble(line[2]);

            StatisticInfo current = map.get(currentKey);
            if (current == null) {
                map.put(currentKey, new StatisticInfo(currentCount, currentSum));
            } else {
                current.count += currentCount;
                current.sum += currentSum;
            }
        }

        StatisticResult result = calculateTheBestCashier(map);

        List<Cashier> cafeCashiers = cafe.getCashiers();
        cafeCashiers.stream()
                .filter(cashier -> cashier.getId().toString().equals(result.id))
                .findFirst()
                .ifPresent(cashier ->
                        System.out.println("The best cashier is " + cashier + " with " + result.maxValue + " average check!"));
    }

    private StatisticResult calculateTheBestCashier(Map<String, StatisticInfo> map) {
        String resultId = "";
        double maxCheck = 0.0;
        for (Map.Entry<String, StatisticInfo> entry : map.entrySet()) {
            StatisticInfo current = entry.getValue();
            double averageCheck = current.sum / current.count;
            if (averageCheck > maxCheck) {
                resultId = entry.getKey();
            }
        }
        return new StatisticResult(resultId, maxCheck);
    }

    private void getTheHungriestUser() {
        List<String[]> lines = readFile(cafe.getFilenameForUserStatistic());
        if (lines == null) return;

        Map<String, Double> map = new HashMap<>();
        for (String[] line : lines) {
            String currentId = line[0];
            double currentCalories = Double.parseDouble(line[2]);
            map.merge(currentId, currentCalories, Double::sum);
        }

        StatisticResult result = calculateTheHungriestUser(map);

        List<User> cafeUsers = cafe.getUsers();
        cafeUsers.stream()
                .filter(user -> user.getId().toString().equals(result.id))
                .findFirst()
                .ifPresent(user -> System.out.println("The hungriest user is " + user + " with " + result.maxValue + " calories!"));
    }

    private StatisticResult calculateTheHungriestUser(Map<String, Double> map) {
        String resultId = "";
        double maxCalories = 0.0;
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            double currentCalories = entry.getValue();
            if (currentCalories > maxCalories) {
                resultId = entry.getKey();
            }
        }
        return new StatisticResult(resultId, maxCalories);
    }

    private List<String[]> readFile(String filename) {
        try {
            return Files.lines(Paths.get(filename))
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class StatisticInfo {
        int count;
        double sum;

        public StatisticInfo(int count, double sum) {
            this.count = count;
            this.sum = sum;
        }
    }

    private class StatisticResult {
        String id;
        double maxValue;

        public StatisticResult(String id, double maxValue) {
            this.id = id;
            this.maxValue = maxValue;
        }
    }
}
