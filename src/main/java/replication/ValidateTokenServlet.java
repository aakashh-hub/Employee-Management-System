package replication;

import availability.HealthCheck;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

@WebServlet("/ValidateToken")
public class ValidateTokenServlet extends HttpServlet {

    private Timer timer;
    private static final String SERVER2_URL = "http://localhost:8081/Server2";

    HealthCheck healthCheck = new HealthCheck();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private ReplicateDataToServer2 replicateDataToServer2 = new ReplicateDataToServer2();
    PaginationService paginationService = new PaginationService();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        System.out.println("entered into init ");
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("the data will be sent in a minute");
                System.out.println("time : " + timestamp);
                boolean isServer2Alive = healthCheck.isServerAlive(SERVER2_URL);
                if(isServer2Alive) {
                    paginationService.synchronizeAllEntities(Timestamp.valueOf(dateFormat.format(timestamp)));

                    timestamp = new Timestamp(System.currentTimeMillis());
                    System.out.println("timestamp after data is sent : " + timestamp);
                }
            }
        }, 60000, 60000);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String receivedToken = new BufferedReader(new InputStreamReader(request.getInputStream()))
                .readLine();
        replicateDataToServer2.setToken(receivedToken);
        System.out.println("token received and sent");

        response.setContentType("text/plain");
        response.getWriter().write(receivedToken);
    }
}