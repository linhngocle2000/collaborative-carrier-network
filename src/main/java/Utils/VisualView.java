package Utils;

import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Activity;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.DeliveryActivity;
import com.graphhopper.jsprit.core.problem.solution.route.activity.PickupActivity;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity.JobActivity;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
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


public class VisualView {

    private String STYLESHEET =   "node {" +
            "	size: 10px, 10px;" +
            "  fill-color: green;" +
            "	text-alignment: at-right;" +
            " 	stroke-mode: plain;" +
            "	stroke-color: #999;" +
            "	stroke-width: 1.0;" +
            "	text-font: couriernew;" +
            " 	text-offset: 2,-5;" +
            "	text-size: 8;" +
            "}" +
            "node.pickup {" +
            " 	fill-color: green;" +
            "}" +
            "node.delivery {" +
            " 	fill-color: blue;" +
            "}" +
            "node.pickupInRoute {" +
            "	fill-color: green;" +
            " 	stroke-mode: plain;" +
            "	stroke-color: #333;" +
            "  stroke-width: 2.0;" +
            "}" +
            "node.deliveryInRoute {" +
            " 	fill-color: blue;" +
            " 	stroke-mode: plain;" +
            "	stroke-color: #333;" +
            "  stroke-width: 2.0;" +
            "}" +
            "node.depot {" +
            " 	fill-color: red;" +
            "	size: 10px, 10px;" +
            " 	shape: box;" +
            "}" +
            "node.removed {" +
            " 	fill-color: red;" +
            "	size: 10px, 10px;" +
            " 	stroke-mode: plain;" +
            "	stroke-color: #333;" +
            "  stroke-width: 2.0;" +
            "}" +
            "edge {" +
            "	fill-color: #333;" +
            "	arrow-size: 6px,3px;" +
            "}" +
            "edge.shipment {" +
            "	fill-color: #999;" +
            "	arrow-size: 6px,3px;" +
            "}";

    static Graph createMultiGraph(String name, String style) {
        Graph g = new MultiGraph(name);
        g.addAttribute("ui.quality");
        g.addAttribute("ui.antialias");
        g.addAttribute("ui.stylesheet", style);
        return g;
    }

    private ViewPanel createEmbeddedView(Graph graph) {
        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        ViewPanel view = viewer.addDefaultView(false);
        view.setPreferredSize(new Dimension((int) (570 * scaling), (int) (450 * scaling)));
        return view;
    }

    public enum Label {
        NO_LABEL, ID, JOB_NAME, ARRIVAL_TIME, DEPARTURE_TIME, ACTIVITY
    }

    private static class Center {
        final double x;
        final double y;

        Center(double x, double y) {
            super();
            this.x = x;
            this.y = y;
        }

    }

    private Label label = Label.NO_LABEL;

    private long renderDelay_in_ms = 0;

    private boolean renderShipments = false;

    private Center center;

    private VehicleRoutingProblem vrp;

    private VehicleRoutingProblemSolution solution;

    private double zoomFactor;

    private double scaling = 1.0;


    public VisualView(VehicleRoutingProblem vrp) {
        super();
        this.vrp = vrp;
    }

    public VisualView(VehicleRoutingProblem vrp, VehicleRoutingProblemSolution solution) {
        super();
        this.vrp = vrp;
        this.solution = solution;
    }

    public VisualView labelWith(Label label) {
        this.label = label;
        return this;
    }

    public VisualView setRenderDelay(long ms) {
        this.renderDelay_in_ms = ms;
        return this;
    }

    public VisualView setRenderShipments(boolean renderShipments) {
        this.renderShipments = renderShipments;
        return this;
    }

    public VisualView setGraphStreamFrameScalingFactor(double factor) {
        this.scaling = factor;
        return this;
    }

    /**
     * Sets the camera-view. Center describes the center-focus of the camera and zoomFactor its
     * zoomFactor.
     * <p>
     * <p>a zoomFactor < 1 zooms in and > 1 out.
     *
     * @param centerX    x coordinate of center
     * @param centerY    y coordinate of center
     * @param zoomFactor zoom factor
     * @return the viewer
     */
    public VisualView setCameraView(double centerX, double centerY, double zoomFactor) {
        center = new Center(centerX, centerY);
        this.zoomFactor = zoomFactor;
        return this;
    }

    public JPanel display() {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Graph g = createMultiGraph("g", STYLESHEET);
        ViewPanel view = createEmbeddedView(g);
        render(g, view);
        return createGraphPanel(view);
    }

    private JPanel createGraphPanel(ViewPanel view) {
        JPanel tourPanel = createTourPanel();
        JPanel graphPanel = new JPanel();
        graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
        graphPanel.setPreferredSize(new Dimension((int) (570 * scaling), (int) (460 * scaling)));

        JPanel graphStreamPanel = new JPanel();
        graphStreamPanel.setPreferredSize(new Dimension((int) (550 * scaling), (int) (450 * scaling)));

        graphStreamPanel.add(view);
        graphPanel.add(graphStreamPanel);
        graphPanel.add(tourPanel);

        return graphPanel;
    }

    private void render(Graph g, ViewPanel view) {
        if (center != null) {
            view.resizeFrame(view.getWidth(), view.getHeight());
            alignCamera(view);
        }

        for (Vehicle vehicle : vrp.getVehicles()) {
            renderVehicle(g, vehicle, label);
            sleep(renderDelay_in_ms);
        }

        for (Job j : vrp.getJobs().values()) {
            renderJob(g, j, label);
            sleep(renderDelay_in_ms);
        }

        if (solution != null) {
            int routeId = 1;
            for (VehicleRoute route : solution.getRoutes()) {
                renderRoute(g, route, routeId, renderDelay_in_ms, label);
                sleep(renderDelay_in_ms);
                routeId++;
            }
        }
    }

    private JPanel createTourPanel() {
        int width = 570;
        int height = 100;

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension((int) (width * scaling), (int) (height * scaling)));

        JPanel subpanel = new JPanel();
        subpanel.setLayout(new FlowLayout());
        subpanel.setPreferredSize(new Dimension((int) (550 * scaling), (int) (100 * scaling)));
        subpanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        Font font = Font.decode("couriernew");
        int i = 0;
        String tour = "<HTML>";
        int j = 0;
        for (VehicleRoute route : solution.getRoutes()) {
            if (i==0) {
                tour += "[Start] (" + route.getVehicle().getStartLocation().getCoordinate().getX() + "," + route.getVehicle().getStartLocation().getCoordinate().getY()+")";
                i = 1;
                j++;
            }

            for (TourActivity act : route.getActivities()) {
                tour += " \u2192 ";
                if (act.getName().equals("pickupShipment")) {
                    tour += "[Pickup] ("+ act.getLocation().getCoordinate().getX()+","+act.getLocation().getCoordinate().getY()+")";
                    j++;
                } else {
                    tour += "[Deliver] ("+ act.getLocation().getCoordinate().getX()+","+act.getLocation().getCoordinate().getY()+")";
                    j++;
                }
            }

        }
        tour += "</HTML>";


        JTextPane tourTA = new JTextPane();
        tourTA.setEditable(false);
        tourTA.setForeground(Color.BLACK);
        tourTA.setFont(font);
        tourTA.setContentType("text/html");
        tourTA.setText(tour);
        JScrollPane jsp = new JScrollPane(tourTA);
        jsp.setMinimumSize(new Dimension(550, 100));
        jsp.setPreferredSize(new Dimension(550, 100));

        panel.add(jsp);

        return panel;
    }

    private void alignCamera(View view) {
        view.getCamera().setViewCenter(center.x, center.y, 0);
        view.getCamera().setViewPercent(zoomFactor);
    }

    private void renderJob(Graph g, Job j, Label label) {
        String lastNodeId = null;
        for (Activity act : j.getActivities()) {
            String nodeId = makeId(j.getId(), act.getLocation().getId());
            Node n1 = g.addNode(nodeId);
            if (label.equals(Label.ID)) n1.addAttribute("ui.label", j.getId());
            n1.addAttribute("x", act.getLocation().getCoordinate().getX());
            n1.addAttribute("y", act.getLocation().getCoordinate().getY());
            if (act.getActivityType().equals(Activity.Type.PICKUP)) n1.setAttribute("ui.class", "pickup");
            else if (act.getActivityType().equals(Activity.Type.DELIVERY)) n1.setAttribute("ui.class", "delivery");
            if (renderShipments && lastNodeId != null) {
                Edge s = g.addEdge(j.getId(), lastNodeId, nodeId, true);
                s.addAttribute("ui.class", "shipment");
            }
            lastNodeId = nodeId;
        }
    }

    private void sleep(long renderDelay_in_ms2) {
        try {
            Thread.sleep(renderDelay_in_ms2);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String makeId(String id, String locationId) {
        return id + "_" + locationId;
    }

    private void renderVehicle(Graph g, Vehicle vehicle, Label label) {
        String nodeId = makeId(vehicle.getId(), vehicle.getStartLocation().getId());
        Node vehicleStart = g.addNode(nodeId);
        if (label.equals(Label.ID)) vehicleStart.addAttribute("ui.label", "depot");
//		if(label.equals(Label.ACTIVITY)) n.addAttribute("ui.label", "start");
        vehicleStart.addAttribute("x", vehicle.getStartLocation().getCoordinate().getX());
        vehicleStart.addAttribute("y", vehicle.getStartLocation().getCoordinate().getY());
        vehicleStart.setAttribute("ui.class", "depot");

        if (!vehicle.getStartLocation().getId().equals(vehicle.getEndLocation().getId())) {
            Node vehicleEnd = g.addNode(makeId(vehicle.getId(), vehicle.getEndLocation().getId()));
            if (label.equals(Label.ID)) vehicleEnd.addAttribute("ui.label", "depot");
            vehicleEnd.addAttribute("x", vehicle.getEndLocation().getCoordinate().getX());
            vehicleEnd.addAttribute("y", vehicle.getEndLocation().getCoordinate().getY());
            vehicleEnd.setAttribute("ui.class", "depot");
        }
    }

    private void renderRoute(Graph g, VehicleRoute route, int routeId, long renderDelay_in_ms, Label label) {
        int vehicle_edgeId = 1;
        String prevIdentifier = makeId(route.getVehicle().getId(), route.getVehicle().getStartLocation().getId());
        if (label.equals(Label.ACTIVITY) || label.equals(Label.JOB_NAME)) {
            Node n = g.getNode(prevIdentifier);
            n.addAttribute("ui.label", "start");
        }
        for (TourActivity act : route.getActivities()) {
            if (act instanceof  JobActivity) {
                Job job = ((JobActivity) act).getJob();
                String currIdentifier = makeId(job.getId(), act.getLocation().getId());
                Node actNode = g.getNode(currIdentifier);
                switch (label) {
                    case ACTIVITY: {
                        actNode.addAttribute("ui.label", act.getName());
                        break;
                    }
                    case JOB_NAME: {
                        actNode.addAttribute("ui.label", job.getName());
                        break;
                    }
                    case ARRIVAL_TIME: {
                        actNode.addAttribute("ui.label", Time.parseSecondsToTime(act.getArrTime()));
                        break;
                    }
                    case DEPARTURE_TIME: {
                        actNode.addAttribute("ui.label", Time.parseSecondsToTime(act.getEndTime()));
                        break;
                    }
                }
                g.addEdge(makeEdgeId(routeId, vehicle_edgeId), prevIdentifier, currIdentifier, true);
                if (act instanceof PickupActivity) g.getNode(currIdentifier).addAttribute("ui.class", "pickupInRoute");
                else if (act instanceof DeliveryActivity)
                    g.getNode(currIdentifier).addAttribute("ui.class", "deliveryInRoute");
                prevIdentifier = currIdentifier;
                vehicle_edgeId++;
                sleep(renderDelay_in_ms);
            }
        }
        if (route.getVehicle().isReturnToDepot()) {
            String lastIdentifier = makeId(route.getVehicle().getId(), route.getVehicle().getEndLocation().getId());
            g.addEdge(makeEdgeId(routeId, vehicle_edgeId), prevIdentifier, lastIdentifier, true);
        }
    }

    private String makeEdgeId(int routeId, int vehicle_edgeId) {
        return Integer.valueOf(routeId).toString() + "." + Integer.valueOf(vehicle_edgeId).toString();
    }

}



