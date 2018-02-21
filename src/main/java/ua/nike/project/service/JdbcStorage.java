package ua.nike.project.service;

import java.sql.*;

/**
 * Клас в якому описані всі основні операції(методи) з базою даних :
 *
 */
public class JdbcStorage {

    private final Connection CONNECT;
    /**
     * Конструктор створює конект до бази даних
     */
    public JdbcStorage(){
        final String URL = Settings.getSettings().value("jdbc.url");
        final String USERNAME = Settings.getSettings().value("jdbc.username");
        final String PASSWORD = Settings.getSettings().value("jdbc.password");
        try {
            Class.forName(Settings.getSettings().value("jdbc.driver_class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            this.CONNECT = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public String getHumanName (int index){
        try (Statement statement = this.CONNECT.createStatement();
             ResultSet result = statement.executeQuery("select surname from human where uid = 1 ")){

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
