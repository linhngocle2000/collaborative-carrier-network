package main.java.agent;

import java.util.List;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.util.EuclideanDistanceCalculator;

public class CostCalculator {
   private TourPlanning currentTour;

   public CostCalculator(TourPlanning currentTour) {
      this.currentTour = currentTour;
   }

   private List<Shipment> requests = currentTour.getRequests();

   // The total of actual earning after delivering all request (Sum of revenues)
   private double revenueSum() {
      double total = 0;
      for (Shipment request : requests) {
         total += earning(request);
      }
      return total;
   }

   // The current total revenue
   private double revenueTotal() {
      return totalIn() - totalOut();
   }

   // The earning that carrier will get from customers after delivering all request
   private double totalIn() {
      double total = 0;
      for (Shipment request : requests) {
         total += getTransportCostIn(request.getPickupLocation(), request.getDeliveryLocation());
      }
      return total;
   }

   // The total cost that carrier has to pay for whole tour
   private double totalOut() {
      return requests.size() * currentTour.getLoadingCost() + getTourLength() * currentTour.getInternalCost();
   }

   // The actual earning for delivering a certain request
   private double earning(Shipment request) {
      return getTransportCostIn(request.getPickupLocation(), request.getDeliveryLocation()) - getTransportCostOut(request.getId());
   }

   // The earning that carrier will get from customer for delivering a certain request
   private double getTransportCostIn(Location pickup, Location deliver) {
		double distance;
		try {
			distance = EuclideanDistanceCalculator.calculateDistance(pickup.getCoordinate(), deliver.getCoordinate());
		} catch (NullPointerException npe) {
			throw new NullPointerException("Cannot calculate euclidean distance. Coordinates are missing.");
		}
		// double cost = vehicle.getType().getVehicleCostParams().fix + distance;
		// if (vehicle != null && vehicle.getType() != null) {
		// 	cost = vehicle.getType().getVehicleCostParams().fix + distance * vehicle.getType().getVehicleCostParams().perDistanceUnit;
		// }
		// return cost;
      return distance * currentTour.getCostPerDistance() + currentTour.getFixedCost();
	}

   // The cost that carrier has to pay for delivering a certain request
   private double getTransportCostOut(String requestID) {
		String jobID;
		double distance = 0;
		for (VehicleRoute route : currentTour.getBestSolution().getRoutes()) {
   		Location pickup = route.getStart().getLocation();
   		Location deliver;
			for (TourActivity act : route.getActivities()) {
				jobID = ((TourActivity.JobActivity) act).getJob().getId();
				deliver = act.getLocation();
				distance += EuclideanDistanceCalculator.calculateDistance(pickup.getCoordinate(), deliver.getCoordinate());
				if (jobID == requestID && act.getName() == "deliverShipment") {
					break;
				}
				pickup = deliver;
			}
		}
		return distance * currentTour.getInternalCost() + currentTour.getLoadingCost();
	}

   // Total tour length
	private double getTourLength() {
		double distance = 0;
		for (VehicleRoute route : currentTour.getBestSolution().getRoutes()) {
   		Location pickup = route.getStart().getLocation();
   		Location deliver;
			for (TourActivity act : route.getActivities()) {
				deliver = act.getLocation();
				distance += EuclideanDistanceCalculator.calculateDistance(pickup.getCoordinate(), deliver.getCoordinate());
				pickup = deliver;
			}
		}
		return distance;
	}
   
}
