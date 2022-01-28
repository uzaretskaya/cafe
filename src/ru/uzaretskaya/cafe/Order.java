package ru.uzaretskaya.cafe;

import ru.uzaretskaya.cafe.utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final List<Meal> meals = new ArrayList<>();
    private final int number;

    public Order(List<Meal> meals, int number) {
        this.meals.addAll(meals);
        this.number = number;
    }

    public List<Meal> getMeals() {
        return new ArrayList<>(meals);
    }

    public double getOrderSum() {
        return meals.stream().mapToDouble(Meal::getCost).sum();
    }

    public Pair<Double,Double> getOrderAverageCaloriesAndAverageSum(){
        int countMeals = meals.size();
        double sumCost = 0;
        int sumCalories = 0;
        for (Meal meal : meals) {
            sumCalories += meal.getCalories();
            sumCost += meal.getCost();
        }
        double averageCalories = sumCalories * 1.0 / countMeals;
        double averageSum = sumCost / countMeals;
        return new Pair<>(averageCalories, averageSum);
    }

    @Override
    public String toString() {
        return "Order #" + number + " " + meals;
    }
}
