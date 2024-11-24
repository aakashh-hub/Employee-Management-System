package availability;

import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@WebServlet("/sync")
public class DataUpdateServletWhenTurnedUp extends HttpServlet {

    DataProcessorWhenTurnedUp dataProcessorWhenTurnedUp = new DataProcessorWhenTurnedUp();
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("sync servlet");
        BufferedReader reader = request.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String jsonData = jsonBuilder.toString();

        System.out.println("JSON  data : " + jsonData);

        Gson gson = new Gson();
        Map<String, Object> updatedData = gson.fromJson(jsonData, Map.class);

        System.out.println("Updated data : " + updatedData);

        dataProcessorWhenTurnedUp.processUpdatedData(updatedData);
        System.out.println("process done");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
