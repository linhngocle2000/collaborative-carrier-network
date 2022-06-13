package Auction;

import Agent.CarrierAgent;

public class Bid {

	
	public Bid(int id, Auction auction, CarrierAgent bidder, int price) {
		this.id = id;
		this.auction = auction;
		this.bidder = bidder;
		this.price = price;
	}
	
	// Variables 
	
	private int id, price;
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

	public int getPrice() {
		return price;
	}

}
