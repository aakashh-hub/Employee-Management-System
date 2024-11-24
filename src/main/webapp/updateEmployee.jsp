<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Update Employee Details</title>
</head>
<body>
    <h2>Update Employee Details</h2>
    <form action="MainServlet" method="post">
        <input type="hidden" name="action" value="Update Employees">  <!-- Fixed action value -->

        Employee ID:
        <select name="id">
            <c:forEach var="emp" items="${employees}">
                <option value="${emp.employeeId}">ID: ${emp.employeeId} - ${emp.employeeName}</option>
            </c:forEach>
        </select>
        </br>
        </br>

        Department:
        <select name="department">
            <c:forEach var="department" items="${departments}">
                <option value="${department.departmentName}">${department.departmentName}</option>
            </c:forEach>
        </select>
        </br>
        </br>

        Type:
        <select name="type">
            <c:forEach var="employeeType" items="${employeeTypes}">
                <option value="${employeeType.employeeType}">${employeeType.employeeType}</option>
            </c:forEach>
        </select>
        </br>
        </br>

        Email: <input type="email" name="email" required>
        </br>
        </br>

        Number: <input type="text" name="number" required>
        </br>
        </br>
        <input type="submit" value="Update Employee">
    </form>
</body>
</html>