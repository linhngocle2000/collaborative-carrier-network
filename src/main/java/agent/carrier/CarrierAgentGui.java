package main.java.agent.carrier;

/**
 * Definition of CarrierAgentGUI interface
 */
public interface CarrierAgentGui {
    void setAgent(CarrierAgent a);
    void show();
    void hide();
    void setAuctionField(String auction);
    void notifyUser(String message);
    void dispose();
}
