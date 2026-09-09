package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Reads values from config.properties at the project root.
 * System properties (Jenkins / Maven -D flags) always win over the file.
 *
 * Example: mvn test -Dbrowser=firefox -Dheadless=true
 */
public class ConfigReader {

    private static final Properties PROP = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            PROP.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("config.properties not found at project root", e);
        }
    }

    public static String getProperty(String key) {
        String fromSystem = System.getProperty(key);
        if (fromSystem != null && !fromSystem.isBlank()) {
            return fromSystem;
        }
        String fromFile = PROP.getProperty(key);
        if (fromFile == null) {
            throw new RuntimeException("Missing config key: " + key);
        }
        return fromFile.trim();
    }

    public static String getProperty(String key, String defaultValue) {
        try {
            String value = getProperty(key);
            return (value == null || value.isBlank()) ? defaultValue : value;
        } catch (RuntimeException e) {
            return defaultValue;
        }
    }

    public static int getInt(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(getProperty(key));
    }
}
