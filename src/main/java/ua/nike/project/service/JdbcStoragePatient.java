package ua.nike.project.service;

import ua.nike.project.struct.Patient;

import java.sql.*;
import java.util.LinkedList;

/**
 * This {@code JdbcStoragePatient} class is used for creating connection to the database "Patient".
 * The class {@code JdbcStoragePatient} includes methods for working with database "Patient"...
 */
public class JdbcStoragePatient {

    /** The value is used for the connection to database. */
    private Connection connect;



    /** Initializes a newly created {@code JdbcStoragePatient} object and automatically creating connection
     * to the database.
     */
    public JdbcStoragePatient() {
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
     * This method return from database instance of class {@code Patient} of the index.
     *
     * @param index   Index in the database.
     * @return  Return instance of class {@code Patient}.
     *          If generated exception, then return NULL.
     */
    public Patient getPatient(int index) {
        try (Statement statement = this.connect.createStatement();
             ResultSet result = statement.executeQuery("select * from patient where patient_id = " + index)) {
            result.next();
            int patient_id = result.getInt("patient_id");
            String surname = result.getString("surname");
            String firstName = result.getString("firstName");
            String secondName = result.getString("secondName");
            char sex = result.getString("sex").toCharArray()[0];
            String status = result.getString("status");
            int relative_id = result.getInt("relative_id");
            String telephone = result.getString("phone");
            return new Patient(patient_id, surname, firstName, secondName, sex, status, relative_id, telephone);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method return from database instance of class {@code Patient} of parameter and his value.
     *
     * @param parameter Parameter in the database.
     * @param value     Value of parameter.
     * @return Return instance of class {@code Patient}.
     *          If this method generated exception, then return NULL.
     */
    public Patient getPatient(String parameter, String value) {
        try (Statement statement = this.connect.createStatement();
             ResultSet result = statement.executeQuery("select * from patient where " + parameter + " = " + value)) {
            result.next();
            int patient_id = result.getInt("patient_id");
            String surname = result.getString("surname");
            String firstName = result.getString("firstName");
            String secondName = result.getString("secondName");
            char sex = result.getString("sex").toCharArray()[0];
            String status = result.getString("status");
            int relative_id = result.getInt("relative_id");
            String telephone = result.getString("phone");
            return new Patient(patient_id, surname, firstName, secondName, sex, status, relative_id, telephone);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method insert to database instance of class {@code Patient} of parameter and his value.
     *
     * @param patient Instance of class {@code Patient}.
     * @return - After insert to database, this method return patient_id from database.
     *          If this method generated exception, then return 0.
     */
    public int addPatient(Patient patient) {
        String surname = patient.getSurname();
        String firstName = patient.getFirstName();
        String secondName = patient.getSecondName();
        char sex = patient.getSex();
        String status = patient.getStatus();
        int relative_id = patient.getRelative_id();
        String telephone = patient.getTelephone();

        try (Statement statement = this.connect.createStatement()) {
            statement.executeUpdate(String.format("insert into patient (surname, firstName, secondName, sex, status, relative_id, phone) " +
                    "values ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", surname, firstName, secondName, sex, status, relative_id, telephone));
// замінити код на більш захищеніший !!!
//--------------------------------------------------------------------------
//
//            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
//                if (generatedKeys.next()){
//                    return generatedKeys.getInt(1);
//                }
//            }


        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }

    /**
     * Getting list of instance of class {@code Patient} from table patient in database.
     *
     * @return List of instance of class {@code Patient} from table patient in database.
     *         If this method generated exception, then return NULL.
     */
    public LinkedList<Patient> getPatients() {
        LinkedList<Patient> patients = new LinkedList<Patient>();
        try (Statement statement = this.connect.createStatement();
             ResultSet result = statement.executeQuery("select * from patient")) {
            while(result.next()) {
                int patient_id = result.getInt("patient_id");
                String surname = result.getString("surname");
                String firstName = result.getString("firstName");
                String secondName = result.getString("secondName");
                char sex = result.getString("sex").toCharArray()[0];
                String status = result.getString("status");
                int relative_id = result.getInt("relative_id");
                String telephone = result.getString("phone");

                patients.add(new Patient(patient_id, surname, firstName, secondName, sex, status, relative_id, telephone));
            }
            return patients;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Correct record in the database.
     *
     * @param index Index in the database.
     * @param patient Instance of class {@code Patient}
     * @return true, if patient in database is successfully update.
     *          If this method generated exception, then return false.
     */
    public boolean editPatient(int index, Patient patient) {
        String surname = patient.getSurname();
        String firstName = patient.getFirstName();
        String secondName = patient.getSecondName();
        char sex = patient.getSex();
        String status = patient.getStatus();
        int relative_id = patient.getRelative_id();
        String telephone = patient.getTelephone();

        try (Statement statement = this.connect.createStatement()) {
            statement.executeUpdate(String.format("update patient set " +
                            "surname = '%s', " +
                            "firstName = '%s', " +
                            "secondName = '%s', " +
                            "sex = '%s', " +
                            "status = '%s', " +
                            "relative_id = '%s', " +
                            "phone = '%s' " +
                            "where patient_id = '%s'",
                    surname, firstName, secondName, sex, status, relative_id, telephone, index ));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}