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
import main.java.agent.carrier.CarrierAgent;

import java.sql.*;
import java.util.Vector;


public class AuctioneerAgent extends Agent {

   // Name of load to auction
   private String load;

   // List of bidders
   private Vector<AID> bidders = new Vector<>();

   private boolean isAgentAlive = false;

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
      isAgentAlive = true;

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

      // remove auction from database
      String dburl = "jdbc:postgresql://ec2-99-80-170-190.eu-west-1.compute.amazonaws.com:5432/d7p2aqlsbl8lc8";
      String dbuser = "mwmhqhpwgzqkkc";
      String dbpw = "694d944a63cbfe7db7e950765dcac3260bd859c0668abe43045555abfba8fd0f";
      try (Connection connection = DriverManager.getConnection(dburl, dbuser, dbpw)) {
         Statement statement = connection.createStatement();
         statement.executeUpdate("DELETE FROM public.auctioneer_agent WHERE auction='"+load+"'");
      } catch (SQLException e) {
         System.out.println("Failed to delete auction from database.");
         e.printStackTrace();
      }
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
            // Join-message
            if (msgContent.equals("join")) {
               // Add sender to bidder list
               reply.setPerformative(ACLMessage.CONFIRM);
               reply.setContent(load);
               reply.setProtocol("auction-welcome");
               bidders.add(msg.getSender());
               myGui.notifyUser("Add " + msg.getSender().getLocalName() + " to bidders list");
               myAgent.send(reply);
            } else {
               // else remove from bidder list
               bidders.remove(msg.getSender());
               myGui.notifyUser("Remove " + msg.getSender().getLocalName() + " from bidders list");
            }
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

      // Printout a dismissal message
      System.out.println("Auctioneer-agent "+getAID().getName()+" terminating.");
      isAgentAlive = false;
   }

   public boolean isAgentAlive() {
      return isAgentAlive;
   }
}
