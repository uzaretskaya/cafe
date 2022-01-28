package ru.uzaretskaya.cafe.utils.statistic.dto;

public class UserStatistic {
    int countOrders;
    double averageCalories;
    double averageOrderSum;

    public UserStatistic(int countOrders, double averageCalories, double averageOrderSum) {
        this.countOrders = countOrders;
        this.averageCalories = averageCalories;
        this.averageOrderSum = averageOrderSum;
    }

    public void updateAddingValues(int countOrders, Double averageCalories, double averageOrderSum) {
        this.countOrders += countOrders;
        this.averageCalories += averageCalories;
        this.averageOrderSum += averageOrderSum;
    }

    @Override
    public String toString() {
        return "" + countOrders + "," + averageCalories + "," + averageOrderSum;
    }
}
