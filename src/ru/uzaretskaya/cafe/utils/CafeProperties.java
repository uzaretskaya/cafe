package ru.uzaretskaya.cafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CafeProperties {

    private static final String FILE_ENDING_CSV = ".csv";
    private final Properties property = new Properties();

    public CafeProperties() {
        try (FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + File.separator + "cafe.properties");) {
            property.load(fis);
        } catch (IOException e) {
            System.out.println("Couldn't read cafe properties.");
            e.printStackTrace();
        }
    }

    public int getCountCashiers() {
        String value = getProperty("countCashiers");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't read property countCashiers.");
        }
    }

    public int getCountCustomers() {
        String value = getProperty("countCustomers");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't read property countCustomers.");
        }
    }

    public int getMinutesForCashierStatisticManager() {
        String value = getProperty("cashierStatisticMinutes");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't read property cashierStatisticMinutes.");
        }
    }

    public int getMinutesForUserStatisticManager() {
        String value = getProperty("userStatisticMinutes");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't read property userStatisticMinutes.");
        }
    }

    public int getMinutesForMainManager() {
        String value = getProperty("mainStatisticMinutes");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RuntimeException("Couldn't read property mainStatisticMinutes.");
        }
    }

    public String getUserStatisticFilenameFromProperties() {
        String fileName = getProperty("userStatisticFilename");
        if (fileName == null) {
            throw new RuntimeException("Couldn't read property userStatisticFilename.");
        }
        return fileName + FILE_ENDING_CSV;
    }

    public String getCashierStatisticFilenameFromProperties() {
        String fileName = getProperty("cashierStatisticFilename");
        if (fileName == null) {
            throw new RuntimeException("Couldn't read property cashierStatisticFilename.");
        }
        return fileName + FILE_ENDING_CSV;
    }

    private String getProperty(String propertyName) {
        return property.getProperty(propertyName);
    }
}
