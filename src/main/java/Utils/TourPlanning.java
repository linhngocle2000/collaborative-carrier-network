package Utils;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.SchrimpfFactory;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl.Builder;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.util.Solutions;

import Agent.CarrierAgent;
import Auction.TransportRequest;
import UIResource.HTTPResource.HTTPRequests;

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

   private VehicleRoutingProblem problem;

   private VehicleRoutingProblemSolution bestSolution;

   private List<Shipment> requests;

   private CostCalculator cost;

   
   // // Initial a tour with depot location and username or username
   // public TourPlanning(Location depot, String username) {
   //    this.depot = depot;
   //    this.username = username;
   // }


   /**
    * Initial a tour with agent ID
    */
   public TourPlanning(String username) {
      this.agent = (CarrierAgent) HTTPRequests.getAgent(username);
      setDepot(agent.getDepotX(), agent.getDepotY());
      // this.costPerDistance = agent.getCostPerDistance();
      // this.fixedCost = agent.getFixedCost();
      // this.internalCost = agent.getInternalCost();
      // this.loadingCost = agent.getLoadingCost();
      refreshRequests();
   }


   /**
    * Refresh list of request
    */
   public void refreshRequests() {
      this.requests = HTTPRequests.getUserTransportRequests(agent.getUsername());
   }


   /**
    * Add new request to request list of current tour
    */
   public void addRequest(TransportRequest request) {
      requests.add(request.getShipmentObj());
   }


   /**
    * Visualize the optimal tour base on current list of request
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
    * Build a vehicle with vehicle type, capacity, username and set it to depot location
    */
   private void vehicleRegister() {
      
		final int WEIGHT_INDEX = 0;
		VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance("MiniCooper").addCapacityDimension(WEIGHT_INDEX, 2);
		VehicleType vehicleType = vehicleTypeBuilder.build();
      
      Builder vehicleBuilder = VehicleImpl.Builder.newInstance(agent.getUsername());
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
      for (Shipment request : requests) {
         vrpBuilder.addJob(request);
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
      tourOptimize();
      cost = new CostCalculator(this);
   }


   /**
    * Get profit of a certain request on tour
    */
   public double getProfit(TransportRequest request) {
      setCost();
      return cost.profit(request.getShipmentObj());
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
    * Get total earning that carrier will get from customers after delivering all request
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
    * Get the earning that carrier will get from customer for delivering a certain request
    */
   public double getTransportCostIn(TransportRequest request) {
      setCost();
      return cost.transportCostIn(request.getPickup(), request.getDelivery());
   }

   
   /**
    * Get the cost that carrier has to pay for delivering a certain request while on tour
    */
   public double getTransportCostOut(TransportRequest request) {
      setCost();
      return cost.transportCostOut(request.getId());
   }

   
   /**
    * Get the total length of current tour
    */
   public double getTourLength() {
      setCost();
      return cost.tourLength();
   }


   /**
    * Basic getters and setters needed
    */

   public double getCostPerDistance() {
      return this.costPerDistance;
   }

   // public void setCostPerDistance(double costPerDistance) {
   //    this.costPerDistance = costPerDistance;
   // }

   public double getFixedCost() {
      return this.fixedCost;
   }

   // public void setFixedCost(double fixCost) {
   //    this.fixedCost = fixCost;
   // }

   public double getInternalCost() {
      return this.internalCost;
   }

   // public void setInternalCost(double internalCost) {
   //    this.internalCost = internalCost;
   // }

   public double getLoadingCost() {
      return this.loadingCost;
   }

   // public void setLoadingCost(double loadingCost) {
   //    this.loadingCost = loadingCost;
   // }

   public Location getDepot() {
      return this.depot;
   }

   public void setDepot(double depotX, double depotY) {
      this.depot = Location.newInstance(depotX, depotY);
   }

   public VehicleRoutingProblemSolution getBestSolution() {
      return this.bestSolution;
   }

   public List<Shipment> getRequests() {
      return this.requests;
   }
   
}
