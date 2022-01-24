package ru.uzaretskaya.cafe;

import java.util.Objects;

public class Meal {
    private String name;
    private int calories;
    private int cost;

    public Meal(String name, int calories, int cost) {
        this.name = name;
        this.calories = calories;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Meal meal) {
            return calories == meal.calories && cost == meal.cost && name.equals(meal.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, calories, cost);
    }
}
