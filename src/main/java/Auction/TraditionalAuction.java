package Auction;

public class TraditionalAuction extends Auction {

	private Bid highestBid;

	public TraditionalAuction(int id, int iteration) {
		super(id, iteration);
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void addBid(Bid bid) {
		if (highestBid == null || bid.getPrice() > highestBid.getPrice()) {
			highestBid = bid;
		}	
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub		
	}

	@Override
	public Bid getWinningBid() {
		return highestBid;
	}

	@Override
	public String getType() {
		return Auction.TYPE_TRADITIONAL;
	}
	
}
