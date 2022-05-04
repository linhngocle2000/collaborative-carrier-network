package agent;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class CarrierAgent extends Agent {
   private Helper helper;

   // Registers the agent with the Directory Facilitator as a Carrier
   protected void setup() {
      helper = Helper.getInstance();
      ServiceDescription sd = new ServiceDescription();
      sd.setType("Carrier");
      sd.setName(getLocalName());
      helper.register(this, sd);
   }
}
