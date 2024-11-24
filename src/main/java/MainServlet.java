import entity.*;
import files.FileService;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import services.DepartmentService;
import services.EmployeeService;
import services.EmployeeTypeService;
import services.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {

    EmployeeService employeeService = new EmployeeService();
    DepartmentService departmentService = new DepartmentService();
    EmployeeTypeService employeeTypeService = new EmployeeTypeService();
    UserService userService = new UserService();
    FileService fileService = new FileService();
    OTPMailer otpMailer = new OTPMailer();
    private static String OTP;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");

        if (!isAuthorizedAction(action) && (session == null || session.getAttribute("username") == null)) {
            response.sendRedirect("employeeLogin.jsp");
            return;
        }

        switch (action) {
            case "Sign out":
                if (session != null) {
                    session.removeAttribute("username");
                    session.invalidate();
                }
                response.sendRedirect("employeeLogin.jsp");
                break;
            case "Add Employee":
//                if(session != null && session.getAttribute("role") != null && session.getAttribute("role").equals("Admin") && session.getAttribute("otp").equals(OTP)) {
                    List<Department> departmentList = departmentService.getDepartmentDetails();
                    List<EmployeeType> employeeTypeList = employeeTypeService.getEmployeeTypeDetails();
                    request.setAttribute("departments", departmentList);
                    request.setAttribute("employeeTypes", employeeTypeList);
                    request.getRequestDispatcher("addEmployee.jsp").forward(request, response);
//                }
                break;
            case "View Employees":
                List<Employee> employees = employeeService.getEmployeeDetails();
                request.setAttribute("employees", employees);
                request.getRequestDispatcher("viewEmployees.jsp").forward(request, response);
                break;
            case "Delete Employees":
                List<Employee> employeeList = employeeService.getEmployeeDetails();
                request.setAttribute("employee", employeeList);
                request.getRequestDispatcher("deleteEmployee.jsp").forward(request, response);
                break;
            case "Update Employees":
                List<Employee> employeeListUpdate = employeeService.getEmployeeDetails();
                List<Department> departmentListUpdate = departmentService.getDepartmentDetails();
                List<EmployeeType> employeeTypeListUpdate = employeeTypeService.getEmployeeTypeDetails();
                request.setAttribute("employees", employeeListUpdate);
                request.setAttribute("departments", departmentListUpdate);
                request.setAttribute("employeeTypes", employeeTypeListUpdate);
                request.getRequestDispatcher("updateEmployee.jsp").forward(request, response);
                break;
            case "Add Department":
                request.getRequestDispatcher("addDepartment.jsp").forward(request, response);
                break;
            case "View Departments":
                List<Department> departments = departmentService.getDepartmentDetails();
                request.setAttribute("departments", departments);
                request.getRequestDispatcher("viewDepartments.jsp").forward(request, response);
                break;
            case "Delete Department Detail":
                List<Department> departmentDetails = departmentService.getDepartmentDetails();
                request.setAttribute("departments", departmentDetails);
                request.getRequestDispatcher("deleteDepartment.jsp").forward(request, response);
                break;
            case "Add Employee Type":
                request.getRequestDispatcher("addEmployeeType.jsp").forward(request, response);
                break;
            case "View Employee Types":
                List<EmployeeType> employeeTypeDetailss = employeeTypeService.getEmployeeTypeDetails();
                request.setAttribute("employeeTypes", employeeTypeDetailss);
                request.getRequestDispatcher("viewEmployeeTypes.jsp").forward(request, response);
                break;
            case "Delete Employee Types":
                List<EmployeeType> employeeTypeListToDelete = employeeTypeService.getEmployeeTypeDetails();
                request.setAttribute("employeeTypes", employeeTypeListToDelete);
                request.getRequestDispatcher("deleteEmployeeType.jsp").forward(request, response);
                break;
            case "View Details":
                System.out.println("view details");
                if (session != null && session.getAttribute("username") != null) {
                    String username = (String) session.getAttribute("username");

                    Employee employee = employeeService.getEmployeeByUsername(username);
                    System.out.println("employee : " + employee);
                    request.setAttribute("employee", employee);

                    request.getRequestDispatcher("viewEmployeeDetails.jsp").forward(request, response);
                } else {
                    response.sendRedirect("employeeLogin.jsp");
                }
                break;
            case "Update Details":
                System.out.println("updating employee details");
                if (session != null && session.getAttribute("username") != null) {
                    String username = (String) session.getAttribute("username");

                    Employee employee = employeeService.getEmployeeByUsername(username);
                    request.setAttribute("employee", employee);

                    request.getRequestDispatcher("updateIndividualEmployee.jsp").forward(request, response);
                } else {
                    response.sendRedirect("employeeLogin.jsp");
                }
                break;
            case "Reset Password":
                if(session != null && session.getAttribute("username") != null) {
                    System.out.println("pass");
                    response.sendRedirect("resetPassword.jsp");
                }
                else {
                    response.sendRedirect("employeeLogin.jsp");
                }
                break;
            case "View Notifications":
                if(session != null && session.getAttribute("username") != null) {
                    response.sendRedirect("employeeNotifications.jsp");
                }
                break;
            case "Upload Files":
                if(session != null && session.getAttribute("username") != null) {
                    response.sendRedirect("uploadFile.jsp");
                }
                break;
            case "View Files":
                if(session != null && session.getAttribute("username") != null) {
                    Employee employee = employeeService.getEmployeeByUsername((String)session.getAttribute("username"));
                    List<FileDetails> fileDetails = fileService.getFiles(employee.getEmployeeId());
                    request.setAttribute("files", fileDetails);
                    request.getRequestDispatcher("viewFiles.jsp").forward(request, response);
                }
                break;
            default:
                if (session != null && session.getAttribute("username") != null) {
//                    if(!OTP.isEmpty()) {
                        response.sendRedirect("menu.jsp");
//                    } else {
//                        response.sendRedirect("employeeMenu.jsp");
//                    }
                } else {
                    System.out.println("get req");
                    response.sendRedirect("employeeLogin.jsp");
                }
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");

        if ("Log in".equals(action)) {
            handleLogin(request, response);
        }

        switch (action) {
            case "Verify OTP":
                String enteredOtp = request.getParameter("otp");
                String sessionOtp = (String) session.getAttribute("otp");

                if (sessionOtp != null && sessionOtp.equals(enteredOtp)) {
                    System.out.println("success");
                    response.sendRedirect("menu.jsp");
                } else {
                    response.sendRedirect("employeeLogin.jsp");
                }
                break;
            case "Add Employee":
                String name = request.getParameter("name");
                String department = request.getParameter("department");
                String type = request.getParameter("type");
                String email = request.getParameter("email");
                int id = employeeService.generateEmployeeId();
                String number = request.getParameter("number");
                Employee employee = new Employee(id, name, department, type, email, number);
                employeeService.addEmployee(employee);

                response.sendRedirect("MainServlet?action=");
                break;
            case "Add Department":
                int departmentId = departmentService.generateDepartmentId();
                String departmentName = request.getParameter("department");
                Department departments = new Department(departmentId, departmentName);
                departmentService.addDepartment(departments);

                response.sendRedirect("MainServlet?action=");
                break;
            case "Add Employee Type":
                int employeeTypeId = employeeTypeService.generateEmployeeTypeId();
                String employeeTypeName = request.getParameter("employeeType");
                EmployeeType employeeType = new EmployeeType(employeeTypeId, employeeTypeName);
                employeeTypeService.addEmployeeType(employeeType);
                response.sendRedirect("MainServlet?action=");
                break;
            case "Delete Employees":
                String empIdToDelete = request.getParameter("employee");
                employeeService.deleteEmployee(empIdToDelete);

                response.sendRedirect("MainServlet?action=");
                break;
            case "Delete Department Detail":
                String departmentToDelete = request.getParameter("department");
                departmentService.deleteFromDepartmentDetails(departmentToDelete);

                response.sendRedirect("MainServlet?action=");
                break;
            case "Delete Employee Types":
                String employeeTypeToDelete = request.getParameter("employeeType");
                employeeTypeService.deleteEmployeeType(employeeTypeToDelete);

                response.sendRedirect("MainServlet?action=");
                break;
            case "Update Employees":
                String empId = request.getParameter("id");
                String deptUpdate = request.getParameter("department");
                String typeUpdate = request.getParameter("type");
                String emailUpdate = request.getParameter("email");
                String mobile = request.getParameter("number");
                employeeService.updateEmployee(empId, deptUpdate, typeUpdate, emailUpdate, mobile);

                response.sendRedirect("MainServlet?action=");
                break;
            case "Save Updated Details":
                if (session != null && session.getAttribute("username") != null) {

                    System.out.println("employee updated data is pushed");
                    String username = (String) session.getAttribute("username");
                    String updatedName = request.getParameter("employeeName");
                    String employeeNumber = request.getParameter("employeeNumber");

                    employeeService.updateIndividualEmployeeDetails(username, updatedName, employeeNumber);
                    Employee employee1 = employeeService.getEmployeeByUsername(username);

                    String notification = employee1.getEmployeeName() + " has updated their details.";
                    NotificationServlet.sendNotification(notification);

                    request.getRequestDispatcher("employeeMenu.jsp").forward(request, response);
                } else {
                    response.sendRedirect("employeeLogin.jsp");
                }
                break;
            case "Reset Password":
                if (session != null && session.getAttribute("username") != null) {
                    String oldPassword = request.getParameter("oldPassword");
                    String newPassword = request.getParameter("newPassword");

                    String username = (String) session.getAttribute("username");
                    User user = userService.getUserByUsername(username);
                    if (user.getPassword().equals(oldPassword)) {
                        user.setPassword(newPassword);
                        userService.updateUserPassword(username, newPassword);

                        Employee employee1 = employeeService.getEmployeeByUsername(username);

                        String notification = employee1.getEmployeeName() + " has reset their password.";
                        NotificationServlet.sendNotification(notification);

                        request.setAttribute("message", "Password successfully updated!");
                        System.out.println("password reset success");
                    } else {
                        request.setAttribute("message", "Old password is incorrect");
                    }
                } else {
                    request.setAttribute("message", "Employee not found.");
                }
                request.getRequestDispatcher("resultPage.jsp").forward(request, response);
                break;
            default:
                if (session != null && session.getAttribute("username") != null) {
                    if(!OTP.isEmpty()) {
                        response.sendRedirect("menu.jsp");
                    } else {
                        response.sendRedirect("employeeMenu.jsp");
                    }
                } else {
                    System.out.println("post req");
                    response.sendRedirect("employeeLogin.jsp");
                }
                break;
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String passwd = request.getParameter("password");

        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("username", username);

        if (userService.isUserValid(new User(username, passwd))) {
            String role = userService.getUserRole(username);
            if(role.equals("Admin")) {
                /*final Dotenv dotenv = Dotenv.load();
                String otp = otpMailer.generateOTP();
                newSession.setAttribute("otp", otp);
                newSession.setAttribute("role", role);
                OTP = otp;

                String userEmail = dotenv.get("RECIPIENT_EMAIL");
                otpMailer.sendOTP(userEmail, otp);

                System.out.println("otp sent successfully");
                request.getRequestDispatcher("verifyOTP.jsp").forward(request, response);*/
                request.getRequestDispatcher("menu.jsp").forward(request, response);
            }
            else {
                newSession.setAttribute("role", role);
                request.getRequestDispatcher("employeeMenu.jsp").forward(request, response);
            }
        } else {
            request.getRequestDispatcher("employeeLogin.jsp").forward(request, response);
        }
    }

    private boolean isAuthorizedAction(String action) {
        return "Log in".equals(action);
    }
}