<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ page import="ua.nike.project.service.ConnectToBase" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.*" %>

<html>
<head>
    <title>Заїзд на операції</title>
</head>
<body>

<% if (request.getMethod().equals("GET")) {                                                     %>

<%      try {                                                                                   %>
<%=         getHeaderHtml()                                                                     %>
<%          } catch (Exception e) {                                                          %>
                     <div> Error 500 </div>
<%      }                                                                                       %>
<% } else if (request.getMethod().equals("POST")) {                                             %>
<%      try {                                                                                   %>
<%=         getHeaderHtml()                                                                     %>
<%          String reqDate = request.getParameter("date");                                      %>

<%          if (reqDate != null && !reqDate.equals("")) {                                       %>
<%              DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");           %>
<%              LocalDate selectedDate = LocalDate.parse(reqDate, format);                      %>
<%=             getTableHtml(selectedDate)                                                      %>
<%          } else {                                                                            %>
         <div> Введіть обовязково дату! </div>
<%          }                                                                                   %>

<%          } catch (Exception e) {                                                             %>
                <div> Error 500 : <%= e.getMessage()%> </div>
<%      }                                                                                       %>
<%  }                                                                                           %>


<%!
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

            result.append("Звіт по прооперованих пацієнтах за " + selectedDate + " :\n" +
                    "<table border='1'>\n" +
                    "<tr><th> Дата </th><th> Пацієнт </th><th> Операція та око </th><th> Хірург </th><th> Менеджер </th></tr>\n");

            while (resultSet.next()) {
                result.append("<tr ><td > " + resultSet.getDate("operationdate") + " </td >" +
                        "<td > " + resultSet.getString("surname") + " " + resultSet.getString("firstname") + " " + resultSet.getString("secondname") + " </td >" +
                        "<td > " + resultSet.getString("operation_id") + " " + resultSet.getString("eye") + " </td >" +
                        "<td > " + resultSet.getString("surgeon") + " </td >" +
                        "<td > " + resultSet.getString("manager") + " </td ></tr >\n");
            }
            result.append("</table>\n");

        }

        return result.toString();
    }
%>

<%!
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
%>

<%!
    private String getHeaderHtml() throws SQLException {
        StringBuilder result = new StringBuilder();
        result.append("" +
                "<form method='post'>\n" +
                "    <p>Виберіть дату: <select name = 'date' autofocus> \n" +
                " <option disabled selected value=Виберіть дату...>Виберіть дату...</option> \n");

        List<Date> dateList = getOperationDates();
        for (Date date : dateList) {
            result.append(String.format("<option value='%s'>%s</option>\n", date, date));
        }

        result.append("" +
                "        </select>\n" +
                "        <input type='submit' value='Отправить'></p>\n" +
                "</form>\n");
        return result.toString();
    }
%>

</body>
</html>
