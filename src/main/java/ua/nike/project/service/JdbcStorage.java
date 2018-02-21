package ua.nike.project.service;

import ua.nike.project.struct.Human;

import java.sql.*;

/**
 * Клас в якому описані всі основні операції(методи) з базою даних :
 */
public class JdbcStorage {

    private Connection connect;  // ???????????

    /**
     * Конструктор створює конект до бази даних
     */
    public JdbcStorage() {
        final String URL = Settings.getSettings().value("jdbc.url");
        final String USERNAME = Settings.getSettings().value("jdbc.username");
        final String PASSWORD = Settings.getSettings().value("jdbc.password");
        try {
            Class.forName(Settings.getSettings().value("jdbc.driver_class"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            this.connect = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Human getHuman(int index) {
        Human human;
        try (Statement statement = this.connect.createStatement();
             ResultSet result = statement.executeQuery("select * from human where uid = " + index)) {
            result.next();
            int uid = result.getInt("uid");
            String surname = result.getString("surname");
            String firstName = result.getString("firstName");
            String secondName = result.getString("secondName");
            char sex = result.getObject("sex").toString().toCharArray()[0];
            String status = result.getString("status");
            String telephone = result.getString("phone");
            return human = new Human(uid, surname, firstName, secondName, sex, status, telephone);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
}
