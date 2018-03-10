package ua.nike.project.service;

import javax.validation.constraints.Null;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectToBase {


    private static Connection connect;
    /**
     * Private constructor. Nobody can create instance this class
     */
    private ConnectToBase() {
    }

    /**
     *
     *
     * @return connection to database
     */
    public static Connection getConnect() {

        final String URL = Settings.getSettings().value("jdbc.url");
        final String USERNAME = Settings.getSettings().value("jdbc.username");
        final String PASSWORD = Settings.getSettings().value("jdbc.password");
        try {
            Class.forName(Settings.getSettings().value("jdbc.driver_class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connect = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return connect;

        } catch (SQLException e) {
            System.out.println("Wrong parameters to connection. Please, check your file 'my.properties' !");
            e.printStackTrace();
        }

        return connect;
    }

}
