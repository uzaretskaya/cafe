package ru.uzaretskaya.cafe.utils.statistic;

import ru.uzaretskaya.cafe.Cafe;
import ru.uzaretskaya.cafe.utils.FileReaderWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class StatisticManager implements Manager {
    private List<String> unsavedStatistic = new ArrayList<>();
    final Cafe cafe;
    int minutes;

    StatisticManager(Cafe cafe, int minutes) {
        this.cafe = cafe;
        this.minutes = minutes;
    }

    public void run() {
        while (true) {
            if (cafe.isCafeOpen()) return;
            sleepForMinutes(minutes);
            saveStatistic();
        }
    }

    abstract void saveStatistic();

    protected void saveToFile(String filename, List<String> statistics) {
        if (unsavedStatistic.size() > 0) {
            statistics.addAll(unsavedStatistic);
        }
        try {
            FileReaderWriter.writeStringArrayToFile(statistics, filename);
        } catch (IOException e) {
            unsavedStatistic = statistics;
            e.printStackTrace();
        }
    }
}
