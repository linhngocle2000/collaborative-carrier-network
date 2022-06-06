package Agent;

public class AuctioneerAgent extends Agent {

	public AuctioneerAgent(String username, String displayname) {
		super(username, displayname);
	}

	@Override
	public boolean isAuctioneer() {
		return true;
	}
	
}
