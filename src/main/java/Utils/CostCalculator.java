package Utils;

import java.util.List;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity.JobActivity;
import com.graphhopper.jsprit.core.util.EuclideanDistanceCalculator;

import Auction.TransportRequest;

public class CostCalculator {
    private TourPlanning currentTour;
    private List<TransportRequest> requests;


    /**
     * Initial the calculation based on current tour
     */
    public CostCalculator(TourPlanning currentTour) {
        this.currentTour = currentTour;
        this.requests = currentTour.getRequests();
    }


    /**
     * The total of actual earning after delivering all request (Sum of revenues)
     */
    public double revenueSum() {
        double total = 0;
        for (TransportRequest request : requests) {
            total += profit(request.getShipmentObj());
        }
        return total;
    }


    /**
     * The actual profit for delivering a certain request while on tour
     */
    public double profit(Shipment request) {
        return transportCostIn(request.getPickupLocation(), request.getDeliveryLocation()) - transportCostOut(request.getId());
    }


    /**
     * The current total revenue
     */
    public double revenueTotal() {
        return totalIn() - totalOut();
    }


    /**
     * The total earning that carrier will get from customers after delivering all request
     */
    public double totalIn() {
        double total = 0;
        for (TransportRequest request : requests) {
            total += transportCostIn(request.getPickup(), request.getDelivery());
        }
        return total;
    }


    /**
     * The total cost that carrier has to pay for delivering all request on a tour
     */
    public double totalOut() {
        return requests.size() * currentTour.getLoadingCost() + tourLength() * currentTour.getInternalCost();
    }


    /**
     * The earning that carrier will get from customer for delivering a certain request
     */
    public double transportCostIn(Location pickup, Location deliver) {
        double distance;
        try {
            distance = EuclideanDistanceCalculator.calculateDistance(pickup.getCoordinate(), deliver.getCoordinate());
        } catch (NullPointerException npe) {
            throw new NullPointerException("Cannot calculate euclidean distance. Coordinates are missing.");
        }
        return distance * currentTour.getCostPerDistance() + currentTour.getFixedCost();
    }


    /**
     * The cost that carrier has to pay for delivering a certain request while on tour
     */
    public double transportCostOut(String requestID) {
        String jobID;
        double distance = 0;
        for (VehicleRoute route : currentTour.getBestSolution().getRoutes()) {
            Location pickup = route.getStart().getLocation();
            Location deliver;
            for (TourActivity act : route.getActivities()) {
                if (act instanceof JobActivity) {
                    jobID = ((JobActivity) act).getJob().getId();
                    deliver = act.getLocation();
                    distance += EuclideanDistanceCalculator.calculateDistance(pickup.getCoordinate(), deliver.getCoordinate());
                    if (jobID == requestID && act.getName() == "deliverShipment") {
                        break;
                    }
                    pickup = deliver;
                }
            }
        }
        return distance * currentTour.getInternalCost() + currentTour.getLoadingCost();
    }


    /**
     * Total tour length
     */
    public double tourLength() {
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
