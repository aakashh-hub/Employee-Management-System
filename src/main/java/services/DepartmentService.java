package services;
import dao.DatabaseConfig;
import entity.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentService {
    public void addDepartment(Department department) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO department_details (department_id, department_name) VALUES (?, ?)")) {
            statement.setInt(1, department.getDepartmentId());
            statement.setString(2, department.getDepartmentName());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while adding the department: " + e.getMessage());
        }
    }

    public List<Department> getDepartmentDetails() {
        List<Department> departments = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM department_details")) {
            while (resultSet.next()) {
                int departmentId = resultSet.getInt("department_id");
                String departmentName = resultSet.getString("department_name");
                departments.add(new Department(departmentId, departmentName));
            }
        } catch (SQLException e) {
            System.out.println("Error while displaying department details: " + e.getMessage());
        }
        return departments;
    }

    public void deleteFromDepartmentDetails(String departmentName) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM department_details WHERE department_name = ?")) {
            statement.setString(1, departmentName);
            statement.executeUpdate();

            System.out.println("Department deleted successfully");
        } catch (SQLException e) {
            System.out.println("Error while deleting the department: " + e.getMessage());
        }
    }

    public int generateDepartmentId() {
        int id = 1;
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT MAX(department_id) AS max_id FROM department_details")) {
            if (resultSet.next()) {
                id = resultSet.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            System.out.println("Error while generating department id: " + e.getMessage());
        }
        return id;
    }

    public List<Integer> getDeletedDepartmentIds(Timestamp lastSync) {
        List<Integer> deletedIds = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT record_id FROM deletion_log WHERE table_name = 'department_details' AND deleted_at > ?")) {
            statement.setTimestamp(1, lastSync);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                deletedIds.add(resultSet.getInt("record_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving deleted department IDs: " + e.getMessage());
        }
        return deletedIds;
    }

    public List<Department> getNewDepartments(Timestamp lastSync) {
        List<Department> newDepartments = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM department_details WHERE last_updated > ?")) {
            statement.setTimestamp(1, lastSync);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                newDepartments.add(new Department(resultSet.getInt("department_id"), resultSet.getString("department_name")));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving new departments: " + e.getMessage());
        }
        return newDepartments;
    }

    // -------------------------------------------------------------------------------------------------- //
    public List<Department> getPaginatedNewDepartments(Timestamp lastSync, int offset, int limit) {
        List<Department> newDepartments = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM department_details WHERE last_updated > ? LIMIT ? OFFSET ?")) {
            statement.setTimestamp(1, lastSync);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                newDepartments.add(new Department(
                        resultSet.getInt("department_id"),
                        resultSet.getString("department_name")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving paginated new departments: " + e.getMessage());
        }
        return newDepartments;
    }

    public int getNewDepartmentsCount(Timestamp lastSync) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) AS count FROM department_details WHERE last_updated > ?")) {
            statement.setTimestamp(1, lastSync);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error counting new departments: " + e.getMessage());
        }
        return 0;
    }

    public List<Integer> getPaginatedDeletedDepartmentIds(Timestamp lastSync, int offset, int limit) {
        List<Integer> deletedIds = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT record_id FROM deletion_log WHERE table_name = 'department_details' AND deleted_at > ? LIMIT ? OFFSET ?")) {
            statement.setTimestamp(1, lastSync);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                deletedIds.add(resultSet.getInt("record_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving paginated deleted department IDs: " + e.getMessage());
        }
        return deletedIds;
    }

    public int getDeletedDepartmentIdsCount(Timestamp lastSync) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) AS count FROM deletion_log WHERE table_name = 'department_details' AND deleted_at > ?")) {
            statement.setTimestamp(1, lastSync);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error counting deleted department IDs: " + e.getMessage());
        }
        return 0;
    }
}