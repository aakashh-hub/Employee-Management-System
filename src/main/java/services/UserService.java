package services;

import dao.DatabaseConfig;
import entity.Employee;
import entity.User;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UserService {

//    private static final String ALGORITHM = "AES";
//    private static final String SECRET_KEY = "12345678901234567890123456789012";

    public void storeUserDetails(User user) {
//        String encryptedPassword = encrypt(user.getPassword());
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO employee_login_details (username, passwd) VALUES (?, ?)")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());

            statement.executeUpdate();
            System.out.println("user service server 1");
        } catch (SQLException e) {
            System.out.println("Error while signing up user: " + e.getMessage());
        }
    }

    public boolean isUserValid(User user) {
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM employee_login_details")) {
            while (resultSet.next()) {

                String username = resultSet.getString("username");
                String passwd = resultSet.getString("passwd");
//                String enc_passwd = encrypt(user.getPassword());

                if(user.getUsername().equals(username) && passwd.equals(user.getPassword())) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while displaying employees: " + e.getMessage());
        }
        return false;
    }

//    public String encrypt(String password) {
//        try {
//            Key key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
//            Cipher cipher = Cipher.getInstance(ALGORITHM);
//            cipher.init(Cipher.ENCRYPT_MODE, key);
//            byte[] encryptedBytes = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
//            return Base64.getEncoder().encodeToString(encryptedBytes);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public List<User> getUpdatedUsers(Timestamp lastSync) {
        List<User> updatedUsers = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM employee_login_details WHERE last_updated > ?")) {
            statement.setTimestamp(1, lastSync);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                updatedUsers.add(new User(
                        resultSet.getString("username"),
                        resultSet.getString("passwd")
                ));
            }
            System.out.println(updatedUsers);
        } catch (SQLException e) {
            System.out.println("Error retrieving updated users: " + e.getMessage());
        }
        return updatedUsers;
    }

    public String getUserRole(String username) {
        String role = "Employee";
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT role FROM employee_login_details WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = resultSet.getString("role");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching user role: " + e.getMessage());
        }
        return role;
    }

    public User getUserByUsername(String username) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee_login_details WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                String name = resultSet.getString("username");
                String oldPass = resultSet.getString("passwd");

                user.setUsername(name);
                user.setPassword(oldPass);
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Error while viewing employees: " + e.getMessage());
        }
        return null;
    }

    public void updateUserPassword(String username, String newPassword) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE employee_login_details SET passwd = ? WHERE username = ?")) {
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, username);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------------------------------------------ //
    public List<User> getPaginatedUpdatedUsers(Timestamp lastSync, int offset, int limit) {
        List<User> updatedUsers = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM employee_login_details WHERE last_updated > ? LIMIT ? OFFSET ?")) {
            statement.setTimestamp(1, lastSync);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                updatedUsers.add(new User(
                        resultSet.getString("username"),
                        resultSet.getString("passwd")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving paginated updated users: " + e.getMessage());
        }
        return updatedUsers;
    }

    public int getUpdatedUsersCount(Timestamp lastSync) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) AS count FROM employee_login_details WHERE last_updated > ?")) {
            statement.setTimestamp(1, lastSync);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error counting updated users: " + e.getMessage());
        }
        return 0;
    }
}
