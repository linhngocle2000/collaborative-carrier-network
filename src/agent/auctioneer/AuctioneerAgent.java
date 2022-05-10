package agent.auctioneer;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.Vector;


public class AuctioneerAgent extends Agent {

   // Name of load to auction
   private String load = null;

   // List of bidders
   private Vector<AID> bidders = new Vector<>();

   // List of available carriers
   private final Vector<AID> carrierAgents = new Vector<>();

   // The GUI to interact with the user
   private AuctioneerAgentGui myGui;

   // Normal ctor
   public AuctioneerAgent() {
      super();
   }

   // Auction ready ctor
   public AuctioneerAgent(String auction) {
      this();
      load = auction;
      startAuction();
   }

   /**
    * Agent initializations
    */
   protected void setup() {

      // Printout a welcome message
      System.out.println("Hello! Auctioneer-agent "+getAID().getName()+" is ready.");

      // Get the list of all available carriers
      addBehaviour(new UpdateCarriers());

      // Show the GUI to interact with the user
      myGui = new AuctioneerAgentGuiImpl(load);
      myGui.setAgent(this);
      myGui.show();
   }

   /**
    * Inner class UpdateCarriers
    */
   private class UpdateCarriers extends CyclicBehaviour {
      public void action() {         
         try {
            // Find all connected carriers
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("Carrier");
            template.addServices(sd);
            DFAgentDescription[] result = DFService.search(myAgent, template);

            // Check if carriers need to be added to the local list
            for (DFAgentDescription dfAgentDescription : result) {
               if (!carrierAgents.stream().anyMatch(item -> item.getName().equals(dfAgentDescription.getName().getName())))
               {
                  // Remote carrier is not in local list so add it
                  carrierAgents.addElement(dfAgentDescription.getName());
               }
            }
         }
         catch (FIPAException fe) {
            fe.printStackTrace();
         }
      }
   }

   /**
    * This method is called by the GUI when the user starts an auction.
    */
   public void startAuction() {
      String req = "Auction for " + load + " started";

      // Inform all available carrier agents about the new auction
      addBehaviour(new InformAuction(this, req));

      // Check for requests from carrier agents
      addBehaviour(new ConfirmRequestsServer());
   }

   /**
    * This method is called by the GUI when the user ends an auction.
    */
   public void endAuction() {
      String req = "Auction for " + load + " ended";

      // Inform all available carrier agents about the closed auction
      addBehaviour(new InformAuction(this, req));
   }

   /**
    * This method is for retrieving the name of the auction.
    * @return Name of auction
    */
   public String getLoad() {
      return load;
   }

   /**
    * This method is to set the name of the auction.
    * @param loadToSet Name of auction to set
    */
   public void setLoad(String loadToSet) {
      load = loadToSet;
   }


   /**
    * This method is called by the GUI to clear list of bidders when auction ended.
    */
   public void clearListOfBidders() {
      bidders.clear();
   }

   /**
    * Inner class InformAuction
    */
   private class InformAuction extends OneShotBehaviour {

      // Request to send
      private String info;

      private InformAuction(Agent a, String info) {
         super(a);
         this.info = info;
      }

      public void action() {
         // Send information about auction to all carrier agents
         ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
         msg.setSender(this.myAgent.getAID());
         msg.setProtocol("auction-information");
         msg.setContent(this.info);
         for (AID carrierAgent : carrierAgents) {
            msg.addReceiver(carrierAgent);
         }
         send(msg);
      }
   }

   /**
    Inner class ConfirmRequestsServer.
    */
   private class ConfirmRequestsServer extends CyclicBehaviour {
      public void action() {
         ACLMessage msg = myAgent.receive();

         // Message received from a carrier agent
         if (msg != null) {
            // Message format: $Auction_name + " " + ("Join"/"Exit")
            // Split message in auction name and request
            ACLMessage reply = msg.createReply();
            String msgContent = msg.getContent();
            String auctionToJoin = msgContent.split(" ")[0];
            String request = msgContent.split(" ")[1];
            // Name of auction in message is invalid
            if (load == null || !load.equals(auctionToJoin)) {
               // Send "invalid request" reply
               reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
               reply.setContent("Auction not available");
            }
            // Sender sends "Join" request
            else if (request.equals("Join")) {
               // Sender is already in bidders list, then no need to add
               if (bidders.contains(msg.getSender())) {
                  reply.setPerformative(ACLMessage.INFORM);
                  reply.setContent("Already in bidders list");
               } else {
                  // else adds to bidders list
                  bidders.add(msg.getSender());
                  reply.setPerformative(ACLMessage.CONFIRM);
                  reply.setContent("Add to bidders list");
                  myGui.notifyUser("Add " + msg.getSender().getLocalName() + " to bidders list");
               }
            }
            // Sender sends "Exit" request
            else {
               // If sender is not in list of bidder, send not-in-list message
               if (!bidders.contains(msg.getSender())) {
                  reply.setPerformative(ACLMessage.INFORM);
                  reply.setContent("Not in bidders list");
               } else {
                  // Remove sender from list of bidders
                  bidders.remove(msg.getSender());
                  reply.setPerformative(ACLMessage.CONFIRM);
                  reply.setContent("Remove from bidders list");
                  myGui.notifyUser("Remove " + msg.getSender().getLocalName() + " from bidders list");
               }
            }
            myAgent.send(reply);
         }
         // No message received, then block behaviour
         else {
            block();
         }
      }
   }

   /**
    * Agent clean-up
    */
   protected void takeDown() {

      // Dispose the GUI if it is there
      if (myGui != null) {
         myGui.dispose();
      }

      // Printout a dismissal message
      System.out.println("Auctioneer agent "+getAID().getName()+" terminating.");
   }
}
