package agent;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class AuctioneerAgent extends Agent {
   private Helper helper;

   // Registers the agent with the Directory Facilitator as an Auctioneer
   protected void setup() {
      helper = Helper.getInstance();
      ServiceDescription sd = new ServiceDescription();
      sd.setType("Auctioneer");
      sd.setName(getLocalName());
      helper.register(this, sd);
   }
	
	
}
