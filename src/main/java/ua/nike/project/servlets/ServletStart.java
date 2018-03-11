package ua.nike.project.servlets;

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
import java.util.TreeSet;

@WebServlet("/operation_index")
public class ServletStart extends HttpServlet {

    private String httpOutStart =
            "<!DOCTYPE HTML>\n" +
                    "<html>\n" +
                    "   <head>\n" +
                    "       <meta charset=\"UTF-8\">\n" +
                    "       <title>Заїзд на операції</title>\n" +
                    "</head>\n" +
                    "<body>\n";
    private String httpOutBody = "";
    private String httpOutEnd = "" +
            "</body>\n" +
            "</html>\n";

    private LocalDate selectedDate = LocalDate.now();

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String responseContentType = "text/html;charset=UTF-8";
        httpServletResponse.setContentType(responseContentType);
        String reqDate = httpServletRequest.getParameter("date");
        if (reqDate != null & reqDate !="") {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            selectedDate = LocalDate.parse(reqDate, format);
        } else {
            selectedDate = LocalDate.now();
        }
        httpServletResponse.getWriter().write(httpOutStart + getHttpOutBody(selectedDate) + httpOutEnd);
        httpOutBody = "";
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doGet(httpServletRequest, httpServletResponse);
    }

    private String getHttpOutBody(LocalDate selectedDate) {

        String sql = "SELECT operationday.operationdate, patient.surname, patient.firstname, patient.secondname, operation.operation_id, operation.eye, operationday.surgeon, operation.manager " +
                "FROM operationday, operation, patient " +
                "WHERE operationdate = ? AND operation.patient_id = patient.patient_id AND operation.operationday_id = operationday.\"operationDay_id\"";
        try (Connection connection = ConnectToBase.getConnect();
             PreparedStatement prepStat = connection.prepareStatement(sql)) {

            prepStat.setDate(1, Date.valueOf(selectedDate));
            ResultSet resultSet = prepStat.executeQuery();


            httpOutBody = httpOutBody.concat("" +
                    "<form method='post'>\n" +
                    "    <p>Виберіть дату: <input list=\"date\" name=\"date\">\n" +
                    "        <datalist id=\"date\">\n");

            TreeSet<Date> dateList = getOperationDates();
            for (Date date : dateList) {
                httpOutBody = httpOutBody.concat("<option value=\"" + date + "\">\n");
            }

            httpOutBody = httpOutBody.concat("" +
                    "        </datalist>\n" +
                    "        <input type=\"submit\" value=\"Отправить\"></p>\n" +
                    "</form>\n" +
                    "" +
                    "<table border=\"1\">" +
                    "<tr><th> Дата </th><th> Пацієнт </th><th> Операція та око </th><th> Хірург </th><th> Менеджер </th></tr>\n");

            while (resultSet.next()) {
                httpOutBody = httpOutBody.concat("<tr ><td > " + resultSet.getDate("operationdate") + " </td >" +
                        "<td > " + resultSet.getString("surname") + " " + resultSet.getString("firstname") + " " + resultSet.getString("secondname") + " </td >" +
                        "<td > " + resultSet.getString("operation_id") + " " + resultSet.getString("eye") + " </td >" +
                        "<td > " + resultSet.getString("surgeon") + " </td >" +
                        "<td > " + resultSet.getString("manager") + " </td ></tr >\n");
            }
            httpOutBody = httpOutBody.concat("</table>\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return httpOutBody;
    }

    private TreeSet<Date> getOperationDates() {

        TreeSet<Date> listDate = new TreeSet<>();
        String sqlDate = "SELECT operationday.operationdate " +
                "FROM operationday ";
        try (Connection connection = ConnectToBase.getConnect();
             PreparedStatement prepStat = connection.prepareStatement(sqlDate)) {
            ResultSet resultSet = prepStat.executeQuery();
            while (resultSet.next()) {
                Date date = resultSet.getDate("operationdate");
                listDate.add(date);
            }
            return listDate;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listDate;
    }
}
