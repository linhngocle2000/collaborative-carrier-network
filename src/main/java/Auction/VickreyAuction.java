package Auction;

import java.util.ArrayList;
import java.util.List;

public class VickreyAuction extends Auction {

	private List<Bid> bids;
	private Bid topBid;
	private Bid secondBid;

	public VickreyAuction(TransportRequest request, int iteration) {
		super(request, iteration);
	}

	private void refreshBids() {
		topBid = null;
		secondBid = null;
		bids = new ArrayList<Bid>();
		// HTTPRequests.getCarrierAgents();
		for (Bid bid : bids) {
			if (bid.getPrice() == 0) {
				bids.remove(bid);
			}
		}
	}

	private void computeWinner() {
		refreshBids();
		for (Bid bid : bids) {
			if (topBid == null) {
				topBid = bid;
			}
			if (secondBid == null) {
				secondBid = bid;
			}
			if (bid.getPrice() > topBid.getPrice()) {
				secondBid = topBid;
				topBid = bid;
			} else if (bid.getPrice() > secondBid.getPrice()) {
				secondBid = bid;
			}
		}
	}

	@Override
	public Bid getWinningBid() {
		computeWinner();
		return new Bid(topBid.getBidder(), topBid.getRequest(), secondBid.getPrice());
	}

}
