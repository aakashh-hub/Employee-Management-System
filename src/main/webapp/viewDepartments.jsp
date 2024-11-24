<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head><title>View Departments</title></head>
<body>
    <h2>Department Details</h2>
    <jsp:useBean id="departments" scope="request" type="java.util.List"/>

    <table class="table table-success table-striped">
        <tbody>
        <c:forEach var="department" items="${departments}">
            <tr>
                <td>${department.departmentName}</td>
            </tr>
        </c:forEach>
        </tbody>

    </table>
</body>
</html>