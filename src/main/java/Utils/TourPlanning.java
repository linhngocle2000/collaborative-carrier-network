package Utils;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.SchrimpfFactory;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl.Builder;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.util.Solutions;

import Agent.CarrierAgent;
import Auction.TransportRequest;
import UIResource.HTTPResource.HTTPRequests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

public class TourPlanning {
   private final CarrierAgent agent;
   private Location depot;
   private double costPerDistance;
   private double fixedCost;
   private double internalCost;
   private double loadingCost;

   private VehicleImpl vehicle;
   private final String vehicleID;
   private VehicleRoutingProblem problem;
   private VehicleRoutingProblemSolution bestSolution;
   private List<TransportRequest> requests;
   private CostCalculator cost;

   /**
    * Initial a tour of current carrier agent.
    * Transport request is updated from DB.
    */
   public TourPlanning(CarrierAgent agent) {
      this.agent = agent;
      setDepot(agent.getDepotX(), agent.getDepotY());
      this.vehicleID = agent.getUsername();
      this.costPerDistance = agent.getCostPerDistance();
      this.fixedCost = agent.getFixedCost();
      this.internalCost = agent.getInternalCost();
      this.loadingCost = agent.getLoadingCost();
      refreshRequests();
   }

   /**
    * Initial an empty tour with the current carrier agent data.
    * Transport request list is set empty.
    * <p>
    * Purpose for visualizing and cost calculating the chosen requests in current
    * request list of carrier.
    */
   public TourPlanning(String username) {
      this.agent = (CarrierAgent) HTTPRequests.getAgent(username);
      setDepot(agent.getDepotX(), agent.getDepotY());
      this.vehicleID = agent.getUsername();
      this.costPerDistance = agent.getCostPerDistance();
      this.fixedCost = agent.getFixedCost();
      this.internalCost = agent.getInternalCost();
      this.loadingCost = agent.getLoadingCost();
      this.requests = new ArrayList<>();
   }

   /**
    * Initial an empty tour.
    * Transport request list is empty.
    * <p>
    * Purpose for generating bundles
    */
   public TourPlanning() {
      this.agent = null;
      this.vehicleID = "bundle";
      this.costPerDistance = 2;
      this.fixedCost = 0;
      this.internalCost = 1;
      this.loadingCost = 0;
   }

   /**
    * Update list of request from carrier DB
    */
   public void refreshRequests() {
      if (agent != null) {
         this.requests.clear();
         this.requests.addAll(HTTPRequests.getTransportRequestsOfAgent(agent));
         cost = null;
      }
   }

   /**
    * Add new request into current tour
    */
   public void addRequest(TransportRequest request) {
      requests.add(request);
      cost = null;
   }

   /**
    * Add certain list of request into current tour
    */
   public void addRequests(List<TransportRequest> request) {
      requests.addAll(request);
      cost = null;
   }

   /**
    * Tour optimize
    */
   public JPanel visualize() {
      tourOptimize();
      return new VisualView(problem, bestSolution).display();
   }

   /**
    * Tour optimize
    */
   private void tourOptimize() {
      vehicleRegister();
      setProblem();
      setBestSolution();
   }

   /**
    * Build a vehicle with vehicle type, capacity, username and set it to depot
    * location
    */
   private void vehicleRegister() {
      final int WEIGHT_INDEX = 0;
      VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance("MiniCooper")
            .addCapacityDimension(WEIGHT_INDEX, 2);
      VehicleType vehicleType = vehicleTypeBuilder.build();

      Builder vehicleBuilder = VehicleImpl.Builder.newInstance(vehicleID);
      vehicleBuilder.setStartLocation(depot);
      vehicleBuilder.setType(vehicleType);
      vehicle = vehicleBuilder.build();
   }

   /**
    * Setup VRP
    */
   private void setProblem() {
      VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
      vrpBuilder.addVehicle(vehicle);
      for (TransportRequest request : requests) {
         vrpBuilder.addJob(request.getShipmentObj());
      }
      problem = vrpBuilder.build();
   }

   /**
    * Solving VRP and get the best solution
    */
   private void setBestSolution() {
      VehicleRoutingAlgorithm algorithm = new SchrimpfFactory().createAlgorithm(problem);
      Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
      bestSolution = Solutions.bestOf(solutions);
   }

   /**
    * Cost setup
    */
   private void setCost() {
      if (cost == null) {
         tourOptimize();
         cost = new CostCalculator(this);
      }
   }

   /**
    * Get profit of a certain request on tour
    */
   public double getProfit(TransportRequest request) {
      setCost();
      return cost.profit(request);
   }

   /**
    * Get the sum of profit made by delivering all request
    */
   public double getRevenueSum() {
      setCost();
      return cost.revenueSum();
   }

   /**
    * Get the total revenue
    */
   public double getRevenueTotal() {
      setCost();
      return cost.revenueTotal();
   }

   /**
    * Get total earning that carrier will get from customers after delivering all
    * request
    */
   public double getTotalIn() {
      setCost();
      return cost.totalIn();
   }

   /**
    * Get total cost that carrier has to pay for delivering all request on a tour
    */
   public double getTotalOut() {
      setCost();
      return cost.totalOut();
   }

   /**
    * Get the earning that carrier will get from customer for delivering a certain
    * request
    */
   public double getTransportCostIn(TransportRequest request) {
      setCost();
      return cost.transportCostIn(request.getPickup(), request.getDelivery());
   }

   /**
    * Get the cost that carrier has to pay for delivering a certain request while
    * on tour
    */
   public double getTransportCostOut(TransportRequest request) {
      setCost();
      return cost.transportCostOut(request.getID());
   }

   /**
    * Get the total length of current tour
    */
   public double getTourLength() {
      setCost();
      return cost.tourLength();
   }

   // Basic getters and setters needed

   public double getCostPerDistance() {
      return this.costPerDistance;
   }

   public double getFixedCost() {
      return this.fixedCost;
   }

   public double getInternalCost() {
      return this.internalCost;
   }

   public double getLoadingCost() {
      return this.loadingCost;
   }

   public Location getDepot() {
      return this.depot;
   }

   public CarrierAgent getAgent() {
      return this.agent;
   }

   public void setDepot(Location depot) {
      this.depot = depot;
   }

   public void setDepot(double depotX, double depotY) {
      this.depot = Location.newInstance(depotX, depotY);
   }

   public VehicleRoutingProblemSolution getBestSolution() {
      return this.bestSolution;
   }

   public List<TransportRequest> getRequests() {
      return this.requests;
   }

   public void setRequests(List<TransportRequest> requests) {
      this.requests = requests;
   }

}
