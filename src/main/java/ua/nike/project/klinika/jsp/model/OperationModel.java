package ua.nike.project.klinika.jsp.model;

import ua.nike.project.service.ConnectToBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class OperationModel {

    public static List<OperationBean> getResultOperation(LocalDate selectedDate) throws SQLException {
        List<OperationBean> result = new ArrayList<>();

        String sql = "SELECT od.operationdate, p.surname, p.firstname, p.secondname, o.operation_id, o.eye, od.surgeon, o.manager " +
                "FROM operation o " +
                "INNER JOIN operationday od ON o.operationday_id = od.operationday_id " +
                "INNER JOIN patient p ON o.patient_id = p.patient_id " +
                "WHERE operationdate = ? ";
        Connection connection = ConnectToBase.getConnection();
        try (PreparedStatement prepStat = connection.prepareStatement(sql)) {

            prepStat.setDate(1, Date.valueOf(selectedDate));
            ResultSet resultSet = prepStat.executeQuery();

            while (resultSet.next()) {
                OperationBean resTable = new OperationBean();
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
