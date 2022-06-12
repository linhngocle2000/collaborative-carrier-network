package Auction;

public class VickreyAuction extends Auction {

	private Bid topBid;
	private Bid secondBid;

	public VickreyAuction(int id, int iteration) {
		super(id, iteration);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
	}

	@Override
	public Bid getWinningBid() {
		return new Bid(topBid.getBidder(), topBid.getRequest(), secondBid.getPrice());
	}

	@Override
	public String getType() {
		return Auction.TYPE_VICKREY;
	}

}
