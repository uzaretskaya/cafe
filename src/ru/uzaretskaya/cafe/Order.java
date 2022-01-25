package ru.uzaretskaya.cafe;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final List<Meal> meals = new ArrayList<>();
    private int number;
    private final Customer customer;
    private boolean isReady = false;

    public Order(List<Meal> meals, int number, Customer customer) {
        this.customer = customer;
        this.meals.addAll(meals);
        this.number = number;
    }

    public List<Meal> getMeals() {
        return new ArrayList<>(meals);
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    @Override
    public String toString() {
        return "Order #" + number + " " + meals;
    }
}
