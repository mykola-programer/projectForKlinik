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

        <c:forEach items="${operations}" var="operation">
            <tr>
                <td>${operation.operationDate}</td>
                <td>${operation.surname} ${operation.firstname} ${operation.secondname} </td>
                <td>${operation.operation} ${operation.eye}</td>
                <td>${operation.surgeon}</td>
                <td>${operation.manager}</td>
            </tr>
        </c:forEach>

    </table>
</c:if>
<br>
</body>
</html>
