package ru.uzaretskaya.cafe;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User implements Runnable {
    private final UUID id;
    private final String name;
    private final Cafe cafe;

    public User(String name, Cafe cafe) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.cafe = cafe;
    }

    public void makeOrder() {
        List<Meal> mealsForOrder = new ArrayList<>();
        List<Meal> menu = cafe.getMenu();

        int countMeals = getRandomNumber(1, 3);
        for (int i = 0; i < countMeals; i++) {
            int mealIndex = getRandomNumber(0, menu.size() - 1);
            mealsForOrder.add(menu.get(mealIndex));
        }
        synchronized (cafe) {
            cafe.createOrder(mealsForOrder, this);
        }
    }

    public UUID getId() {
        return id;
    }

    @Override
    public void run() {
        while (true) {
            if (cafe.isCafeClosed()) return;

            sleepForHalfMinute();
            makeOrder();
        }
    }

    @Override
    public String toString() {
        return name;
    }

    private void sleepForHalfMinute() {
        try {
            Thread.sleep(1000L * 30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
