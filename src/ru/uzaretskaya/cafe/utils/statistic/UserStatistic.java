package ru.uzaretskaya.cafe.utils.statistic;

import java.util.UUID;

public record UserStatistic(UUID cashierId, int countOrders, double averageCalories, double averageOrderSum) {
    @Override
    public String toString() {
        return "" + cashierId + "," + countOrders + "," + averageCalories + ", " + averageOrderSum;
    }
}
