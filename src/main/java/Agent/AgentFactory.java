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
		return new CarrierAgent(username, displayname, depotX, depotY);
	}

	public static AuctioneerAgent auctioneerFromJSON(JSONObject json) {
		String username = json.getString("Username");
		String displayname = json.getString("Name");
		return new AuctioneerAgent(username, displayname);
	}

}
