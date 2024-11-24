package entity;

public class Employee {

    private int employeeId;
    private String employeeName;
    private String employeeDepartment;
    private String employeeType;
    private String employeeEmailId;
    private String employeeNumber;

    public Employee(int employeeId, String employeeName, String employeeDepartment, String employeeType, String employeeEmailId, String employeeNumber) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeDepartment = employeeDepartment;
        this.employeeType = employeeType;
        this.employeeEmailId = employeeEmailId;
        this.employeeNumber = employeeNumber;
    }

    public Employee(){}

    public int getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    public String getEmployeeName() {
        return employeeName;
    }
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
    public String getEmployeeDepartment() {
        return employeeDepartment;
    }
    public void setEmployeeDepartment(String employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }
    public String getEmployeeType() {
        return employeeType;
    }
    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }
    public String getEmployeeEmailId() {
        return employeeEmailId;
    }
    public void setEmployeeEmailId(String employeeEmailId) {
        this.employeeEmailId = employeeEmailId;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    @Override
    public String toString() {
        return "Employee ID : " + employeeId + "\nEmployee Name : " + employeeName + "\nEmployee Department : " + employeeDepartment + "\nEmployee Type : " + employeeType + "\nEmployee Email Id : " + employeeEmailId;
    }


}
