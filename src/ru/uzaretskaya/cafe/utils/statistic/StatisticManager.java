package ru.uzaretskaya.cafe.utils.statistic;

import ru.uzaretskaya.cafe.Cafe;
import ru.uzaretskaya.cafe.utils.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class StatisticManager implements Runnable{
    private List<String> unsavedStatistic = new ArrayList<>();
    final Cafe cafe;
    int minutes;

    StatisticManager(Cafe cafe, int minutes) {
        this.cafe = cafe;
        this.minutes = minutes;
    }

    public void run() {
        while (true) {
            if (cafe.isCafeClosed()) return;
            sleepForMinutes(minutes);
            saveStatistic();
        }
    }

    abstract void saveStatistic();

    private void sleepForMinutes(int min) {
        try {
            Thread.sleep(1000L * min * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void saveToFile(String filename, List<String> statistics) {
        if (unsavedStatistic.size() > 0) {
            statistics.addAll(unsavedStatistic);
        }
        try {
            FileWriter.writeStringArrayToFile(statistics, filename);
        } catch (IOException e) {
            unsavedStatistic = statistics;
            e.printStackTrace();
        }
    }
}
