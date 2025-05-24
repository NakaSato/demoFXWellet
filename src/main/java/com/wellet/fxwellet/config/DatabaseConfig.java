package com.wellet.fxwellet.config;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DatabaseConfig {
    private static final String CONFIG_FILE = "database.properties";
    private static final String DEFAULT_DB = "wellet.db";

    public static String getDatabasePath() {
        Properties props = new Properties();
        File configFile = new File(CONFIG_FILE);

        if (configFile.exists()) {
            try (FileInputStream fis = new FileInputStream(configFile)) {
                props.load(fis);
                String customPath = props.getProperty("database.path");
                if (customPath != null && !customPath.isEmpty()) {
                    return customPath;
                }
            } catch (IOException e) {
                System.err.println("Error reading config file: " + e.getMessage());
            }
        }

        String userHome = System.getProperty("user.home");
        String appDataDir = userHome + File.separator + ".wellet";

        String defaultPath = appDataDir + File.separator + DEFAULT_DB;

        saveDefaultConfig(defaultPath);

        return defaultPath;
    }

    private static void saveDefaultConfig(String dbPath) {
        Properties props = new Properties();
        props.setProperty("database.path", dbPath);
        props.setProperty("database.name", DEFAULT_DB);

        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            props.store(fos, "Wellet database configuration!");
        } catch (IOException e) {
            System.err.println("Error saving config file : " + e.getMessage());
        }
    }
}
