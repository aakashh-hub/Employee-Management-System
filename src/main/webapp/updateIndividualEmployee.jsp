<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<head><title>Update Details</title></head>
<body>
    <h2>Update Your Details</h2>
    <form action="MainServlet" method="post">
        <table>
            <tr>
                <td>Employee ID:</td>
                <td>${employee.employeeId}</td>
            </tr>
            <tr>
                <td>Employee Name:</td>
                <td><input type="text" name="employeeName" value="${employee.employeeName}" required /></td>
            </tr>
            <tr>
                <td>Department:</td>
                <td>${employee.employeeDepartment}</td>
            </tr>
            <tr>
                <td>Employee Type:</td>
                <td>${employee.employeeType}</td>
            </tr>
            <tr>
                <td>Email:</td>
                <td>${employee.employeeEmailId}</td>
            </tr>
            <tr>
                <td>Number:</td>
                <td><input type="text" name="employeeNumber" value="${employee.employeeNumber}" required/></td>
            </tr>
        </table>
        <button type="submit" name="action" value="Save Updated Details">Save Changes</button>
    </form>
    <form action="MainServlet" method="get">
        <button type="submit" name="action" value="Reset Password">Reset Password</button>
    </form>
</body>
</html>
