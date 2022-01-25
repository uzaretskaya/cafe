package ru.uzaretskaya.cafe;

import ru.uzaretskaya.cafe.utils.CafeProperties;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Cafe {
    private final List<Meal> availableMeals = new ArrayList<>();
    private final List<Cashier> cashiers = new ArrayList<>();
    private final CafeProperties properties = new CafeProperties();
    private int numberOfOrder = 0;
    private Queue<Order> orderQueue = new LinkedList<>();
    private boolean isCafeOpen = false;

    public Cafe() {
        fillMenu();
        createCashiers();
    }

    public List<Meal> getMenu() {
        return new ArrayList<>(availableMeals);
    }

    public void createOrder(List<Meal> meals, Customer customer) {
        numberOfOrder++;
        Order order = new Order(meals, numberOfOrder, customer);
        orderQueue.offer(order);
    }

    public void open() {
        isCafeOpen = true;
        cookOrders();
    }

    public void close() {
        isCafeOpen = false;
        stopCooking();
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
        for (int i = 1; i <= countCashiers; i++) {
            cashiers.add(new Cashier("Cashier " + i));
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

    private void cookOrders() {
        //while (isCafeOpen) {
        for (Cashier cashier : cashiers) {
            Order order = orderQueue.peek();
            if (order != null) {
                if (cashier.cookOrder(order)) {
                    orderQueue.poll();
                }
            }
        }
        //}
    }

    private void stopCooking() {

    }
}
