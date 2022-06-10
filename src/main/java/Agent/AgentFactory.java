package Agent;

import org.json.JSONObject;

public class AgentFactory {

	public static Agent fromJSON(JSONObject json) {
		boolean isAuctioneer = json.getBoolean("IsAuctioneer");
		if (isAuctioneer) {
			return auctioneerFromJSON(json);
		} else {
			return carrierFromJSON(json);
		}
	}

	public static CarrierAgent carrierFromJSON(JSONObject json) {
		String username = json.getString("Username");
		String displayname = json.getString("Name");
		float depotX = json.getFloat("DepotLat");
		float depotY = json.getFloat("DepotLon");
		CarrierAgent agent = new CarrierAgent(username, displayname, depotX, depotY);
		return agent;
	}

	public static AuctioneerAgent auctioneerFromJSON(JSONObject json) {
		String username = json.getString("Username");
		String displayname = json.getString("Name");
		AuctioneerAgent agent = new AuctioneerAgent(username, displayname);
		return agent;
	}

}
