package agent;

import jade.core.Agent;

public class AuctioneerAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println("Hello! Auctioneer agent "+getAID().getName()+" is ready.");
    }

}