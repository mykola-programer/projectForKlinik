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
        <c:forEach items="${dates}" var="date_for_list">
            <option value="${date_for_list}">${date_for_list}</option>
        </c:forEach>

    </select>
        <input type='submit' value='Відправити'>

    </p>

</form>
Звіт по прооперованих пацієнтах за <fmt:formatDate value="${date}" type="date" dateStyle="LONG"/> :

<table border='1'>
    <tr>
        <th> Дата</th>
        <th> Пацієнт</th>
        <th> Операція та око</th>
        <th> Хірург</th>
        <th> Менеджер</th>
    </tr>

    <c:forEach items="${list_of_operation}" var="operation">
        <tr>
            <td>${operation.operationDate}</td>
            <td>${operation.surname} ${operation.firstname} ${operation.secondname} </td>
            <td>${operation.operation} ${operation.eye}</td>
            <td>${operation.surgeon}</td>
            <td>${operation.manager}</td>
        </tr>
    </c:forEach>

</table>
<br>
<a href="/mvc/date.jsp" target="_blank" >на іншу сторінку</a>
</body>
</html>
