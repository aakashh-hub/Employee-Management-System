<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Delete Employee</title>
</head>
<body>
    <h2>Delete Employee</h2>
    <form action="MainServlet" method="post">
        <input type="hidden" name="action" value="Delete Employees">
        <label for="employee">Select ID of Employee to Delete:</label>
        <select name="employee" id="employee">
            <c:forEach var="emp" items="${employee}">
                <option value="${emp.employeeId}">${emp.employeeId}</option>
            </c:forEach>
        </select>
        <br><br>
        <input type="submit" value="Delete Employees">
    </form>
</body>
</html>
