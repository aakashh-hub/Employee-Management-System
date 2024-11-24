<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head><title>View Employee Types</title></head>
<body>
    <h2>View Employee Types</h2>
    <jsp:useBean id="employeeTypes" scope="request" type="java.util.List"/>

    <table class="table table-success table-striped">
        <thead>
            <tr>
                <th>Employee Type</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="empType" items="${employeeTypes}">
            <tr>
                <td>${empType.employeeType}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</body>
</html>