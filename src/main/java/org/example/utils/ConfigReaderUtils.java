package org.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration utility class to load and manage static data from config.properties
 */
public class ConfigReaderUtils {
    private static ConfigReaderUtils instance;
    private Properties properties;

    private ConfigReaderUtils() {
        properties = new Properties();
        loadProperties();
    }

    /**
     * Get singleton instance of ConfigReaderUtils
     */
    public static ConfigReaderUtils getInstance() {
        if (instance == null) {
            synchronized (ConfigReaderUtils.class) {
                if (instance == null) {
                    instance = new ConfigReaderUtils();
                }
            }
        }
        return instance;
    }

    /**
     * Load properties from config.properties file
     */
    private void loadProperties() {
        try (InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new RuntimeException("config.properties file not found in classpath");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config.properties file", e);
        }
    }

    /**
     * Get property value by key
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Get property value by key with default value
     */
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get property as integer
     */
    public int getIntProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in config");
        }
        return Integer.parseInt(value);
    }

    /**
     * Get property as integer with default value
     */
    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    /**
     * Get property as array (comma-separated values)
     */
    public String[] getArrayProperty(String key) {
        String value = getProperty(key);
        if (value == null) {
            return new String[0];
        }
        return value.split(",");
    }

    // Convenience methods for common properties

    // URLs
    public String getGoogleUrl() {
        return getProperty("google.url");
    }

    public String getAmazonUrl() {
        return getProperty("amazon.url");
    }

    // Google Expected Title
    public String getGoogleExpectedTitle() {
        return getProperty("google.expected.title");
    }

    // Timeouts
    public int getShortTimeout() {
        return getIntProperty("timeout.short");
    }

    public int getMediumTimeout() {
        return getIntProperty("timeout.medium");
    }

    public int getLongTimeout() {
        return getIntProperty("timeout.long");
    }

    public int getPageLoadTimeout() {
        return getIntProperty("timeout.page.load");
    }

    // Test Data
    public String getMotorolaSearchQuery() {
        return getProperty("test.search.query.motorola");
    }

    public String getSamsungSearchQuery() {
        return getProperty("test.search.query.samsung");
    }
}

