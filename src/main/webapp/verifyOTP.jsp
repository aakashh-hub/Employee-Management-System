<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Verify OTP</title>
</head>
<body>
    <h2>Enter OTP</h2>
    <form action="MainServlet" method="post">
        <input type="text" name="otp" placeholder="Enter OTP" required>
        <input type="hidden" name="action" value="Verify OTP">
        <button type="submit">Verify OTP</button>
    </form>
</body>
</html>
