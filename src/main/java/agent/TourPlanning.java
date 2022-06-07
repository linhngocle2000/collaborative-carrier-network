package main.java.agent;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.SchrimpfFactory;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl.Builder;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.util.EuclideanDistanceCalculator;
import com.graphhopper.jsprit.core.util.Solutions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

public class TourPlanning {
   private double costPerDistance = 1.0;
   private double fixedCost = 0.0;
   private double internalCost = 0.0;
   private double loadingCost = 0.0;
   private Location depot;
   private String vehicleID;
   
   private VehicleImpl vehicle;

   private VehicleRoutingProblem problem;

   private VehicleRoutingProblemSolution bestSolution;

   private List<Shipment> requests = new ArrayList<>();

   public TourPlanning(Location depot, String vehicleID) {
      this.depot = depot;
      this.vehicleID = vehicleID;
   }

   public void addRequest(String requestID, Location pickup, Location deliver) {
      requests.add(Shipment.Builder.newInstance(requestID).setPickupLocation(pickup).setDeliveryLocation(deliver).build());
   }

   private void vehicleRegister() {

      /*
		 * get a vehicle type-builder and build a type with the typeId "vehicleType"
		 */
		final int WEIGHT_INDEX = 0;
		VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance("MiniCooper").addCapacityDimension(WEIGHT_INDEX, 2);
		VehicleType vehicleType = vehicleTypeBuilder.build();
		
		/*
		 * get a vehicle-builder and build a vehicle located at depot location with type "vehicleType"
		 */
      Builder vehicleBuilder = VehicleImpl.Builder.newInstance(vehicleID);
		vehicleBuilder.setStartLocation(depot);
		vehicleBuilder.setType(vehicleType);
		vehicle = vehicleBuilder.build();
  
   }

   private void setProblem() {
      // List<Shipment> requests = TransportRequest.generateRequest(new ArrayList<>());
      
      VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
		vrpBuilder.addVehicle(vehicle);
      for (Shipment request : requests) {
         vrpBuilder.addJob(request);
      }
		
		problem = vrpBuilder.build();
   }
   

   private void tourOptimize() {
      VehicleRoutingAlgorithm algorithm = new SchrimpfFactory().createAlgorithm(problem);
      Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
      bestSolution = Solutions.bestOf(solutions);
      
		// SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);
   }

   public JPanel visualize() {
      vehicleRegister();
      setProblem();
      tourOptimize();
      // CostCalculator cost = new CostCalculator(this);
      return new VisualView(problem, bestSolution).display();
   }

   public double getCostPerDistance() {
      return this.costPerDistance;
   }

   public void setCostPerDistance(double costPerDistance) {
      this.costPerDistance = costPerDistance;
   }

   public double getFixedCost() {
      return this.fixedCost;
   }

   public void setFixedCost(double fixCost) {
      this.fixedCost = fixCost;
   }

   public double getInternalCost() {
      return this.internalCost;
   }

   public void setInternalCost(double internalCost) {
      this.internalCost = internalCost;
   }

   public double getLoadingCost() {
      return this.loadingCost;
   }

   public void setLoadingCost(double loadingCost) {
      this.loadingCost = loadingCost;
   }

   public Location getDepot() {
      return this.depot;
   }

   public void setDepot(Location depot) {
      this.depot = depot;
   }

   public VehicleRoutingProblemSolution getBestSolution() {
      return this.bestSolution;
   }

   public List<Shipment> getRequests() {
      return this.requests;
   }
   
}
