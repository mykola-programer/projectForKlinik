package ua.nike.project.mvc;

import ua.nike.project.service.ConnectToBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


public class ModelDate {
    public List<Date> getOperationDates() throws SQLException {

        List<Date> result = new ArrayList<>();
        String sqlDate = "SELECT DISTINCT operationday.operationdate FROM operationday ORDER BY operationdate ";
        Connection connection = ConnectToBase.getConnection();
        try (PreparedStatement prepStat = connection.prepareStatement(sqlDate)) {
            ResultSet resultSet = prepStat.executeQuery();
            while (resultSet.next()) {
                Date date = resultSet.getDate("operationdate");
                result.add(date);
            }
        }
        return result;
    }

    public List<BeanResultTable> getResultOperation(Date selectedDate) throws SQLException {
        List<BeanResultTable> result = new ArrayList<>();

        String sql = "SELECT od.operationdate, p.surname, p.firstname, p.secondname, o.operation_id, o.eye, od.surgeon, o.manager " +
                "FROM operation o " +
                "INNER JOIN operationday od ON o.operationday_id = od.operationday_id " +
                "INNER JOIN patient p ON o.patient_id = p.patient_id " +
                "WHERE operationdate = ? ";
        Connection connection = ConnectToBase.getConnection();
        try (PreparedStatement prepStat = connection.prepareStatement(sql)) {

            prepStat.setDate(1, (java.sql.Date)(selectedDate));
            ResultSet resultSet = prepStat.executeQuery();

            while (resultSet.next()) {
                BeanResultTable resTable = new BeanResultTable();
                resTable.setOperationDate(resultSet.getDate("operationdate"));
                resTable.setSurname(resultSet.getString("surname"));
                resTable.setFirstname(resultSet.getString("firstname"));
                resTable.setSecondname(resultSet.getString("secondname"));
                resTable.setOperation(resultSet.getString("operation_id"));
                resTable.setEye(resultSet.getString("eye"));
                resTable.setSurgeon(resultSet.getString("surgeon"));
                resTable.setManager(resultSet.getString("manager"));
                result.add(resTable);
            }
        }
        return result;
    }
}
