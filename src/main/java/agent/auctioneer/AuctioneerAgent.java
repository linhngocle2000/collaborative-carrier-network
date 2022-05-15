package main.java.agent.auctioneer;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.util.leap.Iterator;

import java.util.Vector;


public class AuctioneerAgent extends Agent {

   // Name of load to auction
   private String load;

   // List of bidders
   private Vector<AID> bidders = new Vector<>();

   // List of available carriers
   //private final Vector<AID> carrierAgents = new Vector<>();

   // The GUI to interact with the user
   private AuctioneerAgentGui myGui;

   // Auction ready ctor
   public AuctioneerAgent(String auction) {
      super();
      load = auction;
   }

   /**
    * Agent initializations
    */
   protected void setup() {

      // Printout a welcome message
      System.out.println("Hello! Auctioneer-agent "+getAID()+" is ready.");

      try {
         DFAgentDescription dfd = new DFAgentDescription();
         dfd.setName(getAID());
         ServiceDescription sd = new ServiceDescription();
         sd.setName(load);
         sd.setType("tuhh_sd_group04_ccn");
         // Agents that want to use this service need to "know" the weather-forecast-ontology
         sd.addOntologies("ccn-ontology");
         // Agents that want to use this service need to "speak" the FIPA-SL language
         sd.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
         dfd.addServices(sd);

         DFService.register(this, dfd);
      }
      catch (FIPAException fe) {
         fe.printStackTrace();
      }

      // Show the GUI to interact with the user
      myGui = new AuctioneerAgentGuiImpl();
      myGui.setAgent(this);
      myGui.show();

      startAuction();
   }


   /**
    * This method is called by the GUI when the user starts an auction.
    */
   public void startAuction() {

      myGui.setAuctionField(load);
      myGui.notifyUser("Auction: " + load + " started");
      // Check for requests from carrier agents
      addBehaviour(new ConfirmRequestsServer());
   }

   /**
    * This method is called by the GUI when the user ends an auction.
    */
   public void endAuction() {

      // Inform all available carrier agents about the closed auction
      addBehaviour(new EndAuction(this));
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
//   public void clearListOfBidders() {
//      bidders.clear();
//   }

//   private class WelcomeNewBidder extends OneShotBehaviour {
//
//      private Agent auctioneer;
//      private AID bidder;
//
//      public WelcomeNewBidder(Agent auctioneer, AID bidder) {
//         super(auctioneer);
//         this.auctioneer = auctioneer;
//         this.bidder = bidder;
//      }
//
//      public void action() {
//         ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
//         msg.setSender(auctioneer.getAID());
//         msg.setProtocol("auction-welcome");
//         msg.setContent(load);
//         msg.addReceiver(bidder);
//         send(msg);
//      }
//   }

   /**
    * Inner class InformAuction
    */
   private class EndAuction extends OneShotBehaviour {


      private EndAuction(Agent a) {
         super(a);
      }

      public void action() {
         // Send information about auction to all carrier agents
         ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
         msg.setSender(this.myAgent.getAID());
         msg.setProtocol("auction-terminate");
         msg.setContent(load);
         for (AID carrierAgent : bidders) {
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
            ACLMessage reply = msg.createReply();
            String msgContent = msg.getContent();
            myGui.notifyUser("msg received");
            // Name of auction in message is invalid
            if (msgContent.equals("join")) {
               // Add sender to bidder list
               reply.setPerformative(ACLMessage.CONFIRM);
               reply.setContent(load);
               reply.setProtocol("auction-welcome");
               bidders.add(msg.getSender());
               myGui.notifyUser("Add " + msg.getSender().getLocalName() + " to bidders list");
               myGui.notifyUser("msg sent");
            }
            // Sender sends "Join" request
            else {
               // else adds to bidders list
               bidders.remove(msg.getSender());
               myGui.notifyUser("Remove " + msg.getSender().getLocalName() + " to bidders list");
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
      System.out.println("Auctioneer-agent "+getAID().getName()+" terminating.");
   }
}
