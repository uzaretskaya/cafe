package ru.uzaretskaya.cafe;

import java.util.List;

public class Cashier {

    public boolean orderIsReady(Order order) {
        List<Meal> mealsToPrepare = order.getMeals();
        try {
            Thread.sleep(1000L * mealsToPrepare.size());
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
}
