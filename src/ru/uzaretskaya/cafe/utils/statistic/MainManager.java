package ru.uzaretskaya.cafe.utils.statistic;

import ru.uzaretskaya.cafe.Cafe;
import ru.uzaretskaya.cafe.Cashier;
import ru.uzaretskaya.cafe.User;
import ru.uzaretskaya.cafe.utils.FileReaderWriter;
import ru.uzaretskaya.cafe.utils.Pair;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainManager implements Manager {
    private final Cafe cafe;
    private final int minutes;
    private final DecimalFormat df = new DecimalFormat("#.00");

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

        Pair<String,Double> result = getBestCashierIdAndAverageCheck(lines);

        List<Cashier> cafeCashiers = cafe.getCashiers();
        cafeCashiers.stream()
                .filter(cashier -> cashier.getId().toString().equals(result.getX()))
                .findFirst()
                .ifPresent(cashier ->
                        System.out.println("The best cashier is " + cashier + " with " + df.format(result.getY()) + " average check!"));
    }

    private Pair<String,Double> getBestCashierIdAndAverageCheck(List<String[]> lines) {
        Map<String, Pair<Integer, Double>> mapIdToOrdersCountAndOrdersSum = getMapIdToOrdersCountAndOrdersSum(lines);

        String resultId = "";
        double maxCheck = 0.0;
        for (Map.Entry<String, Pair<Integer,Double>> idToOrdersCountAndOrdersSum : mapIdToOrdersCountAndOrdersSum.entrySet()) {
            Pair<Integer,Double> ordersCountAndOrdersSum = idToOrdersCountAndOrdersSum.getValue();
            int ordersCount = ordersCountAndOrdersSum.getX();
            double ordersSum = ordersCountAndOrdersSum.getY();
            double averageCheck = ordersSum / ordersCount;
            if (averageCheck > maxCheck) {
                resultId = idToOrdersCountAndOrdersSum.getKey();
                maxCheck = averageCheck;
            }
        }
        return new Pair<>(resultId, maxCheck);
    }

    private Map<String, Pair<Integer, Double>> getMapIdToOrdersCountAndOrdersSum(List<String[]> lines) {
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

    private void getTheHungriestUser() {
        List<String[]> lines = FileReaderWriter.readFile(cafe.getFilenameForUserStatistic());
        if (lines == null) return;

        Pair<String,Double> result = calculateTheHungriestUser(lines);

        List<User> cafeUsers = cafe.getUsers();
        cafeUsers.stream()
                .filter(user -> user.getId().toString().equals(result.getX()))
                .findFirst()
                .ifPresent(user -> System.out.println("The hungriest user is " + user + " with " + df.format(result.getY()) + " calories!"));
    }

    private Map<String, Double> getMapIdToSumCalories(List<String[]> lines) {
        Map<String, Double> mapIdToSumCalories = new HashMap<>();
        for (String[] line : lines) {
            String currentId = line[0];
            double currentAverageCalories = Double.parseDouble(line[2]);
            double ordersCount = Integer.parseInt(line[1]);
            double currentCalories = currentAverageCalories * ordersCount;

            mapIdToSumCalories.merge(currentId, currentCalories, Double::sum);
        }
        return mapIdToSumCalories;
    }

    private Pair<String,Double> calculateTheHungriestUser(List<String[]> lines) {
        Map<String, Double> mapIdToSumCalories = getMapIdToSumCalories(lines);

        String resultId = "";
        double maxCalories = 0.0;
        for (Map.Entry<String, Double> idAndCalories : mapIdToSumCalories.entrySet()) {
            double currentCalories = idAndCalories.getValue();
            if (currentCalories > maxCalories) {
                resultId = idAndCalories.getKey();
                maxCalories = currentCalories;
            }
        }
        return new Pair<>(resultId, maxCalories);
    }
}
