package ru.uzaretskaya.cafe.utils;

import ru.uzaretskaya.cafe.dto.Meal;

import java.util.ArrayList;
import java.util.List;

public class CafeMenu {

    private CafeMenu() {
    }

    public static List<Meal> getMenu() {
        List<Meal> menu = new ArrayList<>();
        menu.add(new Meal("Айти-стейк", 500, 10));
        menu.add(new Meal("Легаси-салат", 50, 5));
        menu.add(new Meal("Свитч-картофель", 300, 3));
        menu.add(new Meal("Дебаг-кола", 25, 2));
        menu.add(new Meal("Скрипт-мороженое", 150, 4));
        return menu;
    }
}
