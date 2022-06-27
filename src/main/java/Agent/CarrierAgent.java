package Agent;

public class CarrierAgent extends Agent {

	public CarrierAgent(String username, String displayname, double minProfit, double maxProfit, float depotX, float depotY, float pickupBaserate,
			float externalTravelCost, float loadBaserate, float internalTravelCost) {
		super(username, displayname);
		this.minProfit = minProfit;
		this.maxProfit = maxProfit;
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
	private double minProfit, maxProfit;

	// Getters

	/** @return Minimum profit that a transport request must result in for the carrier to bid on it */
	public double getMinProfit() {
		return minProfit;
	}

	/** @return Profit threshold for own transport requests. Transport requests resulting in less profit will be auctioned off. */
	public double getMaxProfit() {
		return maxProfit;
	}

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
