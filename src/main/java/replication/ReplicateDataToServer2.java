package replication;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ReplicateDataToServer2 {

    static String token;

    public void sendDataToServer2(String jsonData) {
        try {
            URL url = new URL("http://localhost:8081/Server2/authsync");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", token);

            System.out.println("data sent: " + jsonData);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonData.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Data sent successfully to Server 1");
            } else {
                System.out.println("Failed to send data: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error while sending data to Server1: " + e.getMessage());
        }
    }

    public void setToken(String token) {
        this.token = token;
    }
}
