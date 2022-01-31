package ru.uzaretskaya.cafe.statistic;

public record UserStatistic(String userId, int countOrders, double averageCalories, double averageSum) {

    @Override
    public String toString() {
        return "" + userId + "," + countOrders + "," + averageCalories + "," + averageSum;
    }
}
