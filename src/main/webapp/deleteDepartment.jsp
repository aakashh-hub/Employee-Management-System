<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Delete Department</title>
</head>
<body>
    <h2>Delete Department</h2>
    <form action="MainServlet" method="post">
        <input type="hidden" name="action" value="Delete Department Detail">
        <label for="department">Select Department to Delete:</label>
        <select name="department" id="department">
            <c:forEach var="department" items="${departments}">
                <option value="${department.departmentName}">${department.departmentName}</option>
            </c:forEach>
        </select>
        <br><br>
        <input type="submit" value="Delete Department">
    </form>
</body>
</html>
