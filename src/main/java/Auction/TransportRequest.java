package Auction;

import org.json.JSONObject;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.job.Shipment;

import Agent.CarrierAgent;

public class TransportRequest {
	
	public TransportRequest(int id, CarrierAgent owner, double pickupX, double pickupY, double deliveryX, double deliveryY) {
		this.id = id;
		this.owner = owner;
		this.pickupX = pickupX;
		this.pickupY = pickupY;
		this.deliveryX = deliveryX;
		this.deliveryY = deliveryY;
		this.cost = 0;
	}

	public TransportRequest(int id, CarrierAgent owner, double pickupX, double pickupY, double deliveryX, double deliveryY, double price) {
		this.id = id;
		this.owner = owner;
		this.pickupX = pickupX;
		this.pickupY = pickupY;
		this.deliveryX = deliveryX;
		this.deliveryY = deliveryY;
		this.cost = price;
	}

	/**
	 * This method does not set the owner of the request.
	 * setOwner must be called on the returned object.
	 */
	public static TransportRequest parse(JSONObject json) {
		int id = json.getInt("ID");
		double cost = json.getDouble("Cost");
		double pickupX = json.getDouble("PickupLat");
		double pickupY = json.getDouble("PickupLon");
		double deliveryX = json.getDouble("DeliveryLat");
		double deliveryY = json.getDouble("DeliveryLon");
		return new TransportRequest(id, null, pickupX, pickupY, deliveryX, deliveryY, cost);
	}
	
	// Variables
	
	private int id;
	private CarrierAgent owner;
	private double pickupX, pickupY, deliveryX, deliveryY;
	private double cost;

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
	public double getPickupX() {
		return pickupX;
	}
	public double getPickupY() {
		return pickupY;
	}
	public double getDeliveryX() {
		return deliveryX;
	}
	public double getDeliveryY() {
		return deliveryY;
	}
	public double getCost() {
		return cost;
	}

}
