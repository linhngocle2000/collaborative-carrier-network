package agent;

import java.util.ArrayList;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;

public class Helper extends Agent {

   private static Helper instance = null;
   private ArrayList<Agent> registeredAgents = new ArrayList<Agent>();


   //Stay in current state instead of opening new state everytime register a new agent
   public static synchronized Helper getInstance() {
		if (instance == null) {
			instance = new Helper();
		}
		return instance;
	}

   // Register a new agent
   public void register(Agent agent, ServiceDescription sd) {
      DFAgentDescription dfd = new DFAgentDescription();
      dfd.setName(getAID());
      dfd.addServices(sd);

      try {
         registeredAgents.add(agent);
         DFService.register(agent, dfd);
         System.out.println(agent.getName() + " registerd as: " + sd.getType());
         
         System.out.println("Agent List:");
         for (int i = 0; i < registeredAgents.size(); i++) {
            System.out.println(registeredAgents.get(i).getName());
         }
      } catch (FIPAException e) {
         e.printStackTrace();
      }
   }
}
