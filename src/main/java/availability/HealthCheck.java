package availability;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

@WebServlet("/healthcheck")
public class HealthCheck extends HttpServlet {

    DowntimeHelper downtimeHelper = new DowntimeHelper();
    private static final String SERVER2_URL = "http://localhost:8081/Server2";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static Timestamp timestamp;
    private Timer timer;
    PaginationService paginationService = new PaginationService();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                performHealthCheck();
            }
        }, 0, 90000);
    }

    private void performHealthCheck() {
        boolean isServer2Up = isServerAlive(SERVER2_URL);
        timestamp = new Timestamp(System.currentTimeMillis());

        if (isServer2Up) {
            paginationService.synchronizeAllEntities(downtimeHelper.getLastDownTimestamp());
            System.out.println("server 2 last time down : "  + downtimeHelper.getLastDownTimestamp());

            downtimeHelper.resetLastDownTimestamp();
        } else {
            downtimeHelper.updateLastDownTimestamp(Timestamp.valueOf(dateFormat.format(timestamp)));
            System.out.println("Time Server 2 went down: " + Timestamp.valueOf(dateFormat.format(timestamp)));
        }
        System.out.println("Health check completed.");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        performHealthCheck();
    }

    public boolean isServerAlive(String serverUrl) {
        try {
            URL url = new URL(serverUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            int responseCode = connection.getResponseCode();
            return responseCode == 200;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
