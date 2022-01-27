package ru.uzaretskaya.cafe.utils.statistic;

import ru.uzaretskaya.cafe.Cafe;

import java.util.List;

public class CashierStatisticManager extends StatisticManager {
    public CashierStatisticManager(Cafe cafe, int minutes) {
        super(cafe, minutes);
    }

    void saveStatistic() {
        List<String> statistics = cafe.getCashierStatistic();
        String filename = cafe.getCashierStatisticFilename();
        saveToFile(filename, statistics);
    }
}
