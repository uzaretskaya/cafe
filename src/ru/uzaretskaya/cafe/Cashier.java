package ru.uzaretskaya.cafe;

import ru.uzaretskaya.cafe.utils.statistic.dto.CashierStatistic;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Cashier implements Runnable {
    private final UUID id;
    private final String name;
    private final Cafe cafe;

    private final AtomicInteger countOrders = new AtomicInteger(0);
    private volatile double sum = 0;

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
        CashierStatistic statistic;
        synchronized (this) {
            statistic = new CashierStatistic(id, countOrders.get(), sum);
            countOrders.set(0);
            sum = 0;
        }
        return statistic;
    }

    @Override
    public String toString() {
        return name;
    }

    private void addStatistic(double orderSum) {
        synchronized (this) {
            sum += orderSum;
        }
        countOrders.addAndGet(1);
    }
}
