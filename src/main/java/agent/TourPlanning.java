package main.java.agent;

import com.graphhopper.jsprit.analysis.toolbox.GraphStreamViewer;
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
import com.graphhopper.jsprit.core.reporting.SolutionPrinter;
import com.graphhopper.jsprit.core.util.Coordinate;
import com.graphhopper.jsprit.core.util.Solutions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JPanel;

public class TourPlanning {
   private static VehicleImpl vehicle;
   private static VehicleRoutingProblem problem;
   private static VehicleRoutingProblemSolution bestSolution;
   private static List<Shipment> requests = new ArrayList<>();

   public static void requestRegister(String ID, double pickup_X, double pickup_Y, double deliver_X, double deliver_Y) {
      requests.add(Shipment.Builder.newInstance(ID).setPickupLocation(loc(Coordinate.newInstance(pickup_X, pickup_Y))).setDeliveryLocation(loc(Coordinate.newInstance(deliver_X, deliver_Y))).build());
   }

   private static void vehicleRegister(String vehicleID, double depot_X, double depot_Y) {

      /*
		 * get a vehicle type-builder and build a type with the typeId "vehicleType"
		 */
		VehicleTypeImpl.Builder vehicleTypeBuilder = VehicleTypeImpl.Builder.newInstance("Lambo");
		VehicleType vehicleType = vehicleTypeBuilder.build();
		
		/*
		 * get a vehicle-builder and build a vehicle located at depot location with type "vehicleType"
		 */
      Builder vehicleBuilder = VehicleImpl.Builder.newInstance(vehicleID);
		vehicleBuilder.setStartLocation(loc(Coordinate.newInstance(depot_X, depot_Y)));
		vehicleBuilder.setType(vehicleType);
		vehicle = vehicleBuilder.build();
  
   }

   private static void setProblem() {
      List<Shipment> requests = TransportRequest.generateRequest(new ArrayList<>());
      
      VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
		vrpBuilder.addVehicle(vehicle);
      for (Shipment request : requests) {
         vrpBuilder.addJob(request);
      }
		
		problem = vrpBuilder.build();
   }

   private static void tourOptimize() {
      VehicleRoutingAlgorithm algorithm = new SchrimpfFactory().createAlgorithm(problem);
      Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
      bestSolution = Solutions.bestOf(solutions);
      
		// SolutionPrinter.print(problem, bestSolution, SolutionPrinter.Print.VERBOSE);
   }

   public static JPanel tourVisualize(String vehicleID, double depot_X, double depot_Y) {
      vehicleRegister(vehicleID, depot_X, depot_Y);
      setProblem();
      tourOptimize();
      return new VisualView(problem, bestSolution).display();
   }

   private static Location loc(Coordinate coordinate) {
      return Location.Builder.newInstance().setCoordinate(coordinate).build();
   }
}
