package ru.uzaretskaya.cafe;

import ru.uzaretskaya.cafe.utils.CafeProperties;
import ru.uzaretskaya.cafe.utils.statistic.*;
import ru.uzaretskaya.cafe.utils.statistic.dto.UserStatistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Cafe {
    private final List<Meal> availableMeals = new ArrayList<>();
    private final List<Cashier> cashiers = new ArrayList<>();
    private final List<Manager> managers = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final Map<UUID, UserStatistic> userStatistic = new HashMap<>();
    private final String filenameForCashierStatistic;
    private final String filenameForUserStatistic;
    private final CafeProperties properties = new CafeProperties();
    private final ConcurrentLinkedQueue<Order> orderQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger numberOfOrder = new AtomicInteger(0);
    private boolean isCafeOpen = false;

    public Cafe() {
        fillMenu();
        createCashiers();
        createManagers();
        createUsers();
        filenameForCashierStatistic = properties.getCashierStatisticFilenameFromProperties();
        filenameForUserStatistic = properties.getUserStatisticFilenameFromProperties();
    }

    public List<Meal> getMenu() {
        return new ArrayList<>(availableMeals);
    }

    public void open() {
        isCafeOpen = true;

        for (Cashier cashier : cashiers) {
            Thread t = new Thread(cashier);
            t.start();
        }

        for (Manager manager : managers) {
            Thread t = new Thread(manager);
            t.start();
        }

        for (User user : users) {
            Thread t = new Thread(user);
            t.start();
        }
    }

    public void close() {
        isCafeOpen = false;
    }

    public void createOrder(List<Meal> meals, User customer) {
        int orderNumber = numberOfOrder.addAndGet(1);
        Order order = new Order(meals, orderNumber, customer);
        orderQueue.offer(order);
        saveUserStatistic(customer, meals);
    }

    public Order getCurrentOrder() {
        return orderQueue.poll();
    }

    public boolean isCafeClosed() {
        return !isCafeOpen;
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

    public List<String> getUserStatistic() {
        List<String> statistic = new ArrayList<>(userStatistic.size());
        for (Map.Entry<UUID, UserStatistic> entry : userStatistic.entrySet()) {
            statistic.add(entry.getKey() + "," + entry.getValue());
        }
        userStatistic.clear();
        return statistic;
    }

    public String getFilenameForCashierStatistic() {
        return filenameForCashierStatistic;
    }

    public String getFilenameForUserStatistic() {
        return filenameForUserStatistic;
    }

    public List<Cashier> getCashiers() {
        return cashiers;
    }

    public List<User> getUsers() {
        return users;
    }

    private void fillMenu() {
        availableMeals.add(new Meal("Айти-стейк", 500, 10));
        availableMeals.add(new Meal("Легаси-салат", 50, 5));
        availableMeals.add(new Meal("Свитч-картофель", 300, 3));
        availableMeals.add(new Meal("Дебаг-кола", 25, 2));
        availableMeals.add(new Meal("Скрипт-мороженое", 150, 4));
    }

    private void createCashiers() {
        int countCashiers = properties.getCountCashiers();
        for (int i = 1; i <= countCashiers; i++) {
            cashiers.add(new Cashier("Cashier " + i, this));
        }
    }

    private void createManagers() {
        Manager cashierManager = new CashierStatisticManager(this, properties.getMinutesForCashierStatisticManager());
        managers.add(cashierManager);

        Manager userManager = new UserStatisticManager(this, properties.getMinutesForUserStatisticManager());
        managers.add(userManager);

        Manager mainManager = new MainManager(this, properties.getMinutesForMainManager());
        managers.add(mainManager);
    }

    private void createUsers() {
        int countCustomers = properties.getCountCustomers();
        for (int i = 1; i <= countCustomers; i++) {
            users.add(new User("Customer " + i, this));
        }
    }

    private void saveUserStatistic(User customer, List<Meal> meals) {
        int countMeals = meals.size();
        double sumCost = 0;
        int sumCalories = 0;
        for (Meal meal : meals) {
            sumCalories += meal.getCalories();
            sumCost += meal.getCost();
        }
        double averageCalories = sumCalories * 1.0 / countMeals;
        double averageSum = sumCost / countMeals;

        UserStatistic current = userStatistic.get(customer.getId());
        if (current == null) {
            userStatistic.put(customer.getId(), new UserStatistic(1, averageCalories, averageSum));
        } else {
            userStatistic.put(customer.getId(),
                    new UserStatistic(1 + current.countOrders(),
                            averageCalories + current.averageCalories(),
                            averageSum + current.averageOrderSum()));
        }
    }
}
