package ru.uzaretskaya.cafe.dto.managers;

import ru.uzaretskaya.cafe.Cafe;
import ru.uzaretskaya.cafe.utils.FileReaderWriter;

import java.io.IOException;
import java.util.List;

public record UserStatisticManager(Cafe cafe, int minutes) implements Manager {

    @Override
    public void run() {
        while (cafe.isOpen()) {
            sleepForMinutes(minutes);
            saveStatistic();
        }
    }

    private void saveStatistic() {
        List<String> statistics = cafe.getUserStatistic();
        String filename = cafe.getFilenameForUserStatistic();
        saveToFile(filename, statistics);
    }

    private void saveToFile(String filename, List<String> statistics) {
        try {
            FileReaderWriter.writeStringArrayToFile(statistics, filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
