package ru.uzaretskaya.cafe.dto;

import ru.uzaretskaya.cafe.Cafe;

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

    @Override
    public void run() {
        while (cafe.isOpen()) {
            sleepForHalfMinute();
            createOrder();
        }
    }

    private void createOrder() {
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

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
