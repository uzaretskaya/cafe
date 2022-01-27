package ru.uzaretskaya.cafe;

import ru.uzaretskaya.cafe.utils.CafeProperties;
import ru.uzaretskaya.cafe.utils.statistic.CashierStatisticManager;
import ru.uzaretskaya.cafe.utils.statistic.StatisticManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Cafe {
    private final List<Meal> availableMeals = new ArrayList<>();
    private final List<Cashier> cashiers = new ArrayList<>();
    private final List<StatisticManager> managers = new ArrayList<>();
    private final CafeProperties properties = new CafeProperties();
    private final ConcurrentLinkedQueue<Order> orderQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger numberOfOrder = new AtomicInteger(0);
    private boolean isCafeOpen = false;

    public Cafe() {
        fillMenu();
        createCashiers();
        createManagers();
    }

    public List<Meal> getMenu() {
        return new ArrayList<>(availableMeals);
    }

    public void createOrder(List<Meal> meals, Customer customer) {
        int orderNumber = numberOfOrder.addAndGet(1);
        Order order = new Order(meals, orderNumber, customer);
        orderQueue.offer(order);
    }

    public void open() {
        isCafeOpen = true;

        for (Cashier cashier : cashiers) {
            Thread t = new Thread(cashier);
            t.start();
        }

        for (StatisticManager manager : managers) {
            Thread t = new Thread(manager);
            t.start();
        }
    }

    public void close() {
        isCafeOpen = false;
    }

    public Order getCurrentOrder() {
        return orderQueue.poll();
    }

    public boolean isCafeOpen() {
        return isCafeOpen;
    }

    public List<String> getCashierStatistic() {
        List<String> statistics = new ArrayList<>(cashiers.size());
        for (Cashier cashier : cashiers) {
            var cashierStatistic = cashier.getStatistic();
            if (cashierStatistic.countOrders() > 0) {
                statistics.add(cashierStatistic.toString());
            }
        }
        return statistics;
    }

    public String getCashierStatisticFilename() {
        String fileName = properties.getProperty("cashierStatisticFilename");
        if (fileName == null) {
            fileName = "cashierStatistic";
        }
        return fileName + ".csv";
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
            cashiers.add(new Cashier("Cashier " + i, this));
        }
    }

    private void createManagers() {
        StatisticManager cashierManager = new CashierStatisticManager(this);
        managers.add(cashierManager);
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
