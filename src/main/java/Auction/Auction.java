package Auction;

import java.util.ArrayList;
import java.util.List;

public abstract class Auction {

	public static final String TYPE_VICKREY = "Vickrey";
	public static final String TYPE_TRADITIONAL = "Traditional";

	private ArrayList<TransportRequest> requests;
	private int iteration;
	private int id;

	public Auction(int id, int iteration) {
		this.id = id;
		this.iteration = iteration;
		requests = new ArrayList<TransportRequest>();
	}

	/**
	 * @return Type of auction. Must be one of 
	 * @see Auction.Auction#TYPE_VICKREY
	 * @see Auction.Auction#TYPE_TRADITIONAL
	 */
	public abstract String getType();

	public abstract void start();
	public abstract void addBid(Bid bid);
	public abstract void end();

	/**
	 * @return The winning bid including the price to be paid by the winner to the
	 *         seller.
	 */
	public abstract Bid getWinningBid();

	public void setTransportRequests(List<TransportRequest> requests) {
		this.requests = new ArrayList<TransportRequest>(requests);
	}

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
