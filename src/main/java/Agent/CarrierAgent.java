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

	public float getCostPerDistance() {
		return (float) 1000.00;
	}

	public float getFixedCost() {
		return (float) 1000.00;
	}

	public float getInternalCost() {
		return (float) 1000.00;
	}

	public float getLoadingCost() {
		return (float) 1000.00;
	}

	public float getDepotY() {
		return depotY;
	}
	
}
