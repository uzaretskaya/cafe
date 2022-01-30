package ru.uzaretskaya.cafe;

import ru.uzaretskaya.cafe.utils.CafeProperties;
import ru.uzaretskaya.cafe.utils.Pair;
import ru.uzaretskaya.cafe.utils.statistic.CashierStatisticManager;
import ru.uzaretskaya.cafe.utils.statistic.MainManager;
import ru.uzaretskaya.cafe.utils.statistic.Manager;
import ru.uzaretskaya.cafe.utils.statistic.UserStatisticManager;
import ru.uzaretskaya.cafe.utils.statistic.dto.UserStatistic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.uzaretskaya.cafe.utils.FileReaderWriter.deleteFileIfExists;

public class Cafe {
    private final List<Meal> availableMeals = new ArrayList<>();
    private final List<Cashier> cashiers = new ArrayList<>();
    private final List<Manager> managers = new ArrayList<>();
    private final List<User> users = new ArrayList<>();
    private final CafeProperties properties = new CafeProperties();

    private final Queue<Order> orderQueue = new ConcurrentLinkedQueue<>();
    private final AtomicInteger countOrders = new AtomicInteger(0);
    private boolean isCafeOpen = false;
    private String filenameForCashierStatistic;
    private String filenameForUserStatistic;
    private final Map<UUID, UserStatistic> userStatistic = new HashMap<>();

    public Cafe() {
        fillMenu();
        createPersonalAndCustomers();
        prepareFilesForStatistic();
    }

    public List<Meal> getMenu() {
        return new ArrayList<>(availableMeals);
    }

    public void open() {
        isCafeOpen = true;
        startThreads();
    }

    public void close() {
        isCafeOpen = false;
    }

    public void createOrder(List<Meal> meals, User customer) {
        int orderNumber = countOrders.addAndGet(1);
        Order order = new Order(meals, orderNumber);
        orderQueue.offer(order);
        saveUserStatistic(customer, order);
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
            statistic.add(entry.getKey() + "," + entry.getValue().getUserStatisticInfo());
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

    private void prepareFilesForStatistic() {
        filenameForCashierStatistic = properties.getCashierStatisticFilenameFromProperties();
        filenameForUserStatistic = properties.getUserStatisticFilenameFromProperties();

        try {
            deleteFileIfExists(filenameForCashierStatistic);
            deleteFileIfExists(filenameForUserStatistic);
        } catch (IOException e) {
            System.out.println("New statistic will be added in existing files.");
            e.printStackTrace();
        }
    }

    private void createPersonalAndCustomers() {
        createCashiers();
        createManagers();
        createUsers();
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

    private void startThreads() {
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

    private void saveUserStatistic(User customer, Order order) {
        Pair<Integer, Double> orderInfo = order.getOrderCaloriesAndSum();
        UserStatistic savedUser = userStatistic.get(customer.getId());
        if (savedUser == null) {
            userStatistic.put(customer.getId(), new UserStatistic(1, orderInfo.getX(), orderInfo.getY()));
        } else {
            savedUser.updateAddingValues(1, orderInfo.getX(), orderInfo.getY());
        }
    }
}
