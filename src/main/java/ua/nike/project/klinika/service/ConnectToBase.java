package ua.nike.project.klinika.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public final class ConnectToBase {

    private static Connection connection;

    /**
     * Private constructor. Nobody can create instance this class
     */
    private ConnectToBase() {
    }

    /**
     * @return connection to database
     */
    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        final String URL = Settings.getSettings().value("jdbc.url");
        final String USERNAME = Settings.getSettings().value("jdbc.username");
        final String PASSWORD = Settings.getSettings().value("jdbc.password");
        try {
            Class.forName(Settings.getSettings().value("jdbc.driver_class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return connection;

        } catch (SQLException e) {
            System.out.println("Wrong parameters to connection. Please, check your file 'my.properties' !");
            e.printStackTrace();
        }

        return connection;
    }

}
