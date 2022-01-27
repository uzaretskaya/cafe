package ru.uzaretskaya.cafe.utils.statistic;

import ru.uzaretskaya.cafe.Cafe;

public abstract class StatisticManager implements Runnable{
    final Cafe cafe;
    int minutes;

    StatisticManager(Cafe cafe) {
        this.cafe = cafe;
    }

    public void run() {
        while (true) {
            if (!cafe.isCafeOpen()) return;
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

}
