package ru.uzaretskaya.cafe;

import ru.uzaretskaya.cafe.dto.Cashier;
import ru.uzaretskaya.cafe.dto.Meal;
import ru.uzaretskaya.cafe.dto.Order;
import ru.uzaretskaya.cafe.dto.User;
import ru.uzaretskaya.cafe.utils.CafeMenu;
import ru.uzaretskaya.cafe.utils.CafeProperties;
import ru.uzaretskaya.cafe.statistic.CafeStatistic;
import ru.uzaretskaya.cafe.dto.managers.CashierStatisticManager;
import ru.uzaretskaya.cafe.dto.managers.MainManager;
import ru.uzaretskaya.cafe.dto.managers.Manager;
import ru.uzaretskaya.cafe.dto.managers.UserStatisticManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Cafe {

    private final List<Meal> menu;

    private final Map<String, Cashier> cashiers = new HashMap<>();
    private final Map<String, User> users = new HashMap<>();
    private final List<Manager> managers = new ArrayList<>();

    private final CafeProperties properties;
    private final CafeStatistic cafeStatistic;

    private final Queue<Order> orderQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger countOrders = new AtomicInteger(0);

    private volatile boolean isOpen = false;

    public Cafe() {
        createCashiers();
        createManagers();
        createUsers();

        menu = CafeMenu.getMenu();
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
        isOpen = true;
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
        isOpen = false;
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
        return cafeStatistic.getCashierStatistic(cashiers);
    }

    public List<String> getUserStatistic() {
        return cafeStatistic.getUserStatistic(users);
    }

    public Cashier getCashierById(String id) {
        return cashiers.get(id);
    }

    public User getUserById(String id) {
        return users.get(id);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public List<Meal> getMenu() {
        return new ArrayList<>(menu);
    }
}
