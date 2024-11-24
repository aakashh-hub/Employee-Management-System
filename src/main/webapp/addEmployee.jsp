<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head><title>Add Employee</title></head>
<body>
    <h2>Add Employee</h2>
    <form action="MainServlet" method="post">
        <input type="hidden" name="action" value="Add Employee">

        Name: <input type="text" name="name">
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

        Email: <input type="email" name="email">
        </br>
        </br>

        Phone number: <input type="text" name="number">
        </br>
        </br>
        <input type="submit" value="Add Employee">
    </form>
</body>
</html>
