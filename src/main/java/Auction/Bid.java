package Auction;

import Agent.CarrierAgent;

public class Bid {

	public Bid(CarrierAgent bidder, TransportRequest request, float price) {
		this.bidder = bidder;
		this.request = request;
		this.price = price;
	}

	// Variables 

	private CarrierAgent bidder;
	private TransportRequest request;
	private float price;

	// Getters

	public CarrierAgent getBidder() {
		return bidder;
	}

	public TransportRequest getRequest() {
		return request;
	}

	public float getPrice() {
		return price;
	}

}
