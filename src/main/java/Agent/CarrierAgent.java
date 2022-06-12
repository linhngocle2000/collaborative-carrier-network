package Agent;

public class CarrierAgent extends Agent {

	public CarrierAgent(String username, String displayname, float depotX, float depotY, float pickupBaserate,
			float externalTravelCost, float loadBaserate, float internalTravelCost) {
		super(username, displayname);
		this.depotX = depotX;
		this.depotY = depotY;
		this.pickupBaserate = pickupBaserate;
		this.externalTravelCost = externalTravelCost;
		this.loadBaserate = loadBaserate;
		this.internalTravelCost = internalTravelCost;
	}

	@Override
	public boolean isAuctioneer() {
		return false;
	}

	// Variables

	private float depotX, depotY, pickupBaserate, externalTravelCost, loadBaserate, internalTravelCost;

	// Getters

	public float getDepotX() {
		return depotX;
	}

	public float getCostPerDistance() {
		return externalTravelCost;
	}

	public float getFixedCost() {
		return pickupBaserate;
	}

	public float getInternalCost() {
		return internalTravelCost;
	}

	public float getLoadingCost() {
		return loadBaserate;
	}

	public float getDepotY() {
		return depotY;
	}

}
