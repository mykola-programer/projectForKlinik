package ua.nike.project.klinika.jdbc;

import ua.nike.project.klinika.service.ConnectToBase;

import java.sql.*;
import java.util.ArrayList;

/**
 * This {@code JdbcStoragePatient} class is used for creating connection to the database "projectForClinic".
 * The class {@code JdbcStoragePatient} includes methods for working with table "Patient"...
 */
public class JdbcStoragePatient {

    /**
     * The value is used for the connection to database.
     */
    private final Connection CONNECT;

    /**
     * Initializes a newly created {@code JdbcStoragePatient} object and automatically creating connection
     * to the database.
     */
    public JdbcStoragePatient() {
        CONNECT = ConnectToBase.getConnection();
    }

    /**
     * This method return record from database, instance of class {@code Patient} of the index.
     *
     * @param index Index in the database.
     * @return Return instance of class {@code Patient}.
     * If generated exception, then return NULL.
     */
    public Patient getPatient(int index) {
        try (Statement statement = this.CONNECT.createStatement();
             ResultSet result = statement.executeQuery("select * from patient where patient_id = " + index)) {
            result.next();
            Integer patient_id = result.getInt("patient_id");
            String surname = result.getString("surname");
            String firstName = result.getString("firstName");
            String secondName = result.getString("secondName");
            Character sex = result.getString("sex").toCharArray()[0];
            String status = result.getString("status");
            Integer relative_id = result.getInt("relative_id");
            String telephone = result.getString("telephone");
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
     * If this method generated exception, then return NULL.
     */
    public Patient getPatient(String parameter, String value) {
        try (Statement statement = this.CONNECT.createStatement();
             ResultSet result = statement.executeQuery("select * from patient where " + parameter + " = " + value)) {
            result.next();
            Integer patient_id = result.getInt("patient_id");
            String surname = result.getString("surname");
            String firstName = result.getString("firstName");
            String secondName = result.getString("secondName");
            Character sex = result.getString("sex").toCharArray()[0];
            String status = result.getString("status");
            Integer relative_id = result.getInt("relative_id");
            String telephone = result.getString("telephone");
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
     * If this method generated exception, then return 0.
     */
    public int addPatient(Patient patient) {
        String surname = patient.getSurname();
        String firstName = patient.getFirstName();
        String secondName = patient.getSecondName();
        Character sex = patient.getSex();
        String status = patient.getStatus();
        Integer relative_id = patient.getRelative_id();
        String telephone = patient.getTelephone();


/**   ----------------------------------------
 * Statement
 */
//        try (Statement statement = this.CONNECT.createStatement()) {
//            statement.executeUpdate(String.format("insert into patient (surname, firstName, secondName, sex, status, relative_id, telephone) " +
//                    "values ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", surname, firstName, secondName, sex, status, relative_id, telephone));
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

/**   ---------------------------------------
 * PreparedStatement
 */
//        String sql = "SELECT add_patient(?,?,?,'Ж',?,?,?);";
        String sql = "INSERT INTO patient (surname, firstName, secondName, sex, status, relative_id, telephone) VALUES (?,?,?,?,?,?,?);";
        try (final PreparedStatement preparedStatement = this.CONNECT.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            preparedStatement.setString(1, surname);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, secondName);
            preparedStatement.setString(4, (sex.toString()));
            preparedStatement.setString(5, status);
            preparedStatement.setInt(6, relative_id);
            preparedStatement.setString(7, telephone);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKey = preparedStatement.getGeneratedKeys()) {
                if (generatedKey.next()) {
                    return generatedKey.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * Getting list of instance of class {@code Patient} from table patient in database.
     *
     * @return List of instance of class {@code Patient} from table patient in database.
     * If this method generated exception, then return NULL.
     */
    public ArrayList<Patient> getPatients() {
        ArrayList<Patient> patients = new ArrayList<Patient>();
        try (Statement statement = this.CONNECT.createStatement();
             ResultSet result = statement.executeQuery("select * from patient")) {
            while (result.next()) {
                Integer patient_id = result.getInt("patient_id");
                String surname = result.getString("surname");
                String firstName = result.getString("firstName");
                String secondName = result.getString("secondName");
                Character sex = result.getString("sex").toCharArray()[0];
                String status = result.getString("status");
                Integer relative_id = result.getInt("relative_id");
                String telephone = result.getString("telephone");

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
     * @param index   Index in the database.
     * @param patient Instance of class {@code Patient}
     * @return true, if patient in database is successfully update.
     * If this method generated exception, then return false.
     */
    public boolean editPatient(int index, Patient patient) {
        String surname = patient.getSurname();
        String firstName = patient.getFirstName();
        String secondName = patient.getSecondName();
        Character sex = patient.getSex();
        String status = patient.getStatus();
        Integer relative_id = patient.getRelative_id();
        String telephone = patient.getTelephone();

        try (Statement statement = this.CONNECT.createStatement()) {
            statement.executeUpdate(String.format("update patient set " +
                            "surname = '%s', " +
                            "firstname = '%s', " +
                            "secondname = '%s', " +
                            "sex = '%s', " +
                            "status = '%s', " +
                            "telephone = '%s', " +
                            "relative_id = %s " +
                            "where patient_id = '%s'",
                    surname, firstName, secondName, sex, status, telephone, relative_id, index));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}