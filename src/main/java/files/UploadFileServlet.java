package files;

import dao.DatabaseConfig;
import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/UploadFileServlet")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 10  //10mb
)
public class UploadFileServlet extends HttpServlet {
    private static final String UPLOAD_DIR = "uploads";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String employeeId = request.getParameter("employeeId");
        String fileName = request.getParameter("fileName");

        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        Part filePart = request.getPart("file");
        if (filePart != null) {
            String filePath = "uploads" + File.separator + fileName;
            filePart.write(uploadPath + File.separator + fileName);

            try (Connection conn = DatabaseConfig.getConnection()) {
                String sql = "INSERT INTO files (file_name, file_path, employee_id) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, fileName);
                stmt.setString(2, filePath);
                stmt.setInt(3, Integer.parseInt(employeeId));
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        response.sendRedirect("menu.jsp");
    }
}