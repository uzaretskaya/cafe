package ru.uzaretskaya.cafe.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.APPEND;

public class FileWriter {

    public static void writeStringArrayToFile(List<String> dataLines, String filename) throws IOException {
        Path file = Paths.get(filename);
        var lines = dataLines.stream()
                .map(FileWriter::convertToCSV)
                .collect(Collectors.toList());
        if (!Files.exists(file)) {
            Files.createFile(file);
        }
        Files.write(file, lines, StandardCharsets.UTF_8, APPEND);
    }

    private static String convertToCSV(String data) {
        return Stream.of(data)
                .map(FileWriter::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    private static String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }
}
