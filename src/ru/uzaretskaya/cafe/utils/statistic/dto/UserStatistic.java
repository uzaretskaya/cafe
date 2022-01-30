package ru.uzaretskaya.cafe.utils.statistic.dto;

public class UserStatistic {
    int countOrders;
    int sumCalories;
    double ordersSum;

    public UserStatistic(int countOrders, int calories, double orderSum) {
        this.countOrders = countOrders;
        this.sumCalories = calories;
        this.ordersSum = orderSum;
    }

    public void updateAddingValues(int countOrders, int calories, double orderSum) {
        this.countOrders += countOrders;
        this.sumCalories += calories;
        this.ordersSum += orderSum;
    }

    public String getUserStatisticInfo() {
        return "" + countOrders + "," + (1.0 * sumCalories) / countOrders + "," + ordersSum / countOrders;
    }

    @Override
    public String toString() {
        return "" + countOrders + "," + sumCalories + "," + ordersSum;
    }
}
