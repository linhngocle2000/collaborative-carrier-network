package Auction;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.job.Shipment;

import Agent.Agent;

public class TransportRequest {

	public TransportRequest(int id, Agent owner, float pickupX, float pickupY, float deliveryX, float deliveryY) {
		this.id = id;
		this.owner = owner;
		this.pickupX = pickupX;
		this.pickupY = pickupY;
		this.deliveryX = deliveryX;
		this.deliveryY = deliveryY;
	}

	// Variables

	private int id;
	private Agent owner;
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

	public String getId() {
		return Integer.toString(id);
	}
	public Agent getOwner() {
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
