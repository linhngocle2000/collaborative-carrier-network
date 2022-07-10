package Auction;

public class VickreyAuction extends AuctionStrategy {

	private Bid topBid;
	private Bid secondBid;

	@Override
	public void start() {
	}

	@Override
	public void addBid(Bid bid) {
		if (topBid == null) {
			topBid = bid;
		}
		if (secondBid == null) {
			secondBid = bid;
		}
		if (bid.getBidPrice() > topBid.getBidPrice()) {
			secondBid = topBid;
			topBid = bid;
		} else if (bid.getBidPrice() > secondBid.getBidPrice()) {
			secondBid = bid;
		}
	}

	@Override
	public void end() {
	}

	@Override
	public Bid getWinningBid() {
		if (topBid == null) {
			return null;
		}

		if (secondBid != null) {
			topBid.setPayPrice(secondBid.getPayPrice());
		}

		return topBid;
	}

}
