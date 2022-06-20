package Auction;

import org.json.JSONObject;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.job.Shipment;

import Agent.CarrierAgent;

public class TransportRequest {
	
	public TransportRequest(int id, CarrierAgent owner, float pickupX, float pickupY, float deliveryX, float deliveryY, boolean inAuction) {
		this.id = id;
		this.owner = owner;
		this.pickupX = pickupX;
		this.pickupY = pickupY;
		this.deliveryX = deliveryX;
		this.deliveryY = deliveryY;
		this.inAuction = inAuction;
	}

	/**
	 * This method does not set the owner of the request.
	 * setOwner must be called on the returned object.
	 */
	public static TransportRequest parse(JSONObject json) {
		int id = json.getInt("ID");
		float pickupX = json.getFloat("PickupLat");
		float pickupY = json.getFloat("PickupLon");
		float deliveryX = json.getFloat("DeliveryLat");
		float deliveryY = json.getFloat("DeliveryLon");
		boolean inAuction = json.getBoolean("IsInAuction");
		return new TransportRequest(id, null, pickupX, pickupY, deliveryX, deliveryY, inAuction);
	}
	
	// Variables
	
	private int id;
	private CarrierAgent owner;
	private float pickupX, pickupY, deliveryX, deliveryY;
	private boolean inAuction;

	// Setters

	public void setOwner(CarrierAgent agent) throws Exception {
		if (owner != null) {
			throw new Exception("Can not set another owner if owner is already set");
		}

		owner = agent;
	}

	// Getters

	public Shipment getShipmentObj() {
      return Shipment.Builder.newInstance(Integer.toString(id)).setPickupLocation(getPickup()).setDeliveryLocation(getDelivery()).build();
	}

	public Location getPickup() {
		return Location.newInstance(pickupX, pickupY);
	}

	public Location getDelivery() {
		return Location.newInstance(deliveryX, deliveryY);
	}

	/**
	 * Converts pickup and delivery of the request to a tuple of tuples, e.g. ((1,2),(2,3)).
	 * Digits after the decimal mark are cut off, e.g. 23.484 becomes 23.
	 */
	public String getRouteString() {
		return String.format("((%.0f,%.0f),(%.0f,%.0f))", pickupX, pickupY, deliveryX, deliveryY);
	}

	public String getID() {
		return Integer.toString(id);
	}
	public CarrierAgent getOwner() {
		return owner;
	}
	public float getPickupX() {
		return pickupX;
	}
	public float getPickupY() {
		return pickupY;
	}
	public float getDeliveryX() {
		return deliveryX;
	}
	public float getDeliveryY() {
		return deliveryY;
	}

	/**
	 * @return True if this request will be sold in an auction
	 */
	public boolean isInAuction() {
		return inAuction;
	}
}
