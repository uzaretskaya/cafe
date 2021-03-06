package ru.uzaretskaya.cafe.dto;

public class Meal {

    private final String name;
    private final int calories;
    private final double cost;

    public Meal(String name, int calories, int cost) {
        this.name = name;
        this.calories = calories;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public String toString() {
        return "Meal {" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                ", cost=" + cost +
                '}';
    }
}
