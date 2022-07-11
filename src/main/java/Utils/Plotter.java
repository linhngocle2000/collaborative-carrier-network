package Utils;

/*
 * Licensed to GraphHopper GmbH under one or more contributor
 * license agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * GraphHopper GmbH licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.util.Coordinate;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.VectorRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.Range;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.ShapeUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.*;


/**
 * Visualizes problem and solution.
 * <p>Note that every item to be rendered need to have coordinates.
 *
 * @author schroeder
 */
public class Plotter {

    private final static Color START_COLOR = Color.RED;
    private final static Color END_COLOR = Color.RED;
    private final static Color PICKUP_COLOR = Color.GREEN;
    private final static Color DELIVERY_COLOR = Color.BLUE;
    private final static Color SERVICE_COLOR = Color.BLUE;

    private final static Shape ELLIPSE = new Ellipse2D.Double(-6, -6, 12, 12);

    private class XYVectorizedRenderer extends XYLineAndShapeRenderer {
		private static final long serialVersionUID = 1L;
		
		
		public XYVectorizedRenderer(boolean lines, boolean shapes) {
			super(lines, shapes);
		}
		
		
		public boolean getDrawSeriesLineAsPath() {
			return false;
		}
		
		
		public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
				ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, int series, int item, CrosshairState crosshairState, int pass) {
			
			// do nothing if item is not visible
			if(!getItemVisible(series, item)) {
				return;
			}
			
			// first pass draws the background (lines, for instance)
			if(isLinePass(pass)) {
				if(getItemLineVisible(series, item)) {
					drawPrimaryLine(state, g2, plot, dataset, pass, series,
							item, domainAxis, rangeAxis, dataArea);
				}
			}
			// second pass adds shapes where the items are ..
			else if(isItemPass(pass)) {
				// setup for collecting optional entity info...
				EntityCollection entities = null;
				if(info != null && info.getOwner() != null) {
					entities = info.getOwner().getEntityCollection();
				}
				
				drawSecondaryPass(g2, plot, dataset, pass, series, item,
						domainAxis, dataArea, rangeAxis, crosshairState, entities);
			}
		}
		
		protected void drawPrimaryLine(XYItemRendererState state, Graphics2D g2, XYPlot plot, XYDataset dataset, int pass, int series, int item, ValueAxis domainAxis, ValueAxis rangeAxis, Rectangle2D dataArea) {
            if(item == 0) {
                return;
            }
            
            // get the data points
            double x1 = dataset.getXValue(series, item);
            double y1 = dataset.getYValue(series, item);
            if(Double.isNaN(y1) || Double.isNaN(x1)) {
                return;
            }
            
            double x0 = dataset.getXValue(series, item - 1);
            double y0 = dataset.getYValue(series, item - 1);
            if(Double.isNaN(y0) || Double.isNaN(x0)) {
                return;
            }
            
            // Works only if the XYSeries is NOT autosorted !!
            double vectorOrientation = x1 - x0;
            
            RectangleEdge xAxisLocation = plot.getDomainAxisEdge();
            RectangleEdge yAxisLocation = plot.getRangeAxisEdge();
            
            double pxx0 = domainAxis.valueToJava2D(x0, dataArea, xAxisLocation);
            double pxy0 = rangeAxis.valueToJava2D(y0, dataArea, yAxisLocation);
            
            double pxx1 = domainAxis.valueToJava2D(x1, dataArea, xAxisLocation);
            double pxy1 = rangeAxis.valueToJava2D(y1, dataArea, yAxisLocation);
            
            // Draw the line between points
            Line2D line;
            PlotOrientation orientation = plot.getOrientation();
            if(orientation.equals(PlotOrientation.HORIZONTAL)) {
                line = new Line2D.Double(pxy0, pxx0, pxy1, pxx1);
            }
            else {
                line = new Line2D.Double(pxx0, pxy0, pxx1, pxy1);
            }
            g2.setPaint(getItemPaint(series, item));
            g2.setStroke(getItemStroke(series, item));
            g2.draw(line);
            
            // Calculate the arrow angle in radians
            double dxx = (pxx1 - pxx0);
            double dyy = (pxy1 - pxy0);
            
            double angle = 0.0;
            if(dxx != 0.0) {
                angle = Math.PI / 2.0 - Math.atan(dyy / dxx);
            }
            
            
            // V2 : Draw arrow shape with fixed size instead of line path
            int arrowHeight = 12;
            int arrowBase = arrowHeight/3;
            
            int px_x = (int) pxx1;
            int px_y = (int) pxy1;
            
            // Create the arrow shape
            int[] xpoints = new int[] {px_x, px_x+arrowBase, px_x-arrowBase};
            int[] ypoints = new int[] {px_y, px_y+arrowHeight, px_y+arrowHeight};
            Polygon arrow = new Polygon(xpoints, ypoints, 3);
            // int[] xpoints = new int[] {px_x, px_x+arrowBase, px_x, px_x-arrowBase};
            // int[] ypoints = new int[] {px_y, px_y+arrowHeight, px_y+2*arrowHeight/3, px_y+arrowHeight};
            // Polygon arrow = new Polygon(xpoints, ypoints, 4);
            
            // Convert shape to path
            Path2D arrowPath = new Path2D.Double(arrow);
            
            // Apply rotation with AffineTransform to the path
            Rectangle bounds = arrowPath.getBounds();
            Point center = new Point(bounds.x + bounds.width/2, bounds.y + bounds.height/2);
            
            double rotationThetaRad = -angle + (vectorOrientation > 0 ? Math.PI : 0);
            arrowPath.transform(AffineTransform.getRotateInstance(rotationThetaRad, px_x, px_y));
            
            
            // Draw the path
            //g2.setPaint(vectorOrientation > 0 ? Color.RED : Color.GREEN);
            g2.fill(arrowPath);
            if(g2.getPaint() instanceof Color) {
                g2.setPaint(((Color)g2.getPaint()).darker());
                g2.draw(arrowPath);
            }
        }
	}

    private static class MyActivityRenderer extends XYLineAndShapeRenderer {

        /**
         *
         */
        private static final long serialVersionUID = 1L;

        private XYSeriesCollection seriesCollection;

        private Map<XYDataItem, Activity> activities;

        private Set<XYDataItem> firstActivities;

        MyActivityRenderer(XYSeriesCollection seriesCollection, Map<XYDataItem, Activity> activities, Set<XYDataItem> firstActivities) {
            super(false, true);
            this.seriesCollection = seriesCollection;
            this.activities = activities;
            this.firstActivities = firstActivities;
            super.setSeriesOutlinePaint(0, Color.DARK_GRAY);
            super.setUseOutlinePaint(true);
        }

        @Override
        public Shape getItemShape(int seriesIndex, int itemIndex) {
            XYDataItem dataItem = seriesCollection.getSeries(seriesIndex).getDataItem(itemIndex);
            if (firstActivities.contains(dataItem)) {
                return ShapeUtilities.createUpTriangle(6.0f);
            }
            return ELLIPSE;
        }

        @Override
        public Paint getItemOutlinePaint(int seriesIndex, int itemIndex) {
            XYDataItem dataItem = seriesCollection.getSeries(seriesIndex).getDataItem(itemIndex);
            if (firstActivities.contains(dataItem)) {
                return Color.BLACK;
            }
            return super.getItemOutlinePaint(seriesIndex, itemIndex);
        }

        @Override
        public Paint getItemPaint(int seriesIndex, int itemIndex) {
            XYDataItem dataItem = seriesCollection.getSeries(seriesIndex).getDataItem(itemIndex);
            Activity activity = activities.get(dataItem);
            if (activity.equals(Activity.PICKUP)) return PICKUP_COLOR;
            if (activity.equals(Activity.DELIVERY)) return DELIVERY_COLOR;
            if (activity.equals(Activity.SERVICE)) return SERVICE_COLOR;
            if (activity.equals(Activity.START)) return START_COLOR;
            if (activity.equals(Activity.END)) return END_COLOR;
            throw new IllegalStateException("activity at " + dataItem.toString() + " cannot be assigned to a color");
        }

    }

    private static class BoundingBox {
        double minX;
        double minY;
        double maxX;
        double maxY;

        BoundingBox(double minX, double minY, double maxX, double maxY) {
            super();
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

    }

    private enum Activity {
        START, END, PICKUP, DELIVERY, SERVICE
    }


    private static Logger log = LoggerFactory.getLogger(Plotter.class);

    /**
     * Label to label ID (=jobId), SIZE (=jobSize=jobCapacityDimensions)
     *
     * @author schroeder
     */
    public enum Label {
        ID, SIZE, @SuppressWarnings("UnusedDeclaration")NO_LABEL
    }

    private Label label = Label.ID;

    private VehicleRoutingProblem vrp;

    private boolean plotSolutionAsWell = false;

    private boolean plotShipments = true;

    private Collection<VehicleRoute> routes;

    private BoundingBox boundingBox = null;

    private Map<XYDataItem, Activity> activitiesByDataItem = new HashMap<>();

    private Map<XYDataItem, String> labelsByDataItem = new HashMap<>();

    private XYSeries activities;

    private Set<XYDataItem> firstActivities = new HashSet<>();

    private boolean containsPickupAct = false;

    private boolean containsDeliveryAct = false;

    private boolean containsServiceAct = false;

    private double scalingFactor = 1.;

    private boolean invert = false;

    /**
     * Constructs Plotter with problem. Thus only the problem can be rendered.
     *
     * @param vrp the routing problem
     */
    public Plotter(VehicleRoutingProblem vrp) {
        super();
        this.vrp = vrp;
    }

    /**
     * Constructs Plotter with problem and solution to render them both.
     *
     * @param vrp      the routing problem
     * @param solution the solution
     */
    public Plotter(VehicleRoutingProblem vrp, VehicleRoutingProblemSolution solution) {
        super();
        this.vrp = vrp;
        this.routes = solution.getRoutes();
        plotSolutionAsWell = true;
    }

    /**
     * Constructs Plotter with problem and routes to render individual routes.
     *
     * @param vrp    the routing problem
     * @param routes routes
     */
    public Plotter(VehicleRoutingProblem vrp, Collection<VehicleRoute> routes) {
        super();
        this.vrp = vrp;
        this.routes = routes;
        plotSolutionAsWell = true;
    }

    @SuppressWarnings("UnusedDeclaration")
    public Plotter setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
        return this;
    }

    /**
     * Sets a label.
     *
     * @param label of jobs
     * @return plotter
     */
    public Plotter setLabel(Label label) {
        this.label = label;
        return this;
    }

    public Plotter invertCoordinates(boolean invert) {
        this.invert = invert;
        return this;
    }

    /**
     * Sets a bounding box to zoom in to certain areas.
     *
     * @param minX lower left x
     * @param minY lower left y
     * @param maxX upper right x
     * @param maxY upper right y
     * @return Plotter
     */
    @SuppressWarnings("UnusedDeclaration")
    public Plotter setBoundingBox(double minX, double minY, double maxX, double maxY) {
        boundingBox = new BoundingBox(minX, minY, maxX, maxY);
        return this;
    }

    /**
     * Flag that indicates whether shipments should be rendered as well.
     *
     * @param plotShipments flag to plot shipment
     * @return the plotter
     */
    public Plotter plotShipments(boolean plotShipments) {
        this.plotShipments = plotShipments;
        return this;
    }

    /**
     * Plots problem and/or solution/routes.
     *
     * @param pngFileName - path and filename
     * @param plotTitle   - title that appears on top of image
     * @return BufferedImage image to write
     */
    public BufferedImage plot(String plotTitle) {
        // String filename = pngFileName;
        // if (!pngFileName.endsWith(".png")) filename += ".png";
        if (plotSolutionAsWell) {
            return plot(vrp, routes, plotTitle);
        } else if (!(vrp.getInitialVehicleRoutes().isEmpty())) {
            return plot(vrp, vrp.getInitialVehicleRoutes(), plotTitle);
        } else {
            return plot(vrp, null, plotTitle);
        }
    }

    private BufferedImage plot(VehicleRoutingProblem vrp, final Collection<VehicleRoute> routes, String title) {
        // log.info("plot to {}", pngFile);
        XYSeriesCollection problem;
        XYSeriesCollection solution = null;
        final XYSeriesCollection shipments;
        try {
            retrieveActivities(vrp);
            problem = new XYSeriesCollection(activities);
            shipments = makeShipmentSeries(vrp.getJobs().values());
            if (routes != null) solution = makeSolutionSeries(vrp, routes);
        } catch (Exception e) {
            log.warn("cannot plot vrp, since coord is missing");
            return null;
        }
        final XYPlot plot = createPlot(problem, shipments, solution);
        JFreeChart chart = new JFreeChart(title, plot);

        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);

        LegendTitle legend = createLegend(routes, shipments, plot);
        chart.removeLegend();
        chart.addLegend(legend);

        // save(chart, pngFile);
        return chart.createBufferedImage(800, 600);

    }

    private LegendTitle createLegend(final Collection<VehicleRoute> routes, final XYSeriesCollection shipments, final XYPlot plot) {
        LegendItemSource lis = new LegendItemSource() {

            @Override
            public LegendItemCollection getLegendItems() {
                LegendItemCollection lic = new LegendItemCollection();
                addLegendItem(lic, "Depot          ", Color.RED);
                if (containsServiceAct) {
                    addLegendItem(lic, "Service          ", Color.BLUE);
                }
                if (containsPickupAct) {
                    addLegendItem(lic, "Pickup          ", Color.GREEN);
                }
                if (containsDeliveryAct) {
                    addLegendItem(lic, "Delivery          ", Color.BLUE);
                }
                if (routes != null) {
                    LegendItem item = new LegendItem("First Activity          ", Color.GREEN);   
                    Shape upTriangle = ShapeUtilities.createUpTriangle(6.0f);
                    item.setShape(upTriangle);
                    item.setOutlinePaint(Color.BLACK);

                    item.setLine(upTriangle);
                    item.setLinePaint(Color.BLACK);
                    item.setShapeVisible(true);

                    lic.add(item);
                }
                if (!shipments.getSeries().isEmpty()) {
                    lic.add(plot.getRenderer(1).getLegendItem(1, 0));
                }
                // if (routes != null) {
                //     lic.addAll(plot.getRenderer(2).getLegendItems());
                // }
                return lic;
            }

            private void addLegendItem(LegendItemCollection lic, String jobType, Color color) {
                LegendItem item = new LegendItem(jobType, color);
                item.setShape(ELLIPSE);
                item.setShapeVisible(true);
                lic.add(item);
            }
        };

        LegendTitle legend = new LegendTitle(lis);
        legend.setPosition(RectangleEdge.BOTTOM);
        return legend;
    }

    private XYItemRenderer getShipmentRenderer(XYSeriesCollection shipments) {
        XYItemRenderer shipmentsRenderer = new XYLineAndShapeRenderer(true, false);   // Shapes only
        for (int i = 0; i < shipments.getSeriesCount(); i++) {
            shipmentsRenderer.setSeriesPaint(i, Color.DARK_GRAY);
            shipmentsRenderer.setSeriesStroke(i, new BasicStroke(
                    1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                    1.f, new float[]{4.0f, 4.0f}, 0.0f
            ));
        }
        return shipmentsRenderer;
    }

    private MyActivityRenderer getProblemRenderer(final XYSeriesCollection problem) {
        MyActivityRenderer problemRenderer = new MyActivityRenderer(problem, activitiesByDataItem, firstActivities);
        problemRenderer.setBaseItemLabelGenerator((arg0, arg1, arg2) -> {
            XYDataItem item = problem.getSeries(arg1).getDataItem(arg2);
            return labelsByDataItem.get(item);
        });
        problemRenderer.setBaseItemLabelsVisible(true);
        problemRenderer.setBaseItemLabelPaint(Color.BLACK);

        return problemRenderer;
    }

    private Range getRange(final XYSeriesCollection seriesCol) {
        if (this.boundingBox == null) return seriesCol.getRangeBounds(false);
        else return new Range(boundingBox.minY, boundingBox.maxY);
    }

    private Range getDomainRange(final XYSeriesCollection seriesCol) {
        if (this.boundingBox == null) return seriesCol.getDomainBounds(true);
        else return new Range(boundingBox.minX, boundingBox.maxX);
    }

    private XYPlot createPlot(final XYSeriesCollection problem, XYSeriesCollection shipments, XYSeriesCollection solution) {
        XYPlot plot = new XYPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);

        XYLineAndShapeRenderer problemRenderer = getProblemRenderer(problem);
        plot.setDataset(0, problem);
        plot.setRenderer(0, problemRenderer);

        XYItemRenderer shipmentsRenderer = getShipmentRenderer(shipments);
        plot.setDataset(1, shipments);
        plot.setRenderer(1, shipmentsRenderer);

        if (solution != null) {
            XYItemRenderer solutionRenderer = getRouteRenderer(solution);
            plot.setDataset(2, solution);
            plot.setRenderer(2, solutionRenderer);
        }

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        if (boundingBox == null) {
            xAxis.setRangeWithMargins(getDomainRange(problem));
            yAxis.setRangeWithMargins(getRange(problem));
        } else {
            xAxis.setRangeWithMargins(new Range(boundingBox.minX, boundingBox.maxX));
            yAxis.setRangeWithMargins(new Range(boundingBox.minY, boundingBox.maxY));
        }

        plot.setDomainAxis(xAxis);
        plot.setRangeAxis(yAxis);

        return plot;
    }

    private XYItemRenderer getRouteRenderer(XYSeriesCollection solutionColl) {
        XYItemRenderer solutionRenderer = new XYVectorizedRenderer(true, false);   // Lines only
        solutionRenderer.setSeriesPaint(0, Color.BLACK);
        for (int i = 0; i < solutionColl.getSeriesCount(); i++) {
            XYSeries s = solutionColl.getSeries(i);
            XYDataItem firstCustomer = s.getDataItem(1);
            firstActivities.add(firstCustomer);
        }
        return solutionRenderer;
    }

    // private void save(JFreeChart chart, String pngFile) {
    //     try {
    //         ChartUtilities.saveChartAsPNG(new File(pngFile), chart, 1000, 600);
    //     } catch (IOException e) {
    //         log.error("cannot plot");
    //         log.error(e.toString());
    //         e.printStackTrace();
    //         throw new RuntimeException(e);
    //     }
    // }

    private XYSeriesCollection makeSolutionSeries(VehicleRoutingProblem vrp, Collection<VehicleRoute> routes) {
        Map<String, Coordinate> coords = makeMap(vrp.getAllLocations());
        XYSeriesCollection coll = new XYSeriesCollection();
        int counter = 1;
        for (VehicleRoute route : routes) {
            if (route.isEmpty()) continue;
            XYSeries series = new XYSeries(counter, false, true);

            Coordinate startCoord = getCoordinate(coords.get(route.getStart().getLocation().getId()));
            series.add(startCoord.getX() * scalingFactor, startCoord.getY() * scalingFactor);

            for (TourActivity act : route.getTourActivities().getActivities()) {
                Coordinate coord = getCoordinate(coords.get(act.getLocation().getId()));
                series.add(coord.getX() * scalingFactor, coord.getY() * scalingFactor);
            }

            Coordinate endCoord = getCoordinate(coords.get(route.getEnd().getLocation().getId()));
            series.add(endCoord.getX() * scalingFactor, endCoord.getY() * scalingFactor);

            coll.addSeries(series);
            counter++;
        }
        return coll;
    }

    private Map<String, Coordinate> makeMap(Collection<Location> allLocations) {
        Map<String, Coordinate> coords = new HashMap<>();
        for (Location l : allLocations) coords.put(l.getId(), l.getCoordinate());
        return coords;
    }

    private XYSeriesCollection makeShipmentSeries(Collection<Job> jobs) {
        XYSeriesCollection coll = new XYSeriesCollection();
        if (!plotShipments) return coll;
        int sCounter = 1;
        String ship = "shipment";
        boolean first = true;
        for (Job job : jobs) {
            if (job.getActivities().size() == 1) continue;
//            Shipment shipment = (Shipment) job;
            XYSeries shipmentSeries;
            if (first) {
                first = false;
                shipmentSeries = new XYSeries(ship, false, true);
            } else {
                shipmentSeries = new XYSeries(sCounter, false, true);
                sCounter++;
            }
            for (com.graphhopper.jsprit.core.problem.job.Activity act : job.getActivities()) {
                Coordinate actCoordinate = getCoordinate(act.getLocation().getCoordinate());
                shipmentSeries.add(actCoordinate.getX() * scalingFactor, actCoordinate.getY() * scalingFactor);
            }
            coll.addSeries(shipmentSeries);
        }
        return coll;
    }

    private void addJob(XYSeries activities, Job job) {
        for (com.graphhopper.jsprit.core.problem.job.Activity act : job.getActivities()) {
            if (act.getLocation() == null) continue;
            XYDataItem dataItem = new XYDataItem(getCoordinate(act.getLocation().getCoordinate()).getX() * scalingFactor, getCoordinate(act.getLocation().getCoordinate()).getY() * scalingFactor);
            activities.add(dataItem);
            addLabel(job, dataItem);
            switch (act.getActivityType()) {
                case PICKUP:
                    markItem(dataItem, Activity.PICKUP);
                    containsPickupAct = true;
                    break;
                case DELIVERY:
                    markItem(dataItem, Activity.DELIVERY);
                    containsDeliveryAct = true;
                    break;
                case SERVICE:
                    markItem(dataItem, Activity.SERVICE);
                    containsServiceAct = true;
                    break;
            }
        }
    }

    private void addLabel(Job job, XYDataItem dataItem) {
        if (this.label.equals(Label.SIZE)) {
            labelsByDataItem.put(dataItem, getSizeString(job));
        } else if (this.label.equals(Label.ID)) {
            labelsByDataItem.put(dataItem, String.valueOf(job.getId()));
        }
    }

    private void addLabel(XYDataItem dataItem, String value) {
      labelsByDataItem.put(dataItem, value);
  }

    private String getSizeString(Job job) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        boolean firstDim = true;
        for (int i = 0; i < job.getSize().getNuOfDimensions(); i++) {
            if (firstDim) {
                builder.append(job.getSize().get(i));
                firstDim = false;
            } else {
                builder.append(",");
                builder.append(job.getSize().get(i));
            }
        }
        builder.append(")");
        return builder.toString();
    }

    private Coordinate getCoordinate(Coordinate coordinate) {
        if (invert) {
            return Coordinate.newInstance(coordinate.getY(), coordinate.getX());
        }
        return coordinate;
    }

    private void retrieveActivities(VehicleRoutingProblem vrp) {
        activities = new XYSeries("activities", false, true);
        for (Vehicle v : vrp.getVehicles()) {
            Coordinate startCoordinate = getCoordinate(v.getStartLocation().getCoordinate());
            if (startCoordinate == null) throw new Error();
            XYDataItem item = new XYDataItem(startCoordinate.getX() * scalingFactor, startCoordinate.getY() * scalingFactor);
            markItem(item, Activity.START);
            activities.add(item);
            if (!v.getStartLocation().getId().equals(v.getEndLocation().getId())) {
                Coordinate endCoordinate = getCoordinate(v.getEndLocation().getCoordinate());
                if (endCoordinate == null) throw new Error();
                XYDataItem end_item = new XYDataItem(endCoordinate.getX() * scalingFactor, endCoordinate.getY() * scalingFactor);
                markItem(end_item, Activity.END);
                activities.add(end_item);
            }
        }
        for (Job job : vrp.getJobs().values()) {
            addJob(activities, job);
        }
        for (VehicleRoute r : vrp.getInitialVehicleRoutes()) {
            for (Job job : r.getTourActivities().getJobs()) {
                addJob(activities, job);
            }
        }
    }

    private void markItem(XYDataItem item, Activity activity) {
        activitiesByDataItem.put(item, activity);
    }


}

