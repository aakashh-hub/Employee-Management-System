import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@WebServlet("/notification")
public class NotificationServlet extends HttpServlet {
    private static final CopyOnWriteArrayList<PrintWriter> clients = new CopyOnWriteArrayList<>();
    private static final List<String> notificationHistory = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("into the do get in notification");
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        clients.add(out);
        for (String notification : notificationHistory) {
            out.write("data: " + notification + "\n\n");
        }

        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(30000);
                out.write(": keep-alive\n\n");
                out.flush();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void sendNotification(String message) {
        notificationHistory.add(message);
        for (PrintWriter client : clients) {
            client.write("data: " + message + "\n\n");
            client.flush();
        }
    }

    @Override
    public void destroy() {
        clients.clear();
    }
}
