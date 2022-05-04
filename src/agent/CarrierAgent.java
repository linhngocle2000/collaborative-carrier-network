package agent;

import java.util.ArrayList;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

public class CarrierAgent extends Agent {
    private ArrayList<Agent> registeredAgents = new ArrayList<Agent>();


   // Register a new agent
   public void register(Agent agent, ServiceDescription sd) {
      DFAgentDescription dfd = new DFAgentDescription();
      dfd.setName(getAID());
      dfd.addServices(sd);

      try {
         registeredAgents.add(agent);
         DFService.register(agent, dfd);
         System.out.println(agent.getName() + " registerd as: " + sd.getType());
      } catch (FIPAException e) {
         e.printStackTrace();
      }
   }


   // Registers the agent with the Directory Facilitator as a Carrier
   protected void setup() {
      ServiceDescription sd = new ServiceDescription();
      sd.setType("Carrier");
      sd.setName(getLocalName());
      register(this, sd);
   }
}
