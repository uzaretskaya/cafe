package ru.uzaretskaya.cafe.dto.managers;

import ru.uzaretskaya.cafe.Cafe;
import ru.uzaretskaya.cafe.dto.Cashier;
import ru.uzaretskaya.cafe.dto.User;
import ru.uzaretskaya.cafe.utils.FileReaderWriter;
import ru.uzaretskaya.cafe.statistic.CashierStatisticCalculated;
import ru.uzaretskaya.cafe.statistic.UserStatisticCalculated;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainManager implements Manager {
    private final Cafe cafe;
    private final int minutes;
    private final DecimalFormat df = new DecimalFormat("#.##");

    public MainManager(Cafe cafe, int minutes) {
        this.cafe = cafe;
        this.minutes = minutes;
    }

    @Override
    public void run() {
        while (cafe.isOpen()) {
            sleepForMinutes(minutes);
            readStatistic();
        }
    }

    public void readStatistic() {
        var bestCashier = getTheBestCashier();
        if (bestCashier != null) {
            Cashier cashier = cafe.getCashierById(bestCashier.cashierId());
            System.out.println("The best cashier is " + cashier + " with " + df.format(bestCashier.averageCheck()) + "$ average check!");
        }

        var bestUser = getTheHungriestUser();
        if (bestUser != null) {
            User user = cafe.getUserById(bestUser.userId());
            System.out.println("The hungriest user is " + user + " with " + df.format(bestUser.sumCalories()) + " calories!");

        }
    }

    private CashierStatisticCalculated getTheBestCashier() {
        List<String[]> lines = FileReaderWriter.readFile(cafe.getFilenameForCashierStatistic());

        List<CashierStatisticCalculated> statistics = new ArrayList<>();
        for (String[] s : lines) {
            int countOrders = Integer.parseInt(s[1]);
            double sumOrders = Double.parseDouble(s[2]);
            double averageCheck = sumOrders / countOrders;

            statistics.add(new CashierStatisticCalculated(s[0], countOrders, sumOrders, averageCheck));
        }

        return statistics.stream()
                .max(Comparator.comparing(CashierStatisticCalculated::averageCheck)).orElse(null);
    }

    private UserStatisticCalculated getTheHungriestUser() {
        List<String[]> lines = FileReaderWriter.readFile(cafe.getFilenameForUserStatistic());

        List<UserStatisticCalculated> statistics = new ArrayList<>();
        for (String[] s : lines) {
            int countOrders = Integer.parseInt(s[1]);
            double averageCalories = Double.parseDouble(s[2]);
            double averageSum = Double.parseDouble(s[3]);
            double sumCalories = averageCalories * countOrders;

            statistics.add(new UserStatisticCalculated(s[0], countOrders, averageCalories, averageSum, sumCalories));
        }

        return statistics.stream()
                .max(Comparator.comparing(UserStatisticCalculated::sumCalories)).orElse(null);
    }
}
