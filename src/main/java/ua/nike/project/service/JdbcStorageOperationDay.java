package ua.nike.project.service;

import ua.nike.project.struct.OperationDay;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This {@code JdbcStorageOperationDay} class is used for creating connection to the database "projectForClinic".
 * The class {@code JdbcStorageOperationDay} includes methods for working with table "OperationDay"...
 */
public class JdbcStorageOperationDay {

    /**
     * The value is used for the connection to database.
     */
    private final Connection CONNECT;

    /**
     * Initializes a newly created {@code JdbcStorageOperationDay} object and automatically creating connection
     * to the database.
     */
    public JdbcStorageOperationDay() {
        CONNECT = ConnectToBase.getConnection();
    }

    /**
     * This method return record from database, instance of class {@code OperationDay} of the index.
     *
     * @param index Index in the database.
     * @return Return instance of class {@code OperationDay}.
     * If generated exception, then return NULL.
     */
    public OperationDay getOperationDay(int index) {
        try (Statement statement = this.CONNECT.createStatement();
             ResultSet result = statement.executeQuery("select * from operationday where operationDay_id = " + index)) {
            result.next();
            OperationDay operationDay = new OperationDay();
            operationDay.setOperationDay_id(result.getInt("operationDay_id"));
            operationDay.setOperationDate(result.getDate("operationdate").toLocalDate());
            operationDay.setSurgeon(result.getString("surgeon"));
            return operationDay;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    //---------------------------------------------------------------------

    /**
     * This method return from database instance of class {@code OperationDay} of parameter and it's value.
     *
     * @param parameter Parameter in the database.
     * @param value     Value of parameter.
     * @return Return instance of class {@code OperationDay}.
     * If this method generated exception, then return NULL.
     */
    public OperationDay getOperationDay(String parameter, String value) {
        try (Statement statement = this.CONNECT.createStatement();
             ResultSet result = statement.executeQuery("select * from operationday where " + parameter + " = " + value)) {
            result.next();
            OperationDay operationDay = new OperationDay();
            operationDay.setOperationDay_id(result.getInt("operationDay_id"));
            operationDay.setOperationDate(result.getDate("operationdate").toLocalDate());
            operationDay.setSurgeon(result.getString("surgeon"));
            return operationDay;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This method insert to database instance of class {@code OperationDay} of parameter and his value.
     *
     * @param operationDay Instance of class {@code OperationDay}.
     * @return - After insert to database, this method return operationDay_id from database.
     * If this method generated exception, then return 0.
     */
    public int addOperationDay(OperationDay operationDay) {
        Integer operationDay_id = operationDay.getOperationDay_id();
        LocalDate operationDate = operationDay.getOperationDate();
        String surgeon = operationDay.getSurgeon();
        


/**   ----------------------------------------
 * Statement
 */
//        try (Statement statement = this.CONNECT.createStatement()) {
//            statement.executeUpdate(String.format("insert into operationDay (surname, firstName, secondName, sex, status, relative_id, telephone) " +
//                    "values ('%s', '%s', '%s', '%s', '%s', '%s', '%s')", surname, firstName, secondName, sex, status, relative_id, telephone));
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

/**   ---------------------------------------
 * PreparedStatement
 */
        String sql = "INSERT INTO operationday (operationdate, surgeon) VALUES (?,?);";
        try (final PreparedStatement preparedStatement = this.CONNECT.prepareStatement(sql)) {
            preparedStatement.setDate(1, Date.valueOf(operationDate));
            preparedStatement.setString(2, surgeon);
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
     * Getting list of instance of class {@code OperationDay} from table operationDay in database.
     *
     * @return List of instance of class {@code OperationDay} from table operationDay in database.
     * If this method generated exception, then return NULL.
     */
    public ArrayList<OperationDay> getOperationDays() {
        ArrayList<OperationDay> operationDays = new ArrayList<OperationDay>();
        try (Statement statement = this.CONNECT.createStatement();
             ResultSet result = statement.executeQuery("select * from operationDay")) {
            while (result.next()) {

                OperationDay operationDay = new OperationDay();
                operationDay.setOperationDay_id(result.getInt("operationDay_id"));
                operationDay.setOperationDate(result.getDate("operationdate").toLocalDate());
                operationDay.setSurgeon(result.getString("surgeon"));

                operationDays.add(operationDay);
            }
            return operationDays;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Correct record in the database.
     *
     * @param index   Index in the database.
     * @param operationDay Instance of class {@code OperationDay}
     * @return true, if operationDay in database is successfully update.
     * If this method generated exception, then return false.
     */
    public boolean editOperationDay(int index, OperationDay operationDay) {

        Integer operationDay_id = operationDay.getOperationDay_id();
        LocalDate operationDate = operationDay.getOperationDate();
        String surgeon = operationDay.getSurgeon();

        try (Statement statement = this.CONNECT.createStatement()) {
            statement.executeUpdate(String.format("update operationDay set " +
                            "operationDay_id = '%s', " +
                            "operationDate = '%s', " +
                            "surgeon = '%s', " +
                            "where operationDay_id = '%s'",
                    operationDay_id, operationDate, surgeon, index));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}