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

    public static Auction addAuction() {
        try {
            var json = send(RequestBody.addAuction(token));
            boolean success = json.getBoolean("success");
            if (!success) {
                JSONObject error = json.getJSONObject("error");
                lastError = new Exception(error.getString("message"));
                return null;
            }
            int id = json.getJSONObject("data").getInt("Auction");
            return new Auction(id, 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    /**
     * @return All auctions whether they are active or not
     */
    public static List<Auction> getAllAuctions() {
        try {
            var json = send(RequestBody.getAuctions(token));
            boolean success = json.getBoolean("success");
            if (!success) {
                JSONObject error = json.getJSONObject("error");
                lastError = new Exception(error.getString("message"));
                return null;
            }
            var array = json.getJSONArray("data");
            var result = new ArrayList<Auction>();
            for (Object obj : array) {
                JSONObject j = (JSONObject)obj;
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    public static boolean startAuction(Auction auction) {
        try {
            var json = send(RequestBody.startAuction(auction, token));
            return json.getBoolean("success");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return false;
        }
    }

    public static boolean endAuction(Auction auction) {
        try {
            var json = send(RequestBody.endAuction(auction, token));
            return json.getBoolean("success");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return false;
        }
    }

    /**
     * @return Only auctions that are active (open for bidding).
     */
    public static List<Auction> getActiveAuctions() {
        try {
            var json = send(RequestBody.getAuctions(token));
            boolean success = json.getBoolean("success");
            if (!success) {
                JSONObject error = json.getJSONObject("error");
                lastError = new Exception(error.getString("message"));
                return null;
            }
            var array = json.getJSONArray("data");
            var result = new ArrayList<Auction>();
            for (Object obj : array) {
                JSONObject j = (JSONObject) obj;
                boolean isActive = j.getBoolean("IsActive");
                if (!isActive) {
                    continue;
                }
                int id = j.getInt("ID");
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    public static boolean addTransportRequestToAuction(Auction auction, TransportRequest request) {
        try {
            var json = send(RequestBody.addRequestToAuction(auction, request, token));
            boolean result = json.getBoolean("success");
            return result;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return false;
        }
    }

    public static Bid addBid(Auction auction, CarrierAgent agent, int price) {
        try {
            var json = send(RequestBody.addBid(auction, price, token));
            boolean result = json.getBoolean("success");
            if (!result) {
                return null;
            }
            int id = json.getJSONObject("data").getInt("ID");
            return new Bid(id, auction, agent, price);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            lastError = e;
            return null;
        }
    }

    public static List<Bid> getBids(Auction auction) {
        try {
            // Cache agents for multiple bids by same agent
            HashMap<String, CarrierAgent> map = new HashMap<String, CarrierAgent>();

            var json = send(RequestBody.getBids(auction, token));
            boolean success = json.getBoolean("success");
            if (!success) {
                return null;
            }
            var array = json.getJSONArray("data");
            var result = new ArrayList<Bid>();
            for (Object obj : array) {
                JSONObject j = (JSONObject) obj;
                int id = j.getInt("ID");
                String username = j.getString("Agent");
                int price = j.getInt("Price");
                CarrierAgent bidder = null;
                if (map.containsKey(username)) {
                    bidder = map.get(username);
                } else {
                    bidder = (CarrierAgent) getAgent(username);
                    map.put(username, bidder);
                }
                if (bidder == null) {
                    String message = "Could not retrieve bidder";
                    lastError = new Exception(message);
                    System.err.println(message);
                    return null;
                }
                result.add(new Bid(id, auction, bidder, price));
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
        var json = new JSONObject(response.body());
        if (!json.getBoolean("success")) {
            var error = json.getJSONObject("error");
            if (error.has("message")) {
                String message = error.getString("message");
                System.err.println(message);
                lastError = new Exception(message);
            }
            if (error.has("stacktrace")) {
                System.err.println(error.getString("stacktrace"));
            }
        }
        return json;
    }
}
