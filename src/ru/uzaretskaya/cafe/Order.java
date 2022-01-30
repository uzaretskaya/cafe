package ru.uzaretskaya.cafe;

import ru.uzaretskaya.cafe.utils.Pair;

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

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public List<Meal> getMeals() {
        return new ArrayList<>(meals);
    }

    public double getOrderSum() {
        return meals.stream().mapToDouble(Meal::getCost).sum();
    }

    public Pair<Integer,Double> getOrderCaloriesAndSum(){
        double sumCost = 0;
        int sumCalories = 0;
        for (Meal meal : meals) {
            sumCalories += meal.getCalories();
            sumCost += meal.getCost();
        }
        return new Pair<>(sumCalories, sumCost);
    }

    @Override
    public String toString() {
        return "Order #" + number + ": " + meals + " for " + user;
    }
}
