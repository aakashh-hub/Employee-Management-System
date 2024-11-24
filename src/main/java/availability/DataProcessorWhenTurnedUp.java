package availability;

import dao.DatabaseConfig;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class DataProcessorWhenTurnedUp {
    public void processUpdatedData(Map<String, Object> updatedData) {

        if(updatedData.get("users") != null) {
            List<Map<String, String>> users = (List<Map<String, String>>) updatedData.get("users");
            for (Map<String, String> user : users) {
                String username = user.get("username");
                String password = user.get("password");
                insertUser(username, password);
            }
        }
        System.out.println("done with processing users");

        // --------------------------------------------------------------------------------------------------------------- //

        if (updatedData.get("newDepartments") != null) {
            List<Map<String, Object>> newDepartments = (List<Map<String, Object>>) updatedData.get("newDepartments");
            System.out.println(newDepartments);
            for (Map<String, Object> department : newDepartments) {
                String departmentName = (String) department.get("departmentName");
                int departmentId = 0;
                Object deptIdObj = department.get("department_id");
                if (deptIdObj instanceof Number) {
                    departmentId = ((Number) deptIdObj).intValue();
                } else if (deptIdObj instanceof String) {
                    departmentId = Integer.parseInt((String) deptIdObj);
                }
                insertDepartment(departmentId, departmentName);
            }
        }
        System.out.println("done with processing new departments");

        if (updatedData.get("deletedDepartments") != null) {
            List<Object> deletedDepartmentIds = (List<Object>) updatedData.get("deletedDepartments");
            for (Object deptIdObj : deletedDepartmentIds) {
                int departmentId = 0;
                if (deptIdObj instanceof Number) {
                    departmentId = ((Number) deptIdObj).intValue();
                } else if (deptIdObj instanceof String) {
                    departmentId = Integer.parseInt((String) deptIdObj);
                }
                deleteDepartment(departmentId);
            }
        }
        System.out.println("done with processing deleted depts");

        // --------------------------------------------------------------------------------------------------------------- //

        if (updatedData.get("newEmployeeTypes") != null) {
            List<Map<String, Object>> newEmployeeTypes = (List<Map<String, Object>>) updatedData.get("newEmployeeTypes");
            System.out.println(newEmployeeTypes);

            for (Map<String, Object> empTypes : newEmployeeTypes) {
                String employeeTypeName = (String) empTypes.get("employeeType");
                int employeeType_id = 0;
                Object empTypeObj = empTypes.get("employeeTypeId");
                if (empTypeObj instanceof Number) {
                    employeeType_id = ((Number) empTypeObj).intValue();
                } else if (empTypeObj instanceof String) {
                    employeeType_id = Integer.parseInt((String) empTypeObj);
                }
                insertEmployeeTypes(employeeType_id, employeeTypeName);
            }
            System.out.println("done with processing new emp types");
        }

        if (updatedData.get("deletedEmployeeTypes") != null) {
            List<Object> deletedEmployeeTypes = (List<Object>) updatedData.get("deletedEmployeeTypes");
            if (!deletedEmployeeTypes.isEmpty()) {
                for (Object empTypeObj : deletedEmployeeTypes) {
                    int empTypeId = 0;
                    if (empTypeObj instanceof Number) {
                        empTypeId = ((Number) empTypeObj).intValue();
                    } else if (empTypeObj instanceof String) {
                        empTypeId = Integer.parseInt((String) empTypeObj);
                    }
                    deleteEmployeeTypes(empTypeId);
                }
            }
        }
        System.out.println("done with processing deleted emp types");

        // --------------------------------------------------------------------------------------------------------------- //

        if(updatedData.get("employeesData") != null) {
            List<Map<String, Object>> employeesData = (List<Map<String, Object>>) updatedData.get("employeesData");
            System.out.println("new emps : " + employeesData);

            for (Map<String, Object> employee : employeesData) {
                int employeeId = ((Double) employee.get("employeeId")).intValue();
                String employeeName = (String) employee.get("employeeName");
                String department = (String) employee.get("employeeDepartment");
                String employeeType = (String) employee.get("employeeType");
                String email = (String) employee.get("employeeEmailId");
                String number = (String) employee.get("employeeNumber");
                System.out.println(employeeId + " " + employeeName + " " + department + " " + employeeType + " " + email);
                updateOrInsertEmployee(employeeId, employeeName, department, employeeType, email, number);
            }
            System.out.println("done with new emps");
        }

        if(updatedData.get("deletedEmployees") != null) {
            List<Object> deletedEmployeeIds = (List<Object>) updatedData.get("deletedEmployees");
            System.out.println("deleted emps : " + deletedEmployeeIds);
            for (Object deptIdObj : deletedEmployeeIds) {
                int empId = 0;
                if (deptIdObj instanceof Number) {
                    empId = ((Number) deptIdObj).intValue();
                } else if (deptIdObj instanceof String) {
                    empId = Integer.parseInt((String) deptIdObj);
                }
                deleteEmployee(empId);
            }
            System.out.println("done with deleted emps");
        }
        System.out.println("done with emps");
    }

    // --------------------------------------------------------------------------------------------------------------- //

    private void insertUser(String username, String password) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO employee_login_details (username, passwd) VALUES (?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while updating or inserting user: " + e.getMessage());
        }
    }

    private void insertDepartment(int departmentId, String departmentName) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO department_details (department_id, department_name) VALUES (?, ?)")) {
            statement.setInt(1, departmentId);
            statement.setString(2, departmentName);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while updating or inserting department: " + e.getMessage());
        }
        System.out.println("departments inserted");
    }

    private void deleteDepartment(int departmentId) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM department_details WHERE department_id = ?")) {
            statement.setInt(1, departmentId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while deleting department: " + e.getMessage());
        }
        System.out.println("departments deleted");
    }

    private void insertEmployeeTypes(int empTypeId, String employeeType) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO employee_types (emp_type_id, employee_type) VALUES (?, ?)")) {
            statement.setInt(1, empTypeId);
            statement.setString(2, employeeType);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while updating or inserting employee type: " + e.getMessage());
        }
    }

    private void deleteEmployeeTypes(int empTypeId) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM employee_types WHERE emp_type_id = ?")) {
            statement.setInt(1, empTypeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while deleting employee Types: " + e.getMessage());
        }
        System.out.println("emp types deleted");
    }

    private void updateOrInsertEmployee(int employeeId, String employeeName, String department,
                                        String employeeType, String email, String number) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO employee_details (employee_id, employee_name, employee_department, employee_type, employee_email) " +
                             "VALUES (?, ?, ?, ?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE employee_name = VALUES(employee_name), " +
                             "employee_department = VALUES(employee_department), " +
                             "employee_type = VALUES(employee_type), " +
                             "employee_email = VALUES(employee_email), " +
                             "employee_number = VALUES(employee_number)")) {
            statement.setInt(1, employeeId);
            statement.setString(2, employeeName);
            statement.setString(3, department);
            statement.setString(4, employeeType);
            statement.setString(5, email);
            statement.setString(6, number);
            statement.executeUpdate();
            System.out.println("done");
        } catch (SQLException e) {
            System.out.println("Error while updating or inserting employee: " + e.getMessage());
        }
    }

    private void deleteEmployee(int employeeId) {
        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM employee_details WHERE employee_id = ?")) {
            statement.setInt(1, employeeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while deleting department: " + e.getMessage());
        }
        System.out.println("employees deleted");
    }
}
