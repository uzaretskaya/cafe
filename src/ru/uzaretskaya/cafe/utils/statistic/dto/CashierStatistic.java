package ru.uzaretskaya.cafe.utils.statistic.dto;

import java.util.UUID;

public record CashierStatistic(UUID cashierId, int countOrders, double sumOrders) {
    @Override
    public String toString() {
        return "" + cashierId + "," + countOrders + "," + sumOrders;
    }
}
