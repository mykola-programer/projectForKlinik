package ua.nike.project.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Клас в якому будуть зберігатись всі базові налаштування
 */
public class Settings {
    private static final Settings SETTINGS = new Settings();

    private Properties properties = new Properties();

    /**
     * Приватний конструктор. Для того щоб не можна було створити екземпляр класу із зовні. Його можна отримати
     *      із гетера.
     */
    private Settings() {
        try {
            properties.load(new FileInputStream(this.getClass().getClassLoader().getResource("my.properties").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Settings getSettings() {
        return SETTINGS;
    }

    /**
     *
     * @param key - ключ по якому буде йти пошук відповідної настройки
     * @return - метод повертає значення настройки по відповідному ключу.
     */
    public String value(String key) {
        return this.properties.getProperty(key);
    }
}
