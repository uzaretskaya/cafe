package ru.uzaretskaya.cafe.statistic;

public record UserStatisticCalculated(
        String userId,
        int countOrders,
        double averageCalories,
        double averageSum,
        double sumCalories
) {
}
