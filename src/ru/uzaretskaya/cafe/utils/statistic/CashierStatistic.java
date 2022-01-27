package ru.uzaretskaya.cafe.utils.statistic;

import java.util.UUID;

public record CashierStatistic(UUID cashierId, int countOrders, double sumOrders) {
    @Override
    public String toString() {
        return "" + cashierId + "," + countOrders + "," + sumOrders;
    }
}
