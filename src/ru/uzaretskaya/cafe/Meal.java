package ru.uzaretskaya.cafe;

public class Meal {
    private String name;
    private int calories;
    private int cost;

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
}
