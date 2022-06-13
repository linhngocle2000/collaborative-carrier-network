package Auction;

public abstract class AuctionStrategy {

	public static final String TYPE_VICKREY = "Vickrey";
	public static final String TYPE_TRADITIONAL = "Traditional";
	
	public abstract void start();
	public abstract void addBid(Bid bid);
	public abstract void end();
	public abstract Bid getWinningBid();
}
