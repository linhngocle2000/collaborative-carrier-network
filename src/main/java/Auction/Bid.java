package Auction;

import Agent.CarrierAgent;

public class Bid {

	
	public Bid(int id, Auction auction, CarrierAgent bidder, double bidPrice) {
		this.id = id;
		this.auction = auction;
		this.bidder = bidder;
		this.bidPrice = bidPrice;
	}
	
	// Variables 

	private int id;
	private double bidPrice;
	private double payPrice;
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

	public double getBidPrice() {
		return bidPrice;
	}

	public double getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(double payPrice) {
		this.payPrice = payPrice;
	}

}
