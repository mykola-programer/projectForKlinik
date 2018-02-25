package ua.nike.project.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Class {@code Setting} have basic properties for connect.
 */
public class Settings {
    private static final Settings SETTINGS = new Settings();

    private Properties properties = new Properties();

    /**
     * Private constructor. Initializes a newly created {@code Setting} object from getter 'getSettings()'.
     */
    private Settings() {
        try {
            properties.load(new FileInputStream(this.getClass().getClassLoader().getResource("my.properties").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return instance of Settings.
     */
    public static Settings getSettings() {
        return SETTINGS;
    }

    /**
     *
     * @param key Key in our properties.
     * @return {@code String} value from properties relative for key.
     */
    public String value(String key) {
        return this.properties.getProperty(key);
    }
}
