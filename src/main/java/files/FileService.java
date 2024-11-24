package files;

import dao.DatabaseConfig;
import entity.FileDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileService {
    public List<FileDetails> getFiles(int employeeId) {
        try (Connection conn = DatabaseConfig.getConnection()) {
            String sql = "SELECT file_name, file_path, employee_id FROM files WHERE employee_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            List<FileDetails> files = new ArrayList<>();
            while (rs.next()) {
                FileDetails file = new FileDetails(rs.getString("file_name"), rs.getString("file_path"), rs.getInt("employee_id"));
                files.add(file);
            }

            return files;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
