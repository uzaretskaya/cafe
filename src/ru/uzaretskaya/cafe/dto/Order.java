package ru.uzaretskaya.cafe.dto;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final List<Meal> meals = new ArrayList<>();
    private final int number;
    private final User user;
    private Cashier cashier;

    public Order(List<Meal> meals, int number, User user) {
        this.meals.addAll(meals);
        this.number = number;
        this.user = user;
    }

    public Cashier getCashier() {
        return cashier;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public User getUser() {
        return user;
    }

    public List<Meal> getMeals() {
        return new ArrayList<>(meals);
    }

    public double getOrderSum() {
        return meals.stream().mapToDouble(Meal::getCost).sum();
    }

    public int getCaloriesSum() {
        return meals.stream().mapToInt(Meal::getCalories).sum();
    }

    @Override
    public String toString() {
        return "Order #" + number + ": " + meals + " for " + user;
    }
}
