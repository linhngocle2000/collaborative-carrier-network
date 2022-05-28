package UIResource.HTTPResource;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import org.json.*;


public class HTTPRequests {

    private static String dbURL = "https://cgi.tu-harburg.de/~ckh1694/index.php";

    public static Entry<String,Boolean> login(String username, String password) throws JSONException, IOException, InterruptedException {

        String loginRequestBody = RequestBody.loginData(username, password);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dbURL))
                .POST(HttpRequest.BodyPublishers.ofString(loginRequestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        JSONObject obj = new JSONObject(response.body());
        boolean success = obj.getBoolean("success");
        if (success) {
            String name = obj.getJSONObject("data").getJSONObject("Agent").getString("Name");
            Boolean isAuctioneer = obj.getJSONObject("data").getJSONObject("Agent").getBoolean("IsAuctioneer");
            return new SimpleEntry<>(name,isAuctioneer);
        }
        return null;
    }

    public static boolean register(String name, String username, String password, boolean isAuctioneer)
            throws JSONException, IOException, InterruptedException {

        String registerRequestBody = RequestBody.registerData(name, username, password, isAuctioneer);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dbURL))
                .POST(HttpRequest.BodyPublishers.ofString(registerRequestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        JSONObject obj = new JSONObject(response.body());
        return obj.getBoolean("success");
    }
}
