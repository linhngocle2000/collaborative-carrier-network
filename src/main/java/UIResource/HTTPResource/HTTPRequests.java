package UIResource.HTTPResource;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.*;

import Agent.*;
import Auction.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Contains all requests to the server backend.
 * If a request fails, null or in some cases false is returned by the function.
 * The specific exception can be retrieved by calling getLastError().
 */
public class HTTPRequests {

    private static Logger LOGGER = LoggerFactory.getLogger(HTTPRequests.class);

    // Agent

    public static boolean registerCarrier(String name, String username, String password, float depotX, float depotY,
        float pickupBaserate, float externalTravelCost, float loadBaserate, float internalTravelCost) throws IOException, InterruptedException,JSONException {
        LOGGER.info("registerCarrier " + username);
        var json = send(RequestBody.registerCarrier(name, username, password, depotX, depotY, pickupBaserate,
                externalTravelCost, loadBaserate, internalTravelCost));
        var success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
        }
        return success;
    }

    public static Agent login(String username, String password) throws IOException, InterruptedException,JSONException {
        LOGGER.info("login " + username);
        var json = send(RequestBody.login(username, password));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
            return null;
        }
        var data = json.getJSONObject("data");
        token = data.getString("Token");
        return AgentFactory.fromJSON(data.getJSONObject("Agent"));
    }

    public static boolean logout() {
        boolean result = token != null;
        token = null;
        return result;
    }

    public static CarrierAgent getAgent(String username) throws IOException, InterruptedException, JSONException{
        LOGGER.info("getAgent " + username);
        var json = send(RequestBody.getAgent(username, token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
            return null;
        }
        return AgentFactory.carrierFromJSON(json.getJSONObject("data"));
    }

    public static List<Agent> getAllAgents() {
        try {
            var json = send(RequestBody.getAllAgents(token));
            var array = json.getJSONArray("data");
            List<Agent> result = new ArrayList<>(array.length());
            array.forEach(obj -> result.add(AgentFactory.fromJSON((JSONObject) obj)));
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    public static List<CarrierAgent> getCarrierAgents() throws IOException, InterruptedException, JSONException {
        LOGGER.info("getCarrierAgents");
        var json = send(RequestBody.getCarrierAgents(token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
            return null;
        }
        var array = json.getJSONArray("data");
        List<CarrierAgent> result = new ArrayList<>(array.length());
        array.forEach(obj -> result.add(AgentFactory.carrierFromJSON((JSONObject) obj)));
        return result;
    }


    // Transport request

    public static TransportRequest addTransportRequest(CarrierAgent agent, float pickupX, float pickupY,
            float deliveryX, float deliveryY) throws IOException, InterruptedException, JSONException {
        LOGGER.info("addTransportRequest " + agent.getUsername());
        var json = send(RequestBody.addTransportRequest(agent, pickupX, pickupY, deliveryX, deliveryY, token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
            return null;
        }
        int id = json.getInt("data");
        return new TransportRequest(id, agent, pickupX, pickupY, deliveryX, deliveryY);
    }

    public static List<TransportRequest> getTransportRequestsOfAgent(CarrierAgent agent) throws Exception {
            // Load requests
        LOGGER.info("getTransportRequestsOfAgent " + agent.getUsername());
        var json = send(RequestBody.getTransportRequestsOfAgent(agent, token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
            return null;
        }
        var array = json.getJSONArray("data");
        List<TransportRequest> result = new ArrayList<>(array.length());
        for (Object obj : array) {
            JSONObject j = (JSONObject) obj;
            var request = TransportRequest.parse(j);
            request.setOwner(agent);
            result.add(request);
        }
        return result;
    }

    public static List<TransportRequest> getTransportRequestsOfAuction(Auction auction) throws Exception {
        LOGGER.info("getTransportRequestsOfAuction " + auction.getID());
        // Cache agents for multiple ownership
        HashMap<String, CarrierAgent> map = new HashMap<>();

        // Load requests
        var json = send(RequestBody.getTransportRequestsOfAuction(auction, token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
            return null;
        }
        var array = json.getJSONArray("data");
        ArrayList<TransportRequest> result = new ArrayList<>(array.length());
        for (Object obj : array) {
            JSONObject j = (JSONObject) obj;
            String username = j.getString("Owner");
            var request = TransportRequest.parse(j);

            CarrierAgent owner;
            if (map.containsKey(username)) {
                owner = map.get(username);
            } else {
                owner = getAgent(username);
                map.put(username, owner);
            }
            if (owner == null) {
                throw new Exception("Could not retrieve owner of transport request");
            }
            request.setOwner(owner);
            result.add(request);
        }
        return result;
    }

    /**
     * Set cost of all transport requests to 0
     */
    public static boolean resetCost() throws IOException, InterruptedException, JSONException {
        LOGGER.info("resetCost");
        var json = send(body("resetCost", token, null));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
        }
        return success;
    }

    /**
     * Reset auction table
     * @param auction If null, the entire auction table will be deleted
     */
    public static boolean resetAuction(Auction auction) throws IOException, InterruptedException, JSONException {
        LOGGER.info("resetAuction");
        JSONObject data = null;
        if (auction != null)
        {
            data = new JSONObject();
            data.put("Auction", auction.getID());
        }
        var json = send(body("resetAuction", token, data));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
        }
        return success;
    }

    /**
     * Saves all transport requests in a stash.
     * The last stash is deleted before the new stash is created.
     */
    public static boolean stashTransportRequests() throws IOException, InterruptedException, JSONException {
        LOGGER.info("stashTransportRequests");
        var json = send(body("stashRequests", token, null));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
        }
        return success;
    }

    /**
     * Returns the stashed transport requests
     * 
     * @param agent If set this function will only return stashed transport requests
     *              that belong to this agent. If null all stashed transport
     *              requests are returned.
     */
    public static List<TransportRequest> getStashedTransportRequests(CarrierAgent agent) throws Exception {
        LOGGER.info("getStashedTransportRequests " + agent.getUsername());
        // Cache agents for multiple ownership
        HashMap<String, CarrierAgent> map = new HashMap<>();

        // Prepare data
        JSONObject data = null;
        data = new JSONObject();
        data.put("Agent", agent.getUsername());

        // Load requests
        var json = send(body("getStashedRequests", token, data));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
            return null;
        }
        var array = json.getJSONArray("data");
        ArrayList<TransportRequest> result = new ArrayList<>(array.length());
        for (Object obj : array) {
            JSONObject j = (JSONObject) obj;
            String username = j.getString("Owner");
            var request = TransportRequest.parse(j);

            CarrierAgent owner;
            owner = agent;
            request.setOwner(owner);
            result.add(request);
        }
        return result;
    }

    // Auction

    public static Auction addAuction() throws IOException, InterruptedException, JSONException {
        LOGGER.info("addAuction");
        var json = send(RequestBody.addAuction(token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
            return null;
        }
        int id = json.getInt("data");
        return new Auction(id, 0);
    }

    /**
     * @return All auctions whether they are active or not
     */
    public static List<Auction> getAllAuctions() throws Exception {
        LOGGER.info("getAllAuctions");
        var json = send(RequestBody.getAuctions(token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
            return null;
        }
        var array = json.getJSONArray("data");
        var result = new ArrayList<Auction>();
        for (Object obj : array) {
            JSONObject j = (JSONObject) obj;
            int id = j.getInt("ID");
            boolean isActive = j.getBoolean("IsActive");
            int iteration = j.getInt("Iteration");
            Auction auction = new Auction(id, iteration);
            List<TransportRequest> requests = getTransportRequestsOfAuction(auction);
            if (requests == null) {
                // Last error already set by getTransportRequestsOfAuction
                return null;
            }
            auction.setTransportRequests(requests);
            result.add(auction);
        }
        return result;
    }


    public static boolean addTransportRequestToAuction(Auction auction, TransportRequest request) throws IOException, InterruptedException, JSONException {
        LOGGER.info("addTransportRequestToAuction " + auction.getID());
        var json = send(RequestBody.addRequestToAuction(auction, request, token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
        }
        return success;
    }

    public static Bid addBid(Auction auction, CarrierAgent agent, double price) throws IOException, InterruptedException, JSONException {
        LOGGER.info("addBid " + auction.getID());
        JSONObject data = new JSONObject();
        data.put("Auction", auction.getID());
        data.put("Agent", agent.getUsername());
        data.put("Price", price);
        var json = send(body("addBid", token, data));
        boolean result = json.getBoolean("success");
        if (!result) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
            return null;
        }
        int id = json.getInt("data");
        return new Bid(id, auction, agent, price);
    }

    public static List<Bid> getBids(Auction auction) throws IOException, InterruptedException, JSONException {
        LOGGER.info("getBids " + auction.getID());
        // Cache agents for multiple bids by same agent
        HashMap<String, CarrierAgent> map = new HashMap<>();

        var json = send(RequestBody.getBids(auction, token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
            return null;
        }
        var array = json.getJSONArray("data");
        var result = new ArrayList<Bid>();
        for (Object obj : array) {
            JSONObject j = (JSONObject) obj;
            int id = j.getInt("ID");
            String username = j.getString("Agent");
            int price = j.getInt("Price");
            CarrierAgent bidder;
            if (map.containsKey(username)) {
                bidder = map.get(username);
            } else {
                bidder = getAgent(username);
                map.put(username, bidder);
            }
            if (bidder == null) {
                String message = "Could not retrieve bidder";
                lastError = new Exception(message);
                LOGGER.warn(message);
                return null;
            }
            result.add(new Bid(id, auction, bidder, price));
        }
        return result;
    }

    public static boolean startAuction(Auction auction) throws IOException, InterruptedException, JSONException {
        LOGGER.info("startAuction " + auction.getID());
        var json = send(RequestBody.startAuction(auction, token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
        }
        return success;
    }

    public static boolean endAuction(Auction auction) throws IOException, InterruptedException, JSONException {
        LOGGER.info("endAuction " + auction.getID());
        var json = send(RequestBody.endAuction(auction, token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
        }
        return success;
    }

    public static boolean setWinner(Auction auction, Bid bid) throws IOException, InterruptedException, JSONException {
        LOGGER.info("setWinner " + auction.getID());
        var json = send(RequestBody.setWinner(auction, bid, token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
        }
        return success;
    }

    public static boolean addMinProfitToBid(CarrierAgent carrier, double price) throws IOException, InterruptedException, JSONException {
        LOGGER.info("addMinProfitToBid " + carrier.getUsername());
        var json = send(RequestBody.addMinProfitToBid(carrier, price, token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
        }
        return success;
    }

    public static boolean addMaxProfitToAuctionOff(CarrierAgent carrier, double price) throws IOException, InterruptedException, JSONException {
        LOGGER.info("addMaxProfitToAuctionOff " + carrier.getUsername());
        var json = send(RequestBody.addMaxProfitToAuctionOff(carrier, price, token));
        boolean success = json.getBoolean("success");
        if (!success) {
            JSONObject error = json.getJSONObject("error");
            lastError = new Exception(error.getString("message"));
            LOGGER.warn(error.getString("message"));
        }
        return success;
    }

    // Helper methods

    private static String dbURL = "https://cgi.tu-harburg.de/~ckh1694/index.php";
    private static String token = null;
    private static Exception lastError = null;

    public static Exception getLastError() {
        return lastError;
    }

    private static String body(String command, String token, Object data) {
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

    private static JSONObject send(String body) throws IOException, InterruptedException,JSONException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dbURL))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var json = new JSONObject(response.body());
        if (!json.getBoolean("success")) {
            var error = json.getJSONObject("error");
            if (error.has("message")) {
                String message = error.getString("message");
                lastError = new Exception(message);
                LOGGER.warn(error.getString("message"));
            }
            if (error.has("stacktrace")) {
                LOGGER.warn(error.getString("stacktrace"));
            }
        }
        return json;
    }
}
