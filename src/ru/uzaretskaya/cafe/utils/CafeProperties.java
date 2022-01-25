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

    public String getProperty(String propertyName) {
        return property.getProperty(propertyName);
    }
}
