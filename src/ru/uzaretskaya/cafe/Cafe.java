package ru.uzaretskaya.cafe;

import ru.uzaretskaya.cafe.utils.CafeProperties;

import java.util.ArrayList;
import java.util.List;

public class Cafe {
    private final List<Meal> availableMeals = new ArrayList<>();
    private final List<Cashier> cashiers = new ArrayList<>();
    private final CafeProperties properties = new CafeProperties();

    public Cafe() {
        fillMenu();
        createCashiers();
    }

    public List<Meal> getMenu() {
        return new ArrayList<>(availableMeals);
    }

    private void fillMenu() {
        availableMeals.add(new Meal("Айти-стейк", 500, 10));
        availableMeals.add(new Meal("Легаси-салат", 50, 5));
        availableMeals.add(new Meal("Свитч-картофель", 300, 3));
        availableMeals.add(new Meal("Дебаг-кола", 25, 2));
        availableMeals.add(new Meal("Скрипт-мороженое", 150, 4));
    }

    private void createCashiers() {
        int countCashiers = getCountCashiers();
        for (int i = 0; i < countCashiers; i++) {
            cashiers.add(new Cashier());
        }
    }

    private int getCountCashiers() {
        String value = properties.getProperty("countCashiers");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 3;
        }
    }
}
