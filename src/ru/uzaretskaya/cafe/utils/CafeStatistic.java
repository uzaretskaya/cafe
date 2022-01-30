package ru.uzaretskaya.cafe.utils;

import ru.uzaretskaya.cafe.Cashier;
import ru.uzaretskaya.cafe.Order;
import ru.uzaretskaya.cafe.utils.statistic.dto.UserStatistic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CafeStatistic {

    private String filenameForCashierStatistic;
    private String filenameForUserStatistic;
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

    public String getFilenameForCashierStatistic() {
        return filenameForCashierStatistic;
    }

    public String getFilenameForUserStatistic() {
        return filenameForUserStatistic;
    }

    public List<String> getCashierStatistic() {
        List<String> statistics = new ArrayList<>(cashiers.size());
        for (Cashier cashier : cashiers) {
            var cashierStatistic = cashier.getStatistic();
            if (cashierStatistic.countOrders() > 0) {
                statistics.add(cashierStatistic.toString());
            }
        }
        return statistics;
    }

    public List<String> getUserStatistic() {
        List<String> statistic = new ArrayList<>(userStatistic.size());
        for (Map.Entry<UUID, UserStatistic> entry : userStatistic.entrySet()) {
            statistic.add(entry.getKey() + "," + entry.getValue().getUserStatisticInfo());
        }
        userStatistic.clear();
        return statistic;
    }
}
