<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Notifications</title>
    <style>
        #notificationArea {
            margin-top: 20px;
            padding: 10px;
            border: 1px solid #ddd;
            background-color: #f9f9f9;
            max-width: 500px;
            margin: 20px auto;
        }
        .notification {
            background-color: #e7f7ff;
            padding: 10px;
            margin: 5px 0;
            border-left: 5px solid #007bff;
        }
    </style>
</head>
<body>
    <h2>Welcome Admin!</h2>
    <div id="notificationArea">
        <h3>Notifications:</h3>
        <div id="notificationsContainer">
            <!-- Notifications will be appended here -->
        </div>
    </div>

    <script>
        const eventSource = new EventSource('/EmployeeManagementSystem/notification');

        eventSource.onmessage = function(event) {
            const notification = event.data;
            displayNotification(notification);
        };

        function displayNotification(message) {
            const notificationContainer = document.getElementById('notificationsContainer');
            const notificationElement = document.createElement('div');
            notificationElement.classList.add('notification');
            notificationElement.textContent = message;
            notificationContainer.appendChild(notificationElement);
        }

        eventSource.onerror = function() {
            displayNotification("Error receiving notifications.");
        };
    </script>
</body>
</html>