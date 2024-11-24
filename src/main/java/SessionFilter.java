import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/MainServlet")
public class SessionFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("session filter");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        String requestPath = httpRequest.getRequestURI();

        //((HttpServletResponse) response).setHeader("Cache-Control", "no-cache, no-store, must-revalidate");

        boolean isPublicResource = requestPath.endsWith("employeeLogin.jsp");

        boolean isLoginAction = httpRequest.getParameter("action") != null &&
                (httpRequest.getParameter("action").equals("Log in"));

        boolean isSyncRequest = requestPath.endsWith("/sync");

        if (isPublicResource || isLoginAction || isSyncRequest) {
            chain.doFilter(request, response);
            return;
        }

        if (session == null || session.getAttribute("username") == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/employeeLogin.jsp");
            return;
        }

        chain.doFilter(request, response);
    }
}