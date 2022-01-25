package ru.uzaretskaya.cafe;

import java.util.List;
import java.util.UUID;

public class Cashier implements Runnable {
    private final UUID id;
    private final String name;
    private final Cafe cafe;

    public Cashier(String name, Cafe cafe) {
        this.cafe = cafe;
        this.id = UUID.randomUUID();
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void run() {
        while (true) {
            if (!cafe.isCafeOpen()) return;

            Order currentOrder = cafe.getCurrentOrder();
            if (currentOrder != null) {
                List<Meal> mealsToPrepare = currentOrder.getMeals();
                try {
                    System.out.println(this + " started cook " + currentOrder);
                    Thread.sleep(1000L * mealsToPrepare.size());
                    currentOrder.setReady(true);
                    System.out.println(this + " finished " + currentOrder);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Cashier {" +
                "name='" + name + '\'' +
                '}';
    }
}
