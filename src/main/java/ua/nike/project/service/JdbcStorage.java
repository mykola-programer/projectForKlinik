package ua.nike.project.service;

import ua.nike.project.struct.Patient;

import java.sql.*;
import java.util.LinkedList;

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
            System.out.println("Wrong parameters to connection. Please, check your file 'my.properties' !");
            e.printStackTrace();
        }
    }

    /**
     * Метод повертає із SQL бази екземпляр класу Patient по вказазаному індексу
     *
     * @param index - це індекс, по якому буде здійснюватись пошук по базі відповідного значення
     * @return повертає екземпляр класу Patient.
     * Якщо запис не знайдено, то викидає SQLException та передає значення null.
     */
    public Patient getHuman(int index) {
        try (Statement statement = this.connect.createStatement();
             ResultSet result = statement.executeQuery("select * from human where uid = " + index)) {
            result.next();
            int uid = result.getInt("human_id");
            String surname = result.getString("surname");
            String firstName = result.getString("firstName");
            String secondName = result.getString("secondName");
            char sex = result.getString("sex").toCharArray()[0];
            String status = result.getString("status");
            String telephone = result.getString("phone");
            return new Patient(uid, surname, firstName, secondName, sex, status, telephone);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Метод повертає із SQL бази екземпляр класу Patient по вказазаному параметру та його значенню
     *
     * @param parameter - це параметер, по якому буде здійснюватись пошук по базі відповідного значення
     * @param value     - це значення параметру, по якому здійснюється пошук по базі
     * @return повертає перший знайдений екземпляр класу Patient.
     * Якщо не знайдено жодного запису, то викидає SQLException та передає значення null.
     */
    public Patient getHuman(String parameter, String value) {
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
            return new Patient(uid, surname, firstName, secondName, sex, status, telephone);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param patient - передається екземпляр класу Patient
     * @return - повертається true, якщо запис в базу відбувся без Exeptions,
     * повертає false, якщо запис не було додано !
     */
    public int addHuman(Patient patient) {
        String surname = patient.getSurname();
        String firstName = patient.getFirstName();
        String secondName = patient.getSecondName();
        char sex = patient.getSex();
        String status = patient.getStatus();
        String telephone = patient.getTelephone();

        try (Statement statement = this.connect.createStatement()) {
            statement.executeUpdate(String.format("insert into patient (surname, firstName, secondName, sex, status, phone) " +
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

    /**
     *
     * @return - повертає список всіх записів які зберігаються в таблиці Patient (SQL-base)
     */
    public LinkedList<Patient> getHumans() {
        LinkedList<Patient> patients = new LinkedList<Patient>();
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

                patients.add(new Patient(uid, surname, firstName, secondName, sex, status, telephone));
            }
            return patients;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editHuman(int index, Patient patient) {
        String surname = patient.getSurname();
        String firstName = patient.getFirstName();
        String secondName = patient.getSecondName();
        char sex = patient.getSex();
        String status = patient.getStatus();
        String telephone = patient.getTelephone();
// Patient(null, "Шпак", "", "", 'Ж', "супроводжуючий", "+380637943767")
//
// update patient set surname = 'Шпак', firstName = '', secondName='', sex='Ж', status = 'супроводжуючий', phone = '+380637943767' where uid = 7
//
        try (Statement statement = this.connect.createStatement()) {
            statement.executeUpdate(String.format("update patient set " +
                            "surname = '%s', " +
                            "firstName = '%s', " +
                            "secondName = '%s', " +
                            "sex = '%s', " +
                            "status = '%s', " +
                            "phone = '%s' " +
                            "where uid = '%s'",
                    surname, firstName, secondName, sex, status, telephone, index ));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}