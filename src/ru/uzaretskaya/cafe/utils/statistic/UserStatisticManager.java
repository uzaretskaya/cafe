package ru.uzaretskaya.cafe.utils.statistic;

import ru.uzaretskaya.cafe.Cafe;

import java.util.List;

public class UserStatisticManager extends StatisticManager {

    public UserStatisticManager(Cafe cafe, int minutes) {
        super(cafe, minutes);
    }

    void saveStatistic() {
        List<String> statistics = cafe.getUserStatistic();
        String filename = cafe.getFilenameForUserStatistic();
        saveToFile(filename, statistics);
    }
}
