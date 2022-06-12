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

    public static boolean registerCarrier(String name, String username, String password, float depotX, float depotY, float pickupBaserate, float externalTravelCost, float loadBaserate, float internalTravelCost) {
        try {
            var json = send(RequestBody.registerCarrier(name, username, password, depotX, depotY, pickupBaserate, externalTravelCost, loadBaserate, internalTravelCost));
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
            return AgentFactory.fromJSON(data.getJSONObject("Agent"));
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
            boolean success = json.getBoolean("success");
            if (!success) {
                return null;
            }
            return AgentFactory.fromJSON(json.getJSONObject("data"));
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
            List<Agent> result = new ArrayList<>(array.length());
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
            lastError = e;
            return null;
        }
    }

    public static List<AuctioneerAgent> getAuctioneerAgents() {
        try {
            var json = send(RequestBody.getAuctioneerAgents(token));
            var array = json.getJSONArray("data");
            List<AuctioneerAgent> result = new ArrayList<>(array.length());
            array.forEach(obj -> result.add(AgentFactory.auctioneerFromJSON((JSONObject) obj)));
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    // Transport request

    public static TransportRequest addTransportRequest(CarrierAgent agent, float pickupX, float pickupY, float deliveryX, float deliveryY) {
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

    public static List<TransportRequest> getTransportRequestsOfAgent(CarrierAgent agent) {
        try {
            // Load requests
            var json = send(RequestBody.getTransportRequestsOfAgent(agent, token));
            var array = json.getJSONArray("data");
            List<TransportRequest> result = new ArrayList<TransportRequest>(array.length());
            for(Object obj : array) {
                JSONObject j = (JSONObject) obj;
                int id = j.getInt("ID");
                float pickupX = j.getFloat("PickupLat");
                float pickupY = j.getFloat("PickupLon");
                float deliveryX = j.getFloat("DeliveryLat");
                float deliveryY = j.getFloat("DeliveryLon");
                result.add(new TransportRequest(id, agent, pickupX, pickupY, deliveryX, deliveryY));
            }
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    public static List<TransportRequest> getTransportRequestsOfAuction(Auction auction) {
        try {
            // Cache agents for multiple ownership
            HashMap<String, CarrierAgent> map = new HashMap<String, CarrierAgent>();

            // Load requests
            var json = send(RequestBody.getTransportRequestsOfAuction(auction, token));
            var array = json.getJSONArray("data");
            ArrayList<TransportRequest> result = new ArrayList<>(array.length());
            for (Object obj : array) {
                JSONObject j = (JSONObject) obj;
                int id = j.getInt("ID");
                String username = j.getString("Owner");
                float pickupX = j.getFloat("PickupLat");
                float pickupY = j.getFloat("PickupLon");
                float deliveryX = j.getFloat("DeliveryLat");
                float deliveryY = j.getFloat("DeliveryLon");

                CarrierAgent owner = null;
                if (map.containsKey(username)) {
                    owner = map.get(username);
                } else {
                    owner = (CarrierAgent)getAgent(username);
                    map.put(username, owner);
                }
                if (owner == null) {
                    lastError = new Exception("Could not retrieve owner of transport request");
                    return null;
                }
                result.add(new TransportRequest(id, owner, pickupX, pickupY, deliveryX, deliveryY));
            }
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    // Auction

    /**
     * Will return only auctions belonging to the user if the
     * user is an auctioneer agent. Otherwise, all auctions
     * will be returned.
     * @return List of auctions
     */
    public static List<Auction> getAuctions() {
        try {
            var json = send(RequestBody.getAuctions(token));
            var array = json.getJSONArray("data");
            var result = new ArrayList<Auction>();
            for (Object obj : array) {
                JSONObject j = (JSONObject)obj;
                int id = j.getInt("ID");
                String type = j.getString("Type");
                boolean isActive = j.getBoolean("IsActive");
                int iteration = j.getInt("Iteration");

                Auction auction = null;
                switch (type)
                {
                    case Auction.TYPE_VICKREY:
                        auction = new VickreyAuction(id, iteration);
                        break;

                    case Auction.TYPE_TRADITIONAL:
                        auction = new TraditionalAuction(id, iteration);
                        break;

                    default:
                        lastError = new Exception("Failed to parse auction");
                        return null;
                }

                List<TransportRequest> requests = getTransportRequestsOfAuction(auction);
                if (requests == null) {
                    // Last error already set by getTransportRequestsOfAuction
                    return null;
                }
                auction.setTransportRequests(requests);
                result.add(auction);
            }
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

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
        return new JSONObject(response.body());
    }
}
