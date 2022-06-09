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

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.job.Shipment;

import Agent.Agent;
import Agent.AgentFactory;
import Agent.AuctioneerAgent;
import Agent.CarrierAgent;
import Auction.TransportRequest;

/**
 * Contains all requests to the server backend.
 * If a request fails, null or in some cases false is returned by the function.
 * The specific exception can be retrieved by calling getLastError().
 */
public class HTTPRequests {

    // Agent

    public static boolean registerAuctioneer(String name, String username, String password) {
        try {
            var json = send(RequestBody.registerAuctioneer(name, username, password));
            var success = json.getBoolean("success");
            if (!success) {
                JSONObject error = json.getJSONObject("error");
                lastError = new Exception(error.getString("message"));
            }
            return success;
        } catch (IOException | InterruptedException e) {
            lastError = e;
            e.printStackTrace();
            return false;
        }
    }

    public static boolean registerCarrier(String name, String username, String password, float depotX, float depotY) {
        try {
            var json = send(RequestBody.registerCarrier(name, username, password, depotX, depotY));
            var success = json.getBoolean("success");
            if (!success) {
                JSONObject error = json.getJSONObject("error");
                lastError = new Exception(error.getString("message"));
            }
            return success;
        } catch (IOException | InterruptedException e) {
            lastError = e;
            e.printStackTrace();
            return false;
        }
    }

    public static Agent login(String username, String password) {
        try {
            var json = send(RequestBody.login(username, password));
            boolean success = json.getBoolean("success");
            if (!success) {
                return null;
            }

            var data = json.getJSONObject("data");
            token = data.getString("Token");
            Agent agent = AgentFactory.fromJSON(data.getJSONObject("Agent"));
            return agent;
        } catch (IOException | InterruptedException e) {
            lastError = e;
            return null;
        }
    }

    public static boolean logout() {
        boolean result = token != null;
        token = null;
        return result;
    }

    public static Agent getAgent(String username) {
        try {
            var json = send(RequestBody.getAgent(username, token));
            var agent = AgentFactory.fromJSON(json);
            return agent;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    public static List<Agent> getAllAgents() {
        try {
            var json = send(RequestBody.getAllAgents(token));
            var array = json.getJSONArray("data");
            List<Agent> result = new ArrayList<Agent>(array.length());
            array.forEach(obj -> result.add(AgentFactory.fromJSON((JSONObject)obj)));
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    public static List<CarrierAgent> getCarrierAgents() {
        try {
            var json = send(RequestBody.getCarrierAgents(token));
            var array = json.getJSONArray("data");
            List<CarrierAgent> result = new ArrayList<CarrierAgent>(array.length());
            array.forEach(obj -> result.add(AgentFactory.carrierFromJSON((JSONObject) obj)));
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    public static List<AuctioneerAgent> getAuctioneerAgents() {
        try {
            var json = send(RequestBody.getAuctioneerAgents(token));
            var array = json.getJSONArray("data");
            List<AuctioneerAgent> result = new ArrayList<AuctioneerAgent>(array.length());
            array.forEach(obj -> result.add(AgentFactory.auctioneerFromJSON((JSONObject) obj)));
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    // Transport request

    public static TransportRequest addTransportRequest(Agent agent, float pickupX, float pickupY, float deliveryX, float deliveryY) {
        try {
            var json = send(RequestBody.addTransportRequest(agent, pickupX, pickupY, deliveryX, deliveryY, token));
            int id = json.getInt("data");
            return new TransportRequest(id, agent, pickupX, pickupY, deliveryX, deliveryY);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    public static List<TransportRequest> getTransportRequestsOfAgent(Agent agent) {
        try {
            // Load requests
            var json = send(RequestBody.getTransportRequestsOfAgent(agent, token));
            var array = json.getJSONArray("data");
            List<TransportRequest> result = new ArrayList<TransportRequest>(array.length());
            array.forEach(obj -> {
                JSONObject j = (JSONObject)obj;
                if (j.getString("Owner").equals(agent.getUsername())) {
                    int id = j.getInt("ID");
                    float pickupX = j.getFloat("PickupLat");
                    float pickupY = j.getFloat("PickupLon");
                    float deliveryX = j.getFloat("DeliveryLat");
                    float deliveryY = j.getFloat("DeliveryLon");
                    result.add(new TransportRequest(id, agent, pickupX, pickupY, deliveryX, deliveryY));
                }
            });
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    public static List<Shipment> getUserTransportRequests(String username) {
        try {
            // Load requests
            var json = send(RequestBody.getAgent(username, token));
            var array = json.getJSONArray("data");
            List<Shipment> result = new ArrayList<Shipment>(array.length());
            array.forEach(obj -> {
                JSONObject j = (JSONObject)obj;
                String id = j.getString("ID");
                Location pickup = Location.newInstance(j.getFloat("PickupLat"), j.getFloat("PickupLon"));
                Location delivery = Location.newInstance(j.getFloat("DeliveryLat"), j.getFloat("DeliveryLon"));
                result.add(Shipment.Builder.newInstance(id).setPickupLocation(pickup).setDeliveryLocation(delivery).build());
            });
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    // Auction

    // Helper methods

    private static String dbURL = "https://cgi.tu-harburg.de/~ckh1694/index.php";
    private static String token = null;
    private static Exception lastError = null;

    public static Exception getLastError() {
        return lastError;
    }

    private static JSONObject send(String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(dbURL))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject obj = new JSONObject(response.body());
        return obj;
    }
}
