package Auction;

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

	public int getId() {
		return id;
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
