package ua.nike.project;

import ua.nike.project.service.Settings;

import java.io.FileNotFoundException;
import java.sql.*;
import java.util.LinkedList;

public class TestMain {
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {
        final String URL = Settings.getSettings().value("jdbc.url");
        final String username = Settings.getSettings().value("jdbc.username");
        final String password = Settings.getSettings().value("jdbc.password");
        Class.forName((String) Settings.getSettings().value("jdbc.driver_class"));
        try (Connection connection = DriverManager.getConnection(URL, username, password);
             Statement statement = connection.createStatement()) {
            System.out.println("All right !");
            ResultSet resultSet = statement.executeQuery("select * from ?");

            // Як дізнатись скільки колонок в моєму resultSet.

            System.out.println(resultSet);

            while (resultSet.next()) {
                System.out.println(resultSet.getObject(1));
                System.out.println(resultSet.getObject(2));
                System.out.println(resultSet.getObject(3));
                System.out.println(resultSet.getObject(4));
                System.out.println(resultSet.getObject(5));
                System.out.println(resultSet.getObject(6));
                System.out.println(resultSet.getObject(7));
                System.out.println("__________");
            }
        } catch (SQLException e) {
            System.out.println("Wrong parameters to connection. Please, check your file 'my.properties' !");
            e.printStackTrace();
        }
    }
}
