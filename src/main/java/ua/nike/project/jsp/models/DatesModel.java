package ua.nike.project.jsp.models;

import ua.nike.project.service.ConnectToBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatesModel {

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
}
