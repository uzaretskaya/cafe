package ru.uzaretskaya.cafe;

import java.util.List;

public class Customer {

    public Order createOrder(List<Meal> menu) {
        Order order = new Order();
        int countMeals = getRandomNumber(1, 3);
        for (int i = 0; i < countMeals; i++) {
            int mealIndex = getRandomNumber(0, menu.size() - 1);
            order.addMeal(menu.get(mealIndex));
        }
        return order;
    }

    private int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
