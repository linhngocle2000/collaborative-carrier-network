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

import java.util.Iterator;
import java.util.concurrent.TimeUnit;


public class CarrierAgent extends Agent {

   // Available auctioneer main.java.agent
   private AID auctioneerAgent;
   private String auctionName;


   // The GUI to interact with the user
   private CarrierAgentGui myGui;

   public CarrierAgent(String auction) {
      super();
      auctionName = auction;
   }

   /**
    * Agent initializations
    */
   protected void setup() {

      // Printout a welcome message
      System.out.println("Hello! Carrier-agent " + getAID().getName() + " is ready.");

      try {
         // Build the description used as template for the search
         DFAgentDescription template = new DFAgentDescription();
         ServiceDescription templateSd = new ServiceDescription();
         templateSd.setType("tuhh_sd_group04_ccn");
         template.addServices(templateSd);

         SearchConstraints sc = new SearchConstraints();
         // We want to receive 10 results at most
         sc.setMaxResults(10L);

         DFAgentDescription[] results = DFService.search(this, template, sc);
         if (results.length > 0) {
            System.out.println("Agent "+getLocalName()+" found the following auction services:");
            for (DFAgentDescription dfd : results) {
               AID provider = dfd.getName();
               // The same agent may provide several services; we are only interested
               // in the "tuhh_sd_group04_ccn" one
               Iterator it = dfd.getAllServices();
               while (it.hasNext()) {
                  ServiceDescription sd = (ServiceDescription) it.next();
                  if (sd.getType().equals("tuhh_sd_group04_ccn") && sd.getName().equals("Pizza")) {
                     System.out.println("- Service \"" + sd.getName() + "\" provided by agent " + provider.getName());
                     auctioneerAgent = provider;
                  }
               }
            }
         }
         else {
            System.out.println("Agent "+getLocalName()+" did not find any weather-forecast service");
         }
      }
      catch (FIPAException fe) {
         fe.printStackTrace();
      }



   // Show the GUI to interact with the user
      myGui = new CarrierAgentGuiImpl();
      myGui.setAgent(this);
      myGui.show();

      sendJoinRequest();

      // Check whether auctioneer agent is starting/ending an auction
      addBehaviour(new GetAuctionServer());
   }

   /**
    * This method is to get AID of auctioneer main.java.agent
    * @return AID of auctioneer main.java.agent
    */
   public AID getAuctioneerAgent() {
      return auctioneerAgent;
   }

   /**
    * This method is called by the GUI to send a join-request to auctioneer main.java.agent
    */
   public void sendJoinRequest() {
      addBehaviour(new JoinRequest());
   }

   /**
    * This method is called by the GUI to send an exit-request to auctioneer main.java.agent
    */
   public void sendExitRequest() {
      addBehaviour(new ExitRequest(this));
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
         myGui.notifyUser("Msg sent");
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
                  TimeUnit.SECONDS.sleep(10);
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


      private ExitRequest(Agent a) {
         super(a);
      }

      public void action() {
         // Send request to exit
         ACLMessage msg = new ACLMessage(ACLMessage.CANCEL);
         msg.setSender(this.myAgent.getAID());
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
      // Calling dispose() here causes a DisposalInterrupted exception in the main window, so don't do it
      // Dispose the GUI if it is there
      if (myGui != null) {
         myGui.dispose();
      }

      // Printout a dismissal message
      System.out.println("Carrier-agent "+getAID().getName()+" terminating.");
   }
}
