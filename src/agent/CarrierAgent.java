package agent;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;


public class CarrierAgent extends Agent {

   private AID auctioneerAgents ;

   // Agent initialization
   protected void setup() {
      // Printout a welcome message
      System.out.println("Hello! Carrier-agent "+getAID().getName()+" is ready.");
      // Get the name of auctioneer agent which hosts the auction
      Object[] args = getArguments();
      if (args != null && args.length > 0) {
         auctioneerAgents = new AID((String) args[0], AID.ISLOCALNAME);
         System.out.println("Trying to join auction of "+auctioneerAgents);
      }
      else {
         // Make the agent terminate immediately
         System.out.println("No load specified");
         doDelete();
      }
   }

   // Send "Join" message
   private class SendRequest extends OneShotBehaviour {
      public void action() {
         ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
         msg.setSender(this.myAgent.getAID());
         msg.setProtocol("auction-request");
         msg.setContent("Join");
         msg.addReceiver(auctioneerAgents);
         send(msg);
      }
   }

   // Send "Cancel" message
   private class CancelRequest extends OneShotBehaviour {
      public void action() {
         ACLMessage msg = new ACLMessage(ACLMessage.CANCEL);
         msg.setSender(this.myAgent.getAID());
         msg.setProtocol("auction-request");
         msg.setContent("Cancel");
         msg.addReceiver(auctioneerAgents);
         send(msg);
      }
   }

   // Put agent clean-up operations here
   protected void takeDown() {
      // Printout a dismissal message
      System.out.println("Carrier-agent "+getAID().getName()+" terminating.");
   }
}
