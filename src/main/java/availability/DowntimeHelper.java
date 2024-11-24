package availability;

import dao.DatabaseConfig;

import java.sql.*;

public class DowntimeHelper {
    public void updateLastDownTimestamp(Timestamp timestamp) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE server1_status SET lastdown_timestamp = ? WHERE id = 1")) {
            statement.setTimestamp(1, timestamp);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating last down timestamp: " + e.getMessage());
        }
    }

    public void resetLastDownTimestamp() {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "UPDATE server1_status SET lastdown_timestamp = NULL WHERE id = 1")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error resetting last down timestamp: " + e.getMessage());
        }
    }

    public Timestamp getLastDownTimestamp() {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT lastdown_timestamp FROM server1_status WHERE id = 1")) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getTimestamp("lastdown_timestamp");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching last down timestamp: " + e.getMessage());
        }
        return null;
    }

}

