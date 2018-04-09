package ua.nike.project.klinika.jsp.model;

import ua.nike.project.klinika.service.ConnectToBase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class OperationDateModel {
    public static List<LocalDate> getOperationDates() throws SQLException {

        List<LocalDate> result = new ArrayList<>();
        String sql = "SELECT DISTINCT operationday.operationdate FROM operationday ORDER BY operationdate ";
        Connection connection = ConnectToBase.getConnection();
        try (PreparedStatement prepStat = connection.prepareStatement(sql)) {
            ResultSet resultSet = prepStat.executeQuery();
            while (resultSet.next()) {
                LocalDate date = resultSet.getDate("operationdate").toLocalDate();
                result.add(date);
            }
        }
        return result;
    }

}
