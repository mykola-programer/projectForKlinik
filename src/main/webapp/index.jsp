<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Заїзд на операції</title>
</head>
<body>
<form method='post'>
    <p>
        Виберіть дату: <select name = 'date' autofocus>
            <jsp:include page="/select_date"/>
        </select>
        <input type='submit' value='Отправить'>

        <jsp:useBean id="SelectedDate" class="ua.nike.project.servlets.jsp.beans.SelectedDate" >
            <% SelectedDate.setDate(request.getParameter("date"));%>
        </jsp:useBean>


    </p>
</form>

<%--<% if (request.getParameter("date") != null) { %>--%>

Звіт по прооперованих пацієнтах за <jsp:getProperty name="SelectedDate" property="date"/> :

<table border='1'>
    <tr><th> Дата </th><th> Пацієнт </th><th> Операція та око </th><th> Хірург </th><th> Менеджер </th></tr>
    <jsp:include page="/result_table" />
</table>

<%--<% } %>--%>

</body>
</html>
