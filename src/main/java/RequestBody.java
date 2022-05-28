import org.json.JSONObject;

public class RequestBody {

    public RequestBody() {};

    public String loginData(String cmd, String username, String password) {
        JSONObject requestBody = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("Username", username);
        data.put("Password", password);
        requestBody.put("Cmd",cmd);
        requestBody.put("Data", data);
        return requestBody.toString();
    }

}
