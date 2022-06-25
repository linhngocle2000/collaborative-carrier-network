import static org.junit.jupiter.api.Assertions.assertEquals;

import Agent.CarrierAgent;
import Auction.TransportRequest;
import Utils.TourPlanning;
import com.graphhopper.jsprit.core.util.Coordinate;
import com.graphhopper.jsprit.core.util.EuclideanDistanceCalculator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CostCalculatorTest {

    CarrierAgent carrier;
    List<TransportRequest> transportRequestList;
    TourPlanning tour;
    TransportRequest tr1;
    TransportRequest tr2;
    TransportRequest tr3;

    @Before
    public void setUp() {
        carrier = new CarrierAgent("test", "test", 4, 5, 500,500,100,100);
        transportRequestList = new ArrayList<>();
        tr1 = new TransportRequest(0,carrier,3,7,5,2);
        tr2 = new TransportRequest(1,carrier,6,1,2,7);
        tr3 = new TransportRequest(2,carrier,8,2,6,10);
        transportRequestList.add(tr1);
        transportRequestList.add(tr2);
        transportRequestList.add(tr3);
        tour = new TourPlanning(carrier,transportRequestList);
    }

    @Test
    public void testGetProfit() {
        double requestDist1 = EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(3, 7),Coordinate.newInstance(5, 2));
        double realDist1 = EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(3, 7),Coordinate.newInstance(8, 2)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(8, 2),Coordinate.newInstance(6, 1)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(6, 1),Coordinate.newInstance(5, 2));
        double expected1 = carrier.getFixedCost() + requestDist1*carrier.getCostPerDistance() - (carrier.getLoadingCost() + realDist1* carrier.getInternalCost());
        expected1 = Math.round(expected1 * 100.0) / 100.0;
        double result1 = Math.round(tour.getProfit(tr1) * 100.0) / 100.0;
        assertEquals(expected1, result1, "Profit 1 is calculated incorrectly");

        double requestDist2 = EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(6, 1),Coordinate.newInstance(2, 7));
        double realDist2 = EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(6, 1),Coordinate.newInstance(5, 2)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(5, 2),Coordinate.newInstance(6, 10)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(6, 10),Coordinate.newInstance(2, 7));
        double expected2 = carrier.getFixedCost() + requestDist2*carrier.getCostPerDistance() - (carrier.getLoadingCost() + realDist2* carrier.getInternalCost());
        expected2 = Math.round(expected2 * 100.0) / 100.0;
        double result2 = Math.round(tour.getProfit(tr2) * 100.0) / 100.0;
        assertEquals(expected2, result2, "Profit 2 is calculated incorrectly");

        double requestDist3 = EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(8, 2),Coordinate.newInstance(6, 10));
        double realDist3 = EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(8, 2),Coordinate.newInstance(6, 1)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(6, 1),Coordinate.newInstance(5, 2)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(5, 2),Coordinate.newInstance(6, 10));
        double expected3 = carrier.getFixedCost() + requestDist3*carrier.getCostPerDistance() - (carrier.getLoadingCost() + realDist3* carrier.getInternalCost());
        expected3 = Math.round(expected3 * 100.0) / 100.0;
        double result3 = Math.round(tour.getProfit(tr3) * 100.0) / 100.0;
        assertEquals(expected3, result3, "Profit 3 is calculated incorrectly");

        double resultProfit = Math.round(tour.getRevenueSum() * 100.0) / 100.0;
        double expectedProfit = Math.round((expected1+expected2+expected3) * 100.0) / 100.0;
        assertEquals(expectedProfit, resultProfit, "Sum of revenue is calculated incorrectly");
    }

    @Test
    public void testGetRevenueTotal() {
        double tourLen = EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(4, 5),Coordinate.newInstance(3, 7)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(3, 7),Coordinate.newInstance(8, 2)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(8, 2),Coordinate.newInstance(6, 1)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(6, 1),Coordinate.newInstance(5, 2)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(5, 2),Coordinate.newInstance(6, 10)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(6, 10),Coordinate.newInstance(2, 7)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(2, 7),Coordinate.newInstance(4, 5));
        double expectedOut = 3*carrier.getLoadingCost() + tourLen*carrier.getInternalCost();
        expectedOut = Math.round(expectedOut * 100.0) / 100.0;
        double resultOut = Math.round(tour.getTotalOut() * 100.0) / 100.0;
        assertEquals(expectedOut, resultOut, "Cost is calculated incorrectly");
        double totalReqLen = EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(3, 7),Coordinate.newInstance(5, 2)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(6, 1),Coordinate.newInstance(2, 7)) +
                EuclideanDistanceCalculator.calculateDistance(Coordinate.newInstance(8, 2),Coordinate.newInstance(6, 10));
        double expectedIn = 3*carrier.getFixedCost() + totalReqLen*carrier.getCostPerDistance();
        expectedIn = Math.round(expectedIn * 100.0) / 100.0;
        double resultIn = Math.round(tour.getTotalIn() * 100.0) / 100.0;
        assertEquals(expectedIn, resultIn, "Earnings is calculated incorrectly");
        double resultRevenueTotal = Math.round(tour.getRevenueTotal() * 100.0) / 100.0;
        assertEquals(expectedIn - expectedOut, resultRevenueTotal, "Total revenue is calculated incorrectly");
    }
}
