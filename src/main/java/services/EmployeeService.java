package services;

import dao.DatabaseConfig;
import entity.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    public void addEmployee(Employee employee) {

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO employee_details (employee_id, employee_name, employee_department, employee_type, employee_email, employee_number) VALUES (?, ?, ?, ?, ?, ?)")) {
            statement.setInt(1, employee.getEmployeeId());
            statement.setString(2, employee.getEmployeeName());
            statement.setString(3, employee.getEmployeeDepartment());
            statement.setString(4, employee.getEmployeeType());
            statement.setString(5, employee.getEmployeeEmailId());
            statement.setString(6, employee.getEmployeeNumber());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while adding employee: " + e.getMessage());
        }
    }

    public List<Employee> getEmployeeDetails() {
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM employee_details")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("employee_id");
                String name = resultSet.getString("employee_name");
                String department = resultSet.getString("employee_department");
                String type = resultSet.getString("employee_type");
                String email = resultSet.getString("employee_email");
                String number = resultSet.getString("employee_number");
                employees.add(new Employee(id, name, department, type, email, number));
            }
        } catch (SQLException e) {
            System.out.println("Error while displaying employees: " + e.getMessage());
        }
        return employees;
    }

    public Employee getEmployeeByUsername(String username) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee_details WHERE employee_email = ?")) {
            statement.setString(1, username);
             ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Employee employee = new Employee();
                int id = resultSet.getInt("employee_id");
                String name = resultSet.getString("employee_name");
                String department = resultSet.getString("employee_department");
                String type = resultSet.getString("employee_type");
                String email = resultSet.getString("employee_email");
                String number = resultSet.getString("employee_number");
                employee.setEmployeeId(id);
                employee.setEmployeeName(name);
                employee.setEmployeeDepartment(department);
                employee.setEmployeeType(type);
                employee.setEmployeeEmailId(email);
                employee.setEmployeeNumber(number);
                return employee;
            }
        } catch (SQLException e) {
            System.out.println("Error while viewing employees: " + e.getMessage());
        }
        return null;
    }

    public int generateEmployeeId() {
        int id = 1;
        try (Connection connection = DatabaseConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT MAX(employee_id) AS max_id FROM employee_details")) {
            if (resultSet.next()) {
                id = resultSet.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            System.out.println("Error while generating employee id: " + e.getMessage());
        }
        return id;
    }

    public void deleteEmployee(String empId) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM employee_details WHERE employee_id = ?")) {
            int employeeId = Integer.parseInt(empId);
            statement.setInt(1, employeeId);
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while deleting the employee: " + e.getMessage());
        }
    }

    public void updateEmployee(String empId, String department, String type, String email, String mobile) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE employee_details SET employee_department = ?, employee_type = ?, employee_email = ?, employee_number = ? WHERE employee_id = ?")) {
            int employeeId = Integer.parseInt(empId);
            statement.setString(1, department);
            statement.setString(2, type);
            statement.setString(3, email);
            statement.setString(4, mobile);
            statement.setInt(5, employeeId);
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Employee updated successfully");
            } else {
                System.out.println("Employee with ID " + empId + " not found");
            }
        } catch (SQLException e) {
            System.out.println("Error updating employee: " + e.getMessage());
        }
    }

    public List<Employee> getUpdatedEmployees(Timestamp lastSync) {
        List<Employee> updatedEmployees = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM employee_details WHERE last_updated > ?")) {
            statement.setTimestamp(1, lastSync);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                updatedEmployees.add(new Employee(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("employee_name"),
                        resultSet.getString("employee_department"),
                        resultSet.getString("employee_type"),
                        resultSet.getString("employee_email"),
                        resultSet.getString("employee_number")
                ));
            }
            System.out.println(updatedEmployees);
        } catch (SQLException e) {
            System.out.println("Error retrieving updated employees: " + e.getMessage());
        }
        return updatedEmployees;
    }

    public List<Integer> getDeletedEmployeeIds(Timestamp lastSync) {
        List<Integer> deletedIds = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT record_id FROM deletion_log WHERE table_name = 'employee_details' AND deleted_at > ?")) {
            statement.setTimestamp(1, lastSync);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                deletedIds.add(resultSet.getInt("record_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving deleted employee IDs: " + e.getMessage());
        }
        return deletedIds;
    }

    public void updateIndividualEmployeeDetails(String username, String name, String number) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE employee_details SET employee_name = ?, employee_number = ? WHERE employee_email = ?")) {
            statement.setString(1, name);
            statement.setString(2, number);
            statement.setString(3, username);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Employee updated successfully");
            } else {
                System.out.println("Employee with email ID " + username + " not found");
            }
        } catch (SQLException e) {
            System.out.println("Error updating employee: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------------------------- //
    public List<Employee> getPaginatedNewEmployees(Timestamp lastSync, int offset, int limit) {
        List<Employee> newEmployees = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM employee_details WHERE last_updated > ? LIMIT ? OFFSET ?")) {
            statement.setTimestamp(1, lastSync);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                newEmployees.add(new Employee(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("employee_name"),
                        resultSet.getString("employee_department"),
                        resultSet.getString("employee_type"),
                        resultSet.getString("employee_email"),
                        resultSet.getString("employee_number")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving paginated new employees: " + e.getMessage());
        }
        return newEmployees;
    }

    public int getNewEmployeesCount(Timestamp lastSync) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) AS count FROM employee_details WHERE last_updated > ?")) {
            statement.setTimestamp(1, lastSync);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error counting new employees: " + e.getMessage());
        }
        return 0;
    }

    public List<Integer> getPaginatedDeletedEmployeeIds(Timestamp lastSync, int offset, int limit) {
        List<Integer> deletedIds = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT record_id FROM deletion_log WHERE table_name = 'employee_details' AND deleted_at > ? LIMIT ? OFFSET ?")) {
            statement.setTimestamp(1, lastSync);
            statement.setInt(2, limit);
            statement.setInt(3, offset);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                deletedIds.add(resultSet.getInt("record_id"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving paginated deleted employee IDs: " + e.getMessage());
        }
        return deletedIds;
    }

    public int getDeletedEmployeeIdsCount(Timestamp lastSync) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT COUNT(*) AS count FROM deletion_log WHERE table_name = 'employee_details' AND deleted_at > ?")) {
            statement.setTimestamp(1, lastSync);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error counting deleted employee IDs: " + e.getMessage());
        }
        return 0;
    }
}