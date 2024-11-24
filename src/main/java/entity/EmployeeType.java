package entity;

public class EmployeeType {
    int employeeTypeId;
    String employeeType;

    public EmployeeType(int employeeTypeId, String employeeType) {
        this.employeeTypeId = employeeTypeId;
        this.employeeType = employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeTypeId(int employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    public int getEmployeeTypeId() {
        return employeeTypeId;
    }
}
