<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <title>Заїзд на операції (MVC)</title>
</head>
<body>
<form method='GET'>
    <p>
        ${Massage} <br>
        ${ErrorMassage} <br>
    </p>
    <p>Виберіть дату: <select name='date' autofocus>
        <option disabled selected value='#'>Виберіть дату...</option>
        <c:forEach items="${operation_dates}" var="operation_date">
            <option value="${operation_date}">${operation_date}</option>
        </c:forEach>

    </select>
        <input type='submit' value='Відправити'>

    </p>

</form>
<c:if test="${selected_date != null and ErrorMassage == null and Massage == null}">
    Звіт по прооперованих пацієнтах за <fmt:formatDate value="${selected_date}" type="date" dateStyle="LONG"/> :

    <table border='1'>
        <tr>
            <th> Дата</th>
            <th> Пацієнт</th>
            <th> Операція та око</th>
            <th> Хірург</th>
            <th> Менеджер</th>
        </tr>

        <c:forEach items="${visits}" var="visit">
            <tr>
                <td>${visit.visitDate}</td>
                <td>${visit.surname} ${visit.firstname} ${visit.secondname} </td>
                <td>${visit.visit} ${visit.eye}</td>
                <td>${visit.surgeon}</td>
                <td>${visit.manager}</td>
            </tr>
        </c:forEach>

    </table>
</c:if>
<br>
</body>
</html>
