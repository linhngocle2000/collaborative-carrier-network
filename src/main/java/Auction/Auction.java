package Auction;

import java.util.ArrayList;
import java.util.List;

import UIResource.HTTPResource.HTTPRequests;

public class Auction {

	private ArrayList<TransportRequest> requests;
	private int iteration;
	private int id;
	private AuctionStrategy strategy;

	public Auction(int id, int iteration) {
		this.id = id;
		this.iteration = iteration;
		requests = new ArrayList<TransportRequest>();
	}

	public void start() {
		// TODO: Start periodically checking for new bids
		strategy.start();
		HTTPRequests.startAuction(this);
	}

	public void addBid(Bid bid) {
		// TODO: Add bid to database
		strategy.addBid(bid);
	}

	public void end() {
		strategy.end();
		HTTPRequests.endAuction(this);
	}

	/**
	 * @return The winning bid including the price to be paid by the winner to the
	 *         seller.
	 */
	public Bid getWinningBid() {
		return strategy.getWinningBid();
	}

	// Setter

	public void setTransportRequests(List<TransportRequest> requests) {
		this.requests = new ArrayList<TransportRequest>(requests);
	}

	public void setAuctionStrategy(AuctionStrategy strategy) {
		this.strategy = strategy;
	}

	// Getter

	public int getID() {
		return id;
	}

	public TransportRequest getDefaultTransportRequest() {
		if (requests.size() > 0) {
			return requests.get(0);
		} else {
			return null;
		}
	}

	/**
	 * @return The transport request that is sold in this auction
	 */
	public List<TransportRequest> getTransportRequests() {
		return requests;
	}

	/**
	 * @return The number of iterations this auction has been started before
	 */
	public int getIteration() {
		return iteration;
	}

}
