package Utils;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.SchrimpfFactory;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Activity;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.DeliveryActivity;
import com.graphhopper.jsprit.core.problem.solution.route.activity.PickupActivity;

import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl.Builder;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.Time;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TourVisual {

    private Location depot;
    private String vehicleID;

    private VehicleImpl vehicle;

    private VehicleRoutingProblem problem;

    private VehicleRoutingProblemSolution bestSolution;

    private List<Shipment> requests = new ArrayList<>();

    public TourVisual(Location depot, String vehicleID) {
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

    }

    public JPanel visualize() {
        vehicleRegister();
        setProblem();
        tourOptimize();
        return new VisualView(problem, bestSolution).display();
    }

    public VehicleRoutingProblemSolution getBestSolution() {
        return this.bestSolution;
    }

}



