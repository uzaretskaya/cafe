package ru.uzaretskaya.cafe.utils.statistic.dto;

import java.util.UUID;

public record UserStatistic(int countOrders, double averageCalories, double averageOrderSum) {
    @Override
    public String toString() {
        return "" + countOrders + "," + averageCalories + "," + averageOrderSum;
    }
}
