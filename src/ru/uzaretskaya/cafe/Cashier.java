package ru.uzaretskaya.cafe;

import ru.uzaretskaya.cafe.utils.statistic.dto.CashierStatistic;

import java.util.List;
import java.util.UUID;

public class Cashier implements Runnable {
    private final UUID id;
    private final String name;
    private final Cafe cafe;

    private int countOrders = 0;
    private double sum = 0;

    public Cashier(String name, Cafe cafe) {
        this.cafe = cafe;
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void run() {
        while (true) {
            if (cafe.isCafeClosed()) return;

            Order currentOrder = cafe.getCurrentOrder();
            if (currentOrder != null) {
                List<Meal> mealsToPrepare = currentOrder.getMeals();
                try {
                    Thread.sleep(1000L * mealsToPrepare.size());
                    addStatistic(currentOrder.getOrderSum());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public CashierStatistic getStatistic() {
        CashierStatistic statistic = new CashierStatistic(id, countOrders, sum);
        countOrders = 0;
        sum = 0;
        return statistic;
    }

    @Override
    public String toString() {
        return name;
    }

    private void addStatistic(double orderSum) {
        sum += orderSum;
        countOrders++;
    }
}
