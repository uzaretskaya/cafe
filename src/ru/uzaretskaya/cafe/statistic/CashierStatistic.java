package ru.uzaretskaya.cafe.statistic;


public record CashierStatistic(String cashierId, int countOrders, double sumOrders) {

    @Override
    public String toString() {
        return "" + cashierId + "," + countOrders + "," + sumOrders;
    }
}
