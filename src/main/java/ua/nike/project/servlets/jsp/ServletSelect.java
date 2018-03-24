package ua.nike.project.servlets.jsp;

import ua.nike.project.service.ConnectToBase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/select_date")
public class ServletSelect extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       resp.getWriter().write(getOptions());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write(getOptions());
    }

    private String getOptions() {
        StringBuilder result = new StringBuilder();

        try {
            result.append("<option disabled selected value=Виберіть дату...>Виберіть дату...</option>\n");
            List<Date> dateList = getOperationDates();
            for (Date date : dateList) {
                result.append(String.format("<option value='%s'>%s</option>\n", date, date));
            }
        }catch (SQLException e){
            result.append("<option disabled selected value=Помилка бази. Спробуйте пізніше...>Помилка бази. Спробуйте пізніше...</option>\n");
        }
        return result.toString();
    }

    private List<Date> getOperationDates() throws SQLException {

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
