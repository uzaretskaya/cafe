package ru.uzaretskaya.cafe.dto.managers;

public interface Manager extends Runnable {

    default void sleepForMinutes(int min) {
        try {
            Thread.sleep(1000L * min * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
