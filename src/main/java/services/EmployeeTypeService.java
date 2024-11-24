package services;

import dao.DatabaseConfig;
import entity.EmployeeType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeTypeService {
    public void addEmployeeType(EmployeeType employeeType) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO employee_types (emp_type_id, employee_type) VALUES (?, ?)")) {
            statement.setInt(1, employeeType.getEmployeeTypeId());
            statement.setString(2, employeeType.getEmployeeType());
            statement.executeUpdate();

            System.out.println("Employee type added successfully");
        } catch (SQLException e) {
            System.out.println("Error while adding employee type: " + e.getMessage());
        }
    }

    public List<EmployeeType> getEmployeeTypeDetails() {
        List<EmployeeType> employeeTypes = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM employee_types")) {
            while (resultSet.next()) {
                int employeeTypeId = resultSet.getInt("emp_type_id");
                String employeeTypeName = resultSet.getString("employee_type");
                employeeTypes.add(new EmployeeType(employeeTypeId, employeeTypeName));
            }
        } catch (SQLException e) {
            System.out.println("Error while displaying employee type details: " + e.getMessage());
        }
        return employeeTypes;
    }

    public void deleteEmployeeType(String employeeTypeName) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM employee_types WHERE employee_type = ?")) {
            statement.setString(1, employeeTypeName);
            statement.executeUpdate();

            System.out.println("Employee type deleted successfully");
        } catch (SQLException e) {
            System.out.println("Error while deleting the employee type: " + e.getMessage());
        }
    }

    public int generateEmployeeTypeId() {
        int id = 1;
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT MAX(emp_type_id) AS max_id FROM employee_types")) {
            if (resultSet.next()) {
                id = resultSet.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            System.out.println("Error while generating employee type id: " + e.getMessage());
        }
        return id;
    }

    public List<EmployeeType> getNewEmployeeTypes(Timestamp lastSync) {
        List<EmployeeType> newEmployeeTypes = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM employee_types WHERE last_updated > ?")) {
            statement.setTimestamp(1, lastSync);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                newEmployeeTypes.add(new EmployeeType(resultSet.getInt("emp_type_id"), resultSet.getString("employee_type")));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving new employee types : " + e.getMessage());
        }
        return newEmployeeTypes;
    }

    public List<Integer> getDeletedEmployeeTypeIds(Timestamp lastSync) {
        List<Integer> deletedIds = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT record_id FROM deletion_log WHERE table_name = 'employee_types' AND deleted_at > ?")) {
            statement.setTimestamp(1, lastSync);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                deletedIds.add(resultSet.getInt("record_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving deleted employee type IDs: " + e.getMessage());
        }
        return deletedIds;
    }

    // -------------------------------------------------------------------------------------------------------- //
    public List<EmployeeType> getPaginatedNewEmployeeTypes(Timestamp lastSync, int offset, int limit) {
        List<EmployeeType> newEmployeeTypes = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM employee_types WHERE last_updated > ? LIMIT ? OFFSET ?")) {
            statement.setTimestamp(1, lastSync);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                newEmployeeTypes.add(new EmployeeType(
                        resultSet.getInt("emp_type_id"),
                        resultSet.getString("employee_type")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving paginated new employee types: " + e.getMessage());
        }
        return newEmployeeTypes;
    }

    public int getNewEmployeeTypesCount(Timestamp lastSync) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) AS count FROM employee_types WHERE last_updated > ?")) {
            statement.setTimestamp(1, lastSync);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error counting new employee types: " + e.getMessage());
        }
        return 0;
    }

    public List<Integer> getPaginatedDeletedEmployeeTypeIds(Timestamp lastSync, int offset, int limit) {
        List<Integer> deletedIds = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT record_id FROM deletion_log WHERE table_name = 'employee_types' AND deleted_at > ? LIMIT ? OFFSET ?")) {
            statement.setTimestamp(1, lastSync);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                deletedIds.add(resultSet.getInt("record_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving paginated deleted employee type IDs: " + e.getMessage());
        }
        return deletedIds;
    }

    public int getDeletedEmployeeTypeIdsCount(Timestamp lastSync) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) AS count FROM deletion_log WHERE table_name = 'employee_types' AND deleted_at > ?")) {
            statement.setTimestamp(1, lastSync);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error counting deleted employee type IDs: " + e.getMessage());
        }
        return 0;
    }
}