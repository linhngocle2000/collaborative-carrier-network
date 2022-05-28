import org.json.JSONObject;

public class RequestBody {

    public static String loginData(String username, String password) {
        JSONObject requestBody = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("Username", username);
        data.put("Password", password);
        requestBody.put("Cmd","login");
        requestBody.put("Data", data);
        return requestBody.toString();
    }

    public static String registerData(String name, String username, String password, boolean isAuctioneer) {
        JSONObject requestBody = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("Username", username);
        data.put("Name", name);
        data.put("Password", password);
        data.put("IsAuctioneer", isAuctioneer);
        requestBody.put("Cmd","register");
        requestBody.put("Data", data);
        return requestBody.toString();
    }

}
