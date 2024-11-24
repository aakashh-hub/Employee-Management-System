<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Delete Employee Type</title>
</head>
<body>
    <h2>Delete Employee Type</h2>
    <form action="MainServlet" method="post">
        <input type="hidden" name="action" value="Delete Employee Types">
        <label for="employeeType">Select Employee Type to Delete:</label>
        <select name="employeeType" id="employeeType">
            <c:forEach var="empType" items="${employeeTypes}">
                <option value="${empType.employeeType}">${empType.employeeType}</option>
            </c:forEach>
        </select>
        <br><br>
        <input type="submit" value="Delete Employee Type">
    </form>
</body>
</html>
