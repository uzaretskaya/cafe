package ru.uzaretskaya.cafe;

import ru.uzaretskaya.cafe.Meal;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<Meal> meals = new ArrayList<>();

    public Order() {}

    public Order(List<Meal> meals) {
        this.meals.addAll(meals);
    }

    public boolean addMeal(Meal meal) {
        return meals.add(meal);
    }

    public List<Meal> getMeals() {
        return new ArrayList<>(meals);
    }

}
