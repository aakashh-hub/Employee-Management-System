<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head><title>View Employees</title></head>
<body>
    <h2>Employee Details</h2>
    <jsp:useBean id="employees" scope="request" type="java.util.List"/>
    <table class="table table-success table-striped">

        <tbody>
        <c:forEach var="employee" items="${employees}">
            <tr>
                <td>${employee.employeeId}</td>
                <td>${employee.employeeName}</td>
                <td>${employee.employeeDepartment}</td>
                <td>${employee.employeeType}</td>
                <td>${employee.employeeEmailId}</td>
            </tr>
        </c:forEach>
        </tbody>

    </table>
</body>
</html>