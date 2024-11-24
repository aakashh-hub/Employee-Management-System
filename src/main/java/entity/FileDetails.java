package entity;

public class FileDetails {
    private String fileName;
    private String filePath;
    private int employeeId;

    public FileDetails(String fileName, String filePath, int employeeId) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.employeeId = employeeId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
