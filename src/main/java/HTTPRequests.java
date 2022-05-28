import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.*;


public class HTTPRequests {

    public static void main(String[] args) throws JSONException, IOException, InterruptedException {

        RequestBody body = new RequestBody();
        String loginRequestBody = body.loginData("login", "chris", "chris");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://cgi.tu-harburg.de/~ckh1694/index.php"))
                .POST(HttpRequest.BodyPublishers.ofString(loginRequestBody))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        JSONObject obj = new JSONObject(response.body());
        String username = obj.getJSONObject("data").getJSONObject("Agent").getString("Username");
        String token = obj.getJSONObject("data").getString("Token");
        System.out.println(token);
    }
}
