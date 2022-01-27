package ru.uzaretskaya.cafe.utils.statistic;

import ru.uzaretskaya.cafe.Cafe;
import ru.uzaretskaya.cafe.utils.FileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CashierStatisticManager extends StatisticManager {
    private final List<String> unsavedStatistic = new ArrayList<>();

    public CashierStatisticManager(Cafe cafe) {
        super(cafe);
        minutes = 1;
    }

    void saveStatistic() {
        List<String> statistics = cafe.getCashierStatistic();
        String filename = cafe.getCashierStatisticFilename();
        try {
            FileWriter.writeStringArrayToFile(statistics, filename);
        } catch (IOException e) {
            unsavedStatistic.addAll(statistics);
            e.printStackTrace();
        }
    }
}
