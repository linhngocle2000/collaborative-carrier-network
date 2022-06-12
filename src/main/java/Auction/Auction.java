package Auction;

public abstract class Auction {

	private TransportRequest request;
	private int iteration;

	public Auction(TransportRequest request, int iteration) {
		this.request = request;
		this.iteration = iteration;
	}

	/**
	 * @return The winning bid including the price to be paid by the winner to the seller.
	 */
	public abstract Bid getWinningBid();

	/**
	 * @return The transport request that is sold in this auction
	 */
	public TransportRequest getTransportRequest() {
		return request;
	}

	/**
	 * @return The number of iterations this auction has been started before
	 */
	public int getIteration() {
		return iteration;
	}
	
}
