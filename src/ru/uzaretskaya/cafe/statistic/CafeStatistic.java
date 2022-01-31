package ru.uzaretskaya.cafe.statistic;

import ru.uzaretskaya.cafe.dto.Cashier;
import ru.uzaretskaya.cafe.dto.Order;
import ru.uzaretskaya.cafe.dto.User;
import ru.uzaretskaya.cafe.utils.CafeProperties;
import ru.uzaretskaya.cafe.utils.FileReaderWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CafeStatistic {

    private final String filenameForCashierStatistic;
    private final String filenameForUserStatistic;
    private final List<Order> completedOrders = new ArrayList<>();

    public CafeStatistic(CafeProperties properties) {
        filenameForCashierStatistic = properties.getCashierStatisticFilenameFromProperties();
        filenameForUserStatistic = properties.getUserStatisticFilenameFromProperties();

        prepareFilesForStatistic();
    }

    private void prepareFilesForStatistic() {
        try {
            FileReaderWriter.deleteFileIfExists(filenameForCashierStatistic);
            FileReaderWriter.deleteFileIfExists(filenameForUserStatistic);
        } catch (IOException e) {
            System.out.println("New statistic will be added in existing files.");
            e.printStackTrace();
        }
    }

    public void saveCompletedOrder(Order order) {
        completedOrders.add(order);
    }

    public List<String> getCashierStatistic(Map<String, Cashier> cashiers) {
        List<String> statistics = new ArrayList<>();

        for (Map.Entry<String, Cashier> entry : cashiers.entrySet()) {
            String cashierId = entry.getKey();
            List<Order> cashiersOrders = completedOrders.stream()
                    .filter(order -> order.getCashier() == entry.getValue())
                    .collect(Collectors.toList());

            double sumOrders = cashiersOrders.stream().mapToDouble(Order::getOrderSum).sum();

            // id, countOrders, sumOrders
            CashierStatistic cashierStatistic = new CashierStatistic(cashierId, cashiersOrders.size(), sumOrders);

            statistics.add(cashierStatistic.toString());
        }

        return statistics;
    }

    public List<String> getUserStatistic(Map<String, User> users) {
        List<String> statistics = new ArrayList<>();

        for (Map.Entry<String, User> entry : users.entrySet()) {
            String userId = entry.getKey();
            List<Order> userOrders = completedOrders.stream()
                    .filter(order -> order.getUser() == entry.getValue())
                    .collect(Collectors.toList());

            double sumOrders = userOrders.stream().mapToDouble(Order::getOrderSum).sum();
            int sumCalories = userOrders.stream().mapToInt(Order::getCaloriesSum).sum();
            int countOrders = userOrders.size();

            // id, countOrders, averageCalories, averageSum
            UserStatistic userStatistic =
                    new UserStatistic(userId, countOrders, (1.0 * sumCalories) / countOrders, sumOrders);

            statistics.add(userStatistic.toString());
        }

        return statistics;
    }

    public String getFilenameForCashierStatistic() {
        return filenameForCashierStatistic;
    }

    public String getFilenameForUserStatistic() {
        return filenameForUserStatistic;
    }
}
