package agent;

import jade.core.Agent;

public class CarrierAgent extends Agent {

    @Override
    protected void setup() {
        System.out.println("Hello! Carrier agent "+getAID().getName()+" is ready.");
    }
}