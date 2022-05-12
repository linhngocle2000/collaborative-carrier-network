package main.java.agent.auctioneer;

/**
 * Definition of AuctioneerAgentGUI interface
 */
public interface AuctioneerAgentGui {
    void setAgent(AuctioneerAgent a);
    void show();
    void hide();
    void setAuctionField(String auction);
    void notifyUser(String message);
    void dispose();
}
