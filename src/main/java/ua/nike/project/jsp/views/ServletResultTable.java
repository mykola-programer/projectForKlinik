package ua.nike.project.jsp.views;

import ua.nike.project.service.ConnectToBase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/result_table")
public class ServletResultTable  extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String reqDate = req.getParameter("date");
            if (reqDate != null && !reqDate.equals("")) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate selectedDate = LocalDate.parse(reqDate, format);

                resp.getWriter().write(getTableHtml(selectedDate));

            } else {
                resp.getWriter().write("\n <div> Введіть обовязково дату! </div>");
            }

        } catch (Exception e) {
            resp.getWriter().write("\n <div> Error 500. " + e.getMessage() + "</div>");
            e.printStackTrace();

        }
    }

    private String getTableHtml(LocalDate selectedDate) throws SQLException {
        StringBuilder result = new StringBuilder();

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
                result.append("<tr ><td > " + resultSet.getDate("operationdate") + " </td >" +
                        "<td > " + resultSet.getString("surname") + " " + resultSet.getString("firstname") + " " + resultSet.getString("secondname") + " </td >" +
                        "<td > " + resultSet.getString("operation_id") + " " + resultSet.getString("eye") + " </td >" +
                        "<td > " + resultSet.getString("surgeon") + " </td >" +
                        "<td > " + resultSet.getString("manager") + " </td ></tr >\n");
            }

        }

        return result.toString();
    }
}
