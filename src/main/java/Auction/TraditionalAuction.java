package Auction;

public class TraditionalAuction extends AuctionStrategy {

	private Bid highestBid;

	@Override
	public void start() {
	}

	@Override
	public void addBid(Bid bid) {
		if (highestBid == null || bid.getPrice() > highestBid.getPrice()) {
			highestBid = bid;
		}	
	}

	@Override
	public void end() {
	}

	@Override
	public Bid getWinningBid() {
		return highestBid;
	}
	
}
