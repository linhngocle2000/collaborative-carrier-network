package Auction;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.job.Shipment;

import Agent.CarrierAgent;

public class TransportRequest {

	public TransportRequest(int id, CarrierAgent owner, float pickupX, float pickupY, float deliveryX, float deliveryY) {
		this.id = id;
		this.owner = owner;
		this.pickupX = pickupX;
		this.pickupY = pickupY;
		this.deliveryX = deliveryX;
		this.deliveryY = deliveryY;
	}

	// Variables

	private int id;
	private CarrierAgent owner;
	private float pickupX, pickupY, deliveryX, deliveryY;

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
		return String.format("((%.0f, %.0f),(%.0f,%.0f))", pickupX, pickupY, deliveryX, deliveryY);
	}

	public String getId() {
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
}
