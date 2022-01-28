package ru.uzaretskaya.cafe.utils.statistic;

import ru.uzaretskaya.cafe.Cafe;
import ru.uzaretskaya.cafe.Cashier;
import ru.uzaretskaya.cafe.User;
import ru.uzaretskaya.cafe.utils.FileReaderWriter;
import ru.uzaretskaya.cafe.utils.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<String[]> lines = FileReaderWriter.readFile(cafe.getFilenameForCashierStatistic());
        if (lines == null) return;

        Map<String, Pair<Integer, Double>> mapIdToOrdersCountAndOrdersSum = collectToMap(lines);
        Pair<String,Double> result = getBestCashierIdAndAverageCheck(mapIdToOrdersCountAndOrdersSum);

        List<Cashier> cafeCashiers = cafe.getCashiers();
        cafeCashiers.stream()
                .filter(cashier -> cashier.getId().toString().equals(result.getX()))
                .findFirst()
                .ifPresent(cashier ->
                        System.out.println("The best cashier is " + cashier + " with " + result.getY() + " average check!"));
    }

    private Map<String, Pair<Integer, Double>> collectToMap(List<String[]> lines) {
        Map<String, Pair<Integer, Double>> map = new HashMap<>();
        for (String[] line : lines) {
            String currentKey = line[0];
            Integer currentOrdersCount = Integer.parseInt(line[1]);
            Double currentOrdersSum = Double.parseDouble(line[2]);

            Pair<Integer, Double> existingRecord = map.get(currentKey);
            if (existingRecord == null) {
                map.put(currentKey, new Pair<>(currentOrdersCount, currentOrdersSum));
            } else {
                existingRecord.setX(existingRecord.getX() + currentOrdersCount);
                existingRecord.setY(existingRecord.getY() + currentOrdersSum);
            }
        }
        return map;
    }

    private Pair<String,Double> getBestCashierIdAndAverageCheck(Map<String, Pair<Integer,Double>> map) {
        String resultId = "";
        double maxCheck = 0.0;
        for (Map.Entry<String, Pair<Integer,Double>> entry : map.entrySet()) {
            Pair<Integer,Double> ordersCountAndOrdersSum = entry.getValue();
            int ordersCount = ordersCountAndOrdersSum.getX();
            double ordersSum = ordersCountAndOrdersSum.getY();
            double averageCheck = ordersSum / ordersCount;
            if (averageCheck > maxCheck) {
                resultId = entry.getKey();
            }
        }
        return new Pair<>(resultId, maxCheck);
    }

    private void getTheHungriestUser() {
        List<String[]> lines = FileReaderWriter.readFile(cafe.getFilenameForUserStatistic());
        if (lines == null) return;

        Map<String, Double> mapIdToSumCalories = getMapIdToSumCalories(lines);
        Pair<String,Double> result = calculateTheHungriestUser(mapIdToSumCalories);

        List<User> cafeUsers = cafe.getUsers();
        cafeUsers.stream()
                .filter(user -> user.getId().toString().equals(result.getX()))
                .findFirst()
                .ifPresent(user -> System.out.println("The hungriest user is " + user + " with " + result.getY() + " calories!"));
    }

    private Map<String, Double> getMapIdToSumCalories(List<String[]> lines) {
        Map<String, Double> mapIdToSumCalories = new HashMap<>();
        for (String[] line : lines) {
            String currentId = line[0];
            double currentCalories = Double.parseDouble(line[2]);
            mapIdToSumCalories.merge(currentId, currentCalories, Double::sum);
        }
        return mapIdToSumCalories;
    }

    private Pair<String,Double> calculateTheHungriestUser(Map<String, Double> map) {
        String resultId = "";
        double maxCalories = 0.0;
        for (Map.Entry<String, Double> idAndCalories : map.entrySet()) {
            double currentCalories = idAndCalories.getValue();
            if (currentCalories > maxCalories) {
                resultId = idAndCalories.getKey();
            }
        }
        return new Pair<>(resultId, maxCalories);
    }
}
