package Agent;

public abstract class Agent {

	public Agent(String username, String displayname) {
		this.username = username;
		this.displayname = displayname;
	}

	@Override
	public String toString() {
		return displayname + " (" + username + ")";
	}

	public abstract boolean isAuctioneer();

	protected String username, displayname;

	// Getters

	public String getUsername() {
		return username;
	}

	public String getDisplayname() {
		return displayname;
	}
}
