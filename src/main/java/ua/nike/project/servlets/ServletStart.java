package ua.nike.project.servlets;

import ua.nike.project.service.ConnectToBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.TreeSet;


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
        httpOutBody = "";
        String reqDate = httpServletRequest.getParameter("date");
        if (reqDate != null){
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            selectedDate = LocalDate.parse(reqDate, format);
        }else {
            selectedDate = LocalDate.now();
        }
        System.out.println(selectedDate);
        httpServletResponse.getWriter().write(httpOutStart + getHttpOutBody(selectedDate) + httpOutEnd);
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        doGet(httpServletRequest, httpServletResponse);
    }

    private String getHttpOutBody(LocalDate selectedDate) {

        String sql = "SELECT operationday.operationdate, patient.surname, operationday.surgeon " +
                "FROM operationday, operation, patient " +
                "WHERE operationdate = ? AND operation.patient_id = patient.patient_id AND operation.operationday_id = operationday.\"operationDay_id\"";
        try (Connection connection = ConnectToBase.getConnect();
             PreparedStatement prepStat = connection.prepareStatement(sql)) {

            prepStat.setDate(1, Date.valueOf(selectedDate));
            ResultSet resultSet = prepStat.executeQuery();


            httpOutBody += "" +
                    "<form>\n" +
                    "    <p>Виберіть дату: <input list=\"date\" name=\"date\">\n" +
                    "        <datalist id=\"date\">\n";

            TreeSet<Date> dateList = getOperationDates();
            for (Date date : dateList) {
                httpOutBody += "<option value=\"" + date + "\">\n";
            }

            httpOutBody += "" +
                    "        </datalist>\n" +
                    "        <input type=\"submit\" value=\"Отправить\"></p>\n" +
                    "</form>" +
                    "" +
                    "<table border=\"1\">" +
                    "<tr><th> Дата </th><th> Пацієнт </th><th> Хірург </th></tr>\n";

            while (resultSet.next()) {
                httpOutBody += "<tr ><td > " + resultSet.getDate(1) + " </td >" +
                        "<td > " + resultSet.getString(2) + " </td >" +
                        "<td > " + resultSet.getString(3) + " </td ></tr >\n";
            }
            httpOutBody += "</table>\n";

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
