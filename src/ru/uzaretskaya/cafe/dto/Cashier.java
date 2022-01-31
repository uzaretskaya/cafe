package ru.uzaretskaya.cafe.dto;

import ru.uzaretskaya.cafe.Cafe;

import java.util.List;
import java.util.UUID;

public class Cashier implements Runnable {

    private final UUID id;
    private final String name;
    private final Cafe cafe;

    public Cashier(String name, Cafe cafe) {
        this.id = UUID.randomUUID();
        this.cafe = cafe;
        this.name = name;
    }

    @Override
    public void run() {
        while (cafe.isOpen()) {
            Order currentOrder = cafe.getCurrentOrder();
            if (currentOrder != null) {
                makeOrder(currentOrder);
            }
        }
    }

    private void makeOrder(Order currentOrder) {
        List<Meal> mealsToPrepare = currentOrder.getMeals();
        try {
            Thread.sleep(1000L * mealsToPrepare.size());
            currentOrder.setCashier(this);
            synchronized (cafe) {
                cafe.saveCompletedOrder(currentOrder);
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
