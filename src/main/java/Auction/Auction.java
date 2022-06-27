package Auction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import UIResource.HTTPResource.HTTPRequests;
import org.json.JSONException;

public class Auction {

	private ArrayList<TransportRequest> requests;
	private int iteration;
	private int id, lastBidID = -1;
	private boolean isActive = false;
	private AuctionStrategy strategy;

	public Auction(int id, int iteration) {
		this.id = id;
		this.iteration = iteration;
		requests = new ArrayList<>();
	}

	public void start() throws IOException, InterruptedException, JSONException {
		strategy.start();
		HTTPRequests.startAuction(this);
		isActive = true;
	}

	public void addBid(Bid bid) {
		// Guard against bid insertion after auction has ended
		strategy.addBid(bid);
	}

	public void end() throws IOException, InterruptedException, JSONException {
		isActive = false;
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

	/**
	 * Sends a message to the winner of the auction and transfers ownership
	 * of transport requests
	 */
	public void notifyWinner() throws IOException, InterruptedException, JSONException {
		HTTPRequests.setWinner(this, getWinningBid());
	}

	// Setter

	public void setTransportRequests(List<TransportRequest> requests) {
		this.requests = new ArrayList<>(requests);
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
