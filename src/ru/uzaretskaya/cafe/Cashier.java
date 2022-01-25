package ru.uzaretskaya.cafe;

import java.util.List;
import java.util.UUID;

public class Cashier {
    private final UUID id;
    private final String name;

    public Cashier(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public boolean cookOrder(Order order) {
        List<Meal> mealsToPrepare = order.getMeals();
        try {
            System.out.println(this + " started cook " + order);
            Thread.sleep(1000L * mealsToPrepare.size());
            order.setReady(true);
            System.out.println(this + " finished " + order);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Cashier {" +
                "name='" + name + '\'' +
                '}';
    }
}
