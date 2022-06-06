package UIResource.HTTPResource;

import org.json.JSONObject;

import Agent.Agent;

public class RequestBody {

    // Agent

    public static String login(String username, String password) {
        JSONObject data = new JSONObject();
        data.put("Username", username);
        data.put("Password", password);
        return body("login", null, data);
    }

    public static String register(String name, String username, String password, boolean isAuctioneer) {
        JSONObject data = new JSONObject();
        data.put("Username", username);
        data.put("Name", name);
        data.put("Password", password);
        data.put("IsAuctioneer", isAuctioneer);
        return body("register", null, data);
    }

    public static String getAgent(String username, String token) {
        JSONObject data = new JSONObject();
        data.put("Username", username);
        return body("getAgent", token, data);
    }

    public static String getAllAgents(String token) {
        return body("getAgents", token, null);
    }

    public static String getCarrierAgents(String token) {
        return body("getCarriers", token, null);
    }

    public static String getAuctioneerAgents(String token) {
        return body("getAuctioneers", token, null);
    }

    // Transport request

    public static String addTransportRequest(Agent agent, float pickX, float pickY, float delX, float delY, String token) {
        JSONObject data = new JSONObject();
        data.put("Agent", agent.getUsername());
        data.put("PickupLat", pickX);
        data.put("PickupLon", pickY);
        data.put("DeliveryLat", delX);
        data.put("DeliveryLon", delY);
        return body("addRequest", token, data);
    }

    public static String getAllTransportRequests(String token) {
        return body("getRequests", token, null);
    }

    // Helper

    private static String body(String command, String token, JSONObject data) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("Cmd", command);
        if (token != null) {
            requestBody.put("Token", token);
        }
        if (data != null) {
            requestBody.put("Data", data);
        }
        return requestBody.toString();
    }

}
