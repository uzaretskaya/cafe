package ru.uzaretskaya.cafe.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CafeProperties {
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
            return 3;
        }
    }

    public int getCountCustomers() {
        String value = getProperty("countCustomers");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 6;
        }
    }

    public int getMinutesForCashierStatisticManager() {
        String value = getProperty("cashierStatisticMinutes");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 1;
        }
    }

    public int getMinutesForUserStatisticManager() {
        String value = getProperty("userStatisticMinutes");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 2;
        }
    }

    public int getMinutesForMainManager() {
        String value = getProperty("mainStatisticMinutes");
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 2;
        }
    }

    public String getUserStatisticFilenameFromProperties() {
        String fileName = getProperty("userStatisticFilename");
        if (fileName == null) {
            fileName = "userStatistic";
        }
        return fileName + ".csv";
    }

    public String getCashierStatisticFilenameFromProperties() {
        String fileName = getProperty("cashierStatisticFilename");
        if (fileName == null) {
            fileName = "cashierStatistic";
        }
        return fileName + ".csv";
    }

    private String getProperty(String propertyName) {
        return property.getProperty(propertyName);
    }
}
