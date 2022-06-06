package Agent;

public class CarrierAgent extends Agent {

	public CarrierAgent(String username, String displayname, String vehicle, float depotX, float depotY) {
		super(username, displayname);
		this.vehicle = vehicle;
		this.depotX = depotX;
		this.depotY = depotY;
	}

	@Override
	public boolean isAuctioneer() {
		return false;
	}

	// Variables

	private String vehicle;
	private float depotX, depotY;

	// Getters

	public String getVehicle() {
		return vehicle;
	}

	public float getDepotX() {
		return depotX;
	}

	public float getDepotY() {
		return depotY;
	}
	
}
