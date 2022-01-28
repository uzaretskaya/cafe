package ru.uzaretskaya.cafe;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ru.uzaretskaya.cafe.utils.Random.getRandomNumber;

public class User implements Runnable {
    private final UUID id;
    private final String name;
    private final Cafe cafe;
    private final List<Meal> menu;

    public User(String name, Cafe cafe) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.cafe = cafe;
        this.menu = cafe.getMenu();
    }

    public void makeOrder() {
        List<Meal> mealsForOrder = new ArrayList<>();
        int countMeals = getRandomNumber(1, 3);
        for (int i = 0; i < countMeals; i++) {
            int mealIndex = getRandomNumber(0, menu.size() - 1);
            mealsForOrder.add(menu.get(mealIndex));
        }
        //System.out.println(this + " ordered " + mealsForOrder);
        cafe.createOrder(mealsForOrder, this);
    }

    public UUID getId() {
        return id;
    }

    public void run() {
        while (true) {
            if (cafe.isCafeClosed()) return;

            sleepForHalfMinute();
            makeOrder();
        }
    }

    private void sleepForHalfMinute() {
        try {
            Thread.sleep(1000L * 30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User customer = (User) o;
        return id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Customer {" +
                "name='" + name + '\'' +
                "id='" + id + '\'' +
                '}';
    }

}
