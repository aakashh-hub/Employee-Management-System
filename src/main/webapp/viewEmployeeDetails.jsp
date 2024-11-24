<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<html>
<head>
    <title>Employee Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f7f6;
            margin: 0;
            padding: 20px;
        }

        h2 {
            color: #333;
            text-align: center;
        }

        table {
            width: 60%;
            margin: 20px auto;
            border-collapse: collapse;
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        th, td {
            padding: 12px 20px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        th {
            background-color: #007bff;
            color: white;
        }

        td {
            color: #555;
        }
    </style>
</head>
<body>
    <h2>Hello, ${employee.employeeName}</h2>
    <table class="table table-success table-striped">
        <tr><td>Employee ID:</td><td>${employee.employeeId}</td></tr>
        <tr><td>Employee Name:</td><td>${employee.employeeName}</td></tr>
        <tr><td>Department:</td><td>${employee.employeeDepartment}</td></tr>
        <tr><td>Employee Type:</td><td>${employee.employeeType}</td></tr>
        <tr><td>Email:</td><td>${employee.employeeEmailId}</td></tr>
        <tr><td>Number:</td><td>${employee.employeeNumber}</td></tr>
    </table>
</body>
</html>
