<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

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
        <input type='submit' value='Відправити'>

        <jsp:useBean id="SelectedDate" class="ua.nike.project.jsp.beans.SelectedDate" scope="page"/>
        <jsp:setProperty name="SelectedDate" property="date"/>

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
