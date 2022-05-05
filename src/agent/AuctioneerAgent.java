package agent;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;


public class AuctioneerAgent extends Agent {

   // New load to auction
   private String load;
   // List of bidders
   private ArrayList<AID> bidders = new ArrayList<>();

   // Agent initialization
   protected void setup() {
      // Printout a welcome message
      System.out.println("Hello! Auctioneer-agent "+getAID().getName()+" is ready.");
      // Get the name of new load that agent will auction
      Object[] args = getArguments();
      if (args != null && args.length > 0) {
         load = (String) args[0];
         System.out.println("Trying to auction "+load);
      }
      else {
         // Make the agent terminate immediately
         System.out.println("No load specified");
         doDelete();
      }
      // Add the behaviour confirming requests for requests from carrier agents
      addBehaviour(new ConfirmRequestsServer());
   }

   /**
    Inner class ConfirmRequestsServer.
    If Auctioneer-agent receives a "Join" message, it adds the sender to the bidders queue.
    If Auctioneer-agent receives a "Cancel" message, it removes the sender from the bidders queue.
    */
   private class ConfirmRequestsServer extends CyclicBehaviour {
      public void action() {
         ACLMessage msg = myAgent.receive();
         if (msg != null) {
            ACLMessage reply = msg.createReply();
            switch(msg.getContent()){
               // "Join" message received
               case "Join":
                  // Sender is already in bidders list, then no need to add
                  if (bidders.contains(msg.getSender())) {
                     reply.setPerformative(ACLMessage.INFORM);
                     reply.setContent("Auction already joined");
                  } else {
                     // else adds to bidders list
                     bidders.add(msg.getSender());
                     reply.setPerformative(ACLMessage.CONFIRM);
                     reply.setContent("Auction joined");
                  }
               // "Cancel" msg received
               case "Cancel":
                  // Remove sender from list of bidders
                  bidders.remove(msg.getSender());
                  reply.setPerformative(ACLMessage.CONFIRM);
                  reply.setContent("Auction exit");
            }
            myAgent.send(reply);
         }
      }
   }
   // Put agent clean-up operations here
   protected void takeDown() {
      // Printout a dismissal message
      System.out.println("Auctioneer agent "+getAID().getName()+" terminating.");
   }
}
