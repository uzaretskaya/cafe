package ru.uzaretskaya.cafe.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;

public class FileReaderWriter {

    public static void writeStringArrayToFile(List<String> dataLines, String filename) throws IOException {
        Path file = Paths.get(filename);
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
        Files.write(file, dataLines, StandardCharsets.UTF_8, APPEND);
    }

    public static List<String[]> readFile(String filename) {
        try {
            return Files.lines(Paths.get(filename))
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
