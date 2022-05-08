package agent.carrier;

/**
 * Definition of CarrierAgentGUI interface
 */
public interface CarrierAgentGui {
    void setAgent(CarrierAgent a);
    void show();
    void hide();
    void notifyUser(String message);
    void dispose();
}
