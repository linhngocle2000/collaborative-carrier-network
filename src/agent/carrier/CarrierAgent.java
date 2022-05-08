package agent.carrier;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;


public class CarrierAgent extends Agent {

   // Available auctioneer agent
   private AID auctioneerAgent ;

   // The GUI to interact with the user
   private CarrierAgentGui myGui;

   /**
    * Agent initializations
    */
   protected void setup() {

      // Printout a welcome message
      System.out.println("Hello! Carrier-agent "+getAID().getName()+" is ready.");

      // Register the carrier service and carrier agent in the yellow pages
      DFAgentDescription dfd = new DFAgentDescription();
      dfd.setName(getAID());
      ServiceDescription sd = new ServiceDescription();
      sd.setType("Carrier");
      sd.setName(getLocalName());
      dfd.addServices(sd);
      try {
         DFService.register(this, dfd);
      }
      catch (FIPAException fe) {
         fe.printStackTrace();
      }

      // Show the GUI to interact with the user
      myGui = new CarrierAgentGuiImpl();
      myGui.setAgent(this);
      myGui.show();

      // Check whether auctioneer agent is starting/ending an auction
      addBehaviour(new GetAuctionServer());
   }

   /**
    * This method is to get AID of auctioneer agent
    * @return AID of auctioneer agent
    */
   public AID getAuctioneerAgent() {
      return auctioneerAgent;
   }

   /**
    * This method is called by the GUI to send a join-request to auctioneer agent
    * @param request Request to join of format $Auction_name + " Join"
    */
   public void sendJoinRequest(String request) {
      addBehaviour(new JoinRequest(this, request));
   }

   /**
    * This method is called by the GUI to send an exit-request to auctioneer agent
    * @param request Request to exit of format $Auction_name + " Exit"
    */
   public void sendExitRequest(String request) {
      addBehaviour(new ExitRequest(this, request));
   }

   /**
    * Inner class GetAuctionServer
    */
   private class GetAuctionServer extends CyclicBehaviour {
      public void action() {
         ACLMessage msg = myAgent.receive();

         // Received a message from auctioneer agent
         if (msg != null) {

            // Print message content and sender in GUI
            myGui.notifyUser("Message: " + msg.getContent() + " from " + msg.getSender().getLocalName());
            auctioneerAgent = msg.getSender();
         }
         // No message received, then block behaviour
         else {
            block();
         }
      }
   }

   /**
    * Inner class JoinRequest
    */
   private class JoinRequest extends OneShotBehaviour {

      private String request;

      private JoinRequest(Agent a, String req) {
         super(a);
         request = req;
      }

      public void action() {
         // Send request to join
         ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
         msg.setSender(this.myAgent.getAID());
         msg.setProtocol("auction-request");
         msg.setContent(request);
         msg.addReceiver(auctioneerAgent);
         send(msg);
      }
   }

   /**
    * Inner class ExitRequest
    */
   private class ExitRequest extends OneShotBehaviour {

      private String request;

      private ExitRequest(Agent a, String req) {
         super(a);
         request = req;
      }

      public void action() {
         // Send request to exit
         ACLMessage msg = new ACLMessage(ACLMessage.CANCEL);
         msg.setSender(this.myAgent.getAID());
         msg.setProtocol("auction-request");
         msg.setContent(request);
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
      // if (myGui != null) {
      //    myGui.dispose();
      // }

      // Unregister the carrier agent from the yellow pages
      try {
         DFService.deregister(this);
      }
      catch (FIPAException fe) {
         fe.printStackTrace();
      }

      // Printout a dismissal message
      System.out.println("Carrier-agent "+getAID().getName()+" terminating.");
   }
}
