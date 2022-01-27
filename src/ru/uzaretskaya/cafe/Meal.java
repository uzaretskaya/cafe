package ru.uzaretskaya.cafe;

public class Meal {
    private final String name;
    private final int calories;
    private final double cost;

    public Meal(String name, int calories, int cost) {
        this.name = name;
        this.calories = calories;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Meal {" +
                "name='" + name + '\'' +
                ", calories=" + calories +
                ", cost=" + cost +
                '}';
    }

    public double getCost() {
        return cost;
    }
}
