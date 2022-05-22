package main.java.agent.carrier;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import main.java.agent.auctioneer.AuctioneerAgentGui;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;


public class CarrierAgent extends Agent {

   // Available auctioneer main.java.agent
   private AID auctioneerAgent;
   private String auctionName;
   private String mtp;


   // The GUI to interact with the user
   private CarrierAgentGui myGui;
   private boolean isAgentAlive = false;

   public CarrierAgent(String auction, String mtpaddr) {
      super();
      auctionName = auction;
      mtp = mtpaddr;
   }

   /**
    * Agent initializations
    */
   protected void setup() {

      // Printout a welcome message
      System.out.println("Hello! Carrier-agent " + getAID() + " is ready.");
      isAgentAlive = true;

      auctioneerAgent = new AID(auctionName+"@Auction", AID.ISGUID);
      auctioneerAgent.addAddresses(mtp);



   // Show the GUI to interact with the user
      myGui = new CarrierAgentGuiImpl();
      myGui.setAgent(this);
      myGui.show();

      sendJoinRequest();

      // Check whether auctioneer agent is starting/ending an auction
      addBehaviour(new GetAuctionServer());
   }

   /**
    * This method is to get AID of auctioneer agent
    * @return AID of auctioneer main.java.agent
    */
   public AID getAuctioneerAgent() {
      return auctioneerAgent;
   }

   /**
    * This method is called by the GUI to send a join-request to auctioneer agent
    */
   public void sendJoinRequest() {
      addBehaviour(new JoinRequest());
   }

   /**
    * This method is called by the GUI to send an exit-request to auctioneer agent
    */
   public void sendExitRequest() {
      addBehaviour(new ExitRequest());
   }

   /**
    * Inner class JoinRequest
    */
   private class JoinRequest extends OneShotBehaviour {

      public void action() {
         // Send request to join
         ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
         msg.setSender(myAgent.getAID());
         msg.setProtocol("auction-join");
         msg.setContent("join");
         msg.addReceiver(auctioneerAgent);
         send(msg);
      }
   }

   /**
    * Inner class GetAuctionServer
    */
   private class GetAuctionServer extends CyclicBehaviour {
      public void action() {
         ACLMessage msg = myAgent.receive();

         // Received a message from auctioneer main.java.agent
         if (msg != null) {
            if (msg.getProtocol().equals("auction-welcome")) {
               String auction = msg.getContent();
               myGui.notifyUser("You joined the auction for " + auction);
               myGui.setAuctionField(auction);
            } else if (msg.getProtocol().equals("auction-terminate")) {
               String auction = msg.getContent();
               myGui.notifyUser("Auction for " + auction + " is terminating. Window will close shortly");
               try {
                  TimeUnit.SECONDS.sleep(5);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
               myGui.dispose();
            } else {
               // Print message content and sender in GUI
               myGui.notifyUser("Message: \"" + msg.getContent() + "\" from " + msg.getSender().getLocalName());
            }
         }
         // No message received, then block behaviour
         else {
            block();
         }
      }
   }

   /**
    * Inner class ExitRequest
    */
   private class ExitRequest extends OneShotBehaviour {

      public void action() {
         // Send request to exit
         myGui.notifyUser("msg sent");
         ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
         msg.setSender(myAgent.getAID());
         msg.setProtocol("auction-exit");
         msg.setContent("exit");
         msg.addReceiver(auctioneerAgent);
         send(msg);
      }
   }

   /**
    * Agent clean-up
    */
   protected void takeDown() {

      // Printout a dismissal message
      System.out.println("Carrier-agent "+getAID().getName()+" terminating.");
      isAgentAlive = false;

   }

   public boolean isAgentAlive() {
      return isAgentAlive;
   }
}
