package ua.nike.project.service;

import ua.nike.project.struct.Human;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Клас в якому описані всі основні операції(методи) з базою даних :
 */
public class JdbcStorage {

    private Connection connect;  // ???????????
    private LinkedList<Human> humans;

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
            System.out.println("Wrong parameters to connection. Please, check your file 'my.properties' !");
            e.printStackTrace();
        }
    }

    /**
     * Метод повертає із SQL бази екземпляр класу Human по вказазаному індексу
     *
     * @param index - це індекс, по якому буде здійснюватись пошук по базі відповідного значення
     * @return повертає екземпляр класу Human.
     * Якщо запис не знайдено, то викидає SQLException та передає значення null.
     */
    public Human getHuman(int index) {
        try (Statement statement = this.connect.createStatement();
             ResultSet result = statement.executeQuery("select * from human where uid = " + index)) {
            result.next();
            int uid = result.getInt("uid");
            String surname = result.getString("surname");
            String firstName = result.getString("firstName");
            String secondName = result.getString("secondName");
            char sex = result.getString("sex").toCharArray()[0];
            String status = result.getString("status");
            String telephone = result.getString("phone");
            return new Human(uid, surname, firstName, secondName, sex, status, telephone);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Метод повертає із SQL бази екземпляр класу Human по вказазаному параметру та його значенню
     *
     * @param parameter - це параметер, по якому буде здійснюватись пошук по базі відповідного значення
     * @param value     - це значення параметру, по якому здійснюється пошук по базі
     * @return повертає перший знайдений екземпляр класу Human.
     * Якщо не знайдено жодного запису, то викидає SQLException та передає значення null.
     */
    public Human getHuman(String parameter, String value) {
        try (Statement statement = this.connect.createStatement();
             ResultSet result = statement.executeQuery("select * from human where " + parameter + " = " + value)) {
            result.next();
            int uid = result.getInt("uid");
            String surname = result.getString("surname");
            String firstName = result.getString("firstName");
            String secondName = result.getString("secondName");
            char sex = result.getString("sex").toCharArray()[0];
            String status = result.getString("status");
            String telephone = result.getString("phone");
            return new Human(uid, surname, firstName, secondName, sex, status, telephone);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param human - передається екземпляр класу Human
     * @return - повертається true, якщо запис в базу відбувся без Exeptions,
     * повертає false, якщо запис не було додано !
     */
    public int addHuman(Human human) {
        String surname = human.getSurname();
        String firstName = human.getFirstName();
        String secondName = human.getSecondName();
        char sex = human.getSex();
        String status = human.getStatus();
        String telephone = human.getTelephone();

        try (Statement statement = this.connect.createStatement()) {
            statement.executeUpdate(String.format("insert into human (surname, firstName, secondName, sex, status, phone) " +
                    "values ('%s', '%s', '%s', '%s', '%s', '%s')", surname, firstName, secondName, sex, status, telephone));
// замінити код на більш захищеніший !!!
//--------------------------------------------------------------------------

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()){
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public LinkedList<Human> getHumans() {
        try (Statement statement = this.connect.createStatement();
             ResultSet result = statement.executeQuery("select * from human")) {
            while(result.next()) {
                int uid = result.getInt("uid");
                String surname = result.getString("surname");
                String firstName = result.getString("firstName");
                String secondName = result.getString("secondName");
                char sex = result.getString("sex").toCharArray()[0];
                String status = result.getString("status");
                String telephone = result.getString("phone");

                humans.add(new Human(uid, surname, firstName, secondName, sex, status, telephone));
            }
            return humans;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}