package Agent;

public class CarrierAgent extends Agent {

	public CarrierAgent(String username, String displayname, float depotX, float depotY) {
		super(username, displayname);
		this.depotX = depotX;
		this.depotY = depotY;
	}

	@Override
	public boolean isAuctioneer() {
		return false;
	}

	// Variables

	private float depotX, depotY;

	// Getters

	public float getDepotX() {
		return depotX;
	}

	public float getDepotY() {
		return depotY;
	}
	
}
