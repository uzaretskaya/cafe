package ru.uzaretskaya.cafe.statistic;


public record CashierStatisticCalculated(
        String cashierId,
        int countOrders,
        double sumOrders,
        double averageCheck
) {
}
