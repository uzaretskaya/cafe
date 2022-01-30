package ru.uzaretskaya.cafe;

import ru.uzaretskaya.cafe.utils.CafeProperties;
import ru.uzaretskaya.cafe.utils.CafeStatistic;
import ru.uzaretskaya.cafe.utils.statistic.CashierStatisticManager;
import ru.uzaretskaya.cafe.utils.statistic.MainManager;
import ru.uzaretskaya.cafe.utils.statistic.Manager;
import ru.uzaretskaya.cafe.utils.statistic.UserStatisticManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Cafe {

    private final List<Meal> menu = new ArrayList<>();

    private final Map<String, Cashier> cashiers = new HashMap<>();
    private final Map<String, User> users = new HashMap<>();
    private final List<Manager> managers = new ArrayList<>();

    private final CafeProperties properties;
    private final CafeStatistic cafeStatistic;

    private final Queue<Order> orderQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger countOrders = new AtomicInteger(0);

    private volatile boolean isCafeOpen = false;

    public Cafe() {
        fillMenu();

        createCashiers();
        createManagers();
        createUsers();

        properties = new CafeProperties();
        cafeStatistic = new CafeStatistic(properties);
    }

    private void createCashiers() {
        int countCashiers = properties.getCountCashiers();
        for (int i = 1; i <= countCashiers; i++) {
            Cashier cashier = new Cashier("Cashier " + i, this);
            cashiers.put(cashier.getId().toString(), cashier);
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
            User user = new User("Customer " + i, this);
            users.put(user.getId().toString(), user);
        }
    }

    public void open() {
        isCafeOpen = true;
        startThreads();
    }

    private void startThreads() {
        for (Map.Entry<String, Cashier> entry : cashiers.entrySet()) {
            Thread t = new Thread(entry.getValue());
            t.start();
        }

        for (Manager manager : managers) {
            Thread t = new Thread(manager);
            t.start();
        }

        for (Map.Entry<String, User> entry : users.entrySet()) {
            Thread t = new Thread(entry.getValue());
            t.start();
        }
    }

    public void close() {
        isCafeOpen = false;
    }

    public void createOrder(List<Meal> meals, User user) {
        int orderNumber = countOrders.addAndGet(1);
        Order order = new Order(meals, orderNumber, user);
        orderQueue.offer(order);
    }

    public Order getCurrentOrder() {
        return orderQueue.poll();
    }

    public void saveCompletedOrder(Order order) {
        cafeStatistic.saveCompletedOrder(order);
    }

    public String getFilenameForCashierStatistic() {
        return cafeStatistic.getFilenameForCashierStatistic();
    }

    public String getFilenameForUserStatistic() {
        return cafeStatistic.getFilenameForUserStatistic();
    }

    public List<String> getCashierStatistic() {
        return cafeStatistic.getCashierStatistic();
    }

    public List<String> getUserStatistic() {
        return cafeStatistic.getUserStatistic();
    }

    public Cashier getCashierById(String id) {
        return cashiers.get(id);
    }

    public User getUserById(String id) {
        return users.get(id);
    }

    private void fillMenu() {
        menu.add(new Meal("Айти-стейк", 500, 10));
        menu.add(new Meal("Легаси-салат", 50, 5));
        menu.add(new Meal("Свитч-картофель", 300, 3));
        menu.add(new Meal("Дебаг-кола", 25, 2));
        menu.add(new Meal("Скрипт-мороженое", 150, 4));
    }

    public boolean isCafeOpen() {
        return isCafeOpen;
    }

    public List<Meal> getMenu() {
        return new ArrayList<>(menu);
    }
}
