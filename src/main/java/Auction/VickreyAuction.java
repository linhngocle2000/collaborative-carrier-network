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
		if (bid.getPrice() > topBid.getPrice()) {
			secondBid = topBid;
			topBid = bid;
		} else if (bid.getPrice() > secondBid.getPrice()) {
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
		return new Bid(topBid.getID(), topBid.getAuction(), topBid.getBidder(), secondBid.getPrice());
	}

}
