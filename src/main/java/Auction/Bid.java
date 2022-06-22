package Auction;

import Agent.CarrierAgent;

public class Bid {

	
	public Bid(int id, Auction auction, CarrierAgent bidder, double price) {
		this.id = id;
		this.auction = auction;
		this.bidder = bidder;
		this.price = price;
	}
	
	// Variables 

	private int id;
	private double price;
	private Auction auction;
	private CarrierAgent bidder;

	// Getters

	public int getID() {
		return id;
	}

	public CarrierAgent getBidder() {
		return bidder;
	}

	public Auction getAuction() {
		return auction;
	}

	public double getPrice() {
		return price;
	}

}
