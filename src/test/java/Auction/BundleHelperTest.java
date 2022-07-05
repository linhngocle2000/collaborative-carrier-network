package Auction;

import static org.junit.jupiter.api.Assertions.assertEquals;

import Agent.CarrierAgent;
import Auction.BundleHelper;
import Auction.TransportRequest;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.util.EuclideanDistanceCalculator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BundleHelperTest {

    BundleHelper bundleHelper1, bundleHelper2;
    List<CarrierAgent> carriers;
    int[] depotXs = {27,5,16,66,13,75,96,74,60,68};
    int[] depotYs = {19,5,2, 76,77,9, 40,43,83,69};
    List<TransportRequest> requestList1, requestList2, requestList3;
    List<List<TransportRequest>> bundleList;

    @Before
    public void setUp() {
        carriers = new ArrayList<>();
        for (int i=0; i<10; i++) {
            carriers.add(new CarrierAgent("carrier"+Integer.toString(i), "carrier"+Integer.toString(i),
                    0, 0, depotXs[i], depotYs[i],0,0,0,0));
        }
        requestList1 = new ArrayList<>();
        requestList2 = new ArrayList<>();
        requestList3 = new ArrayList<>();
        bundleList = new ArrayList<>();

        requestList2.add(new TransportRequest(0,carriers.get(1),0,0,0,0));
        bundleList.add(requestList2);
        requestList3.add(new TransportRequest(0,carriers.get(1),0,0,0,0));
        requestList3.add(new TransportRequest(0,carriers.get(1),1,1,1,1));
        requestList3.add(new TransportRequest(0,carriers.get(1),2,2,2,2));
        bundleList.add(requestList3);

        bundleHelper1 = new BundleHelper(carriers.subList(0,5), requestList1);
        bundleHelper2 = new BundleHelper(carriers, requestList1);
    }

    @Test
    public void testGenerateSpecialDepots() {
        List<Location> expected1 = new ArrayList<>();
        for (int i=0; i<5; i++) {
            expected1.add(Location.newInstance(depotXs[i], depotYs[i]));
        }
        assertEquals(expected1, bundleHelper1.generateSpecialDepots(), "Special depots are generated even when less than 6 existed");

        double a = (60.0+68.0+66.0)/3.0;
        double b = (a+63.0+67.0)/3.0;
        double c = (76.0+83.0+69.0)/3.0;
        List<Location> expected2 = new ArrayList<>();
        expected2.add(Location.newInstance(18.75, 11.25));
        expected2.add(Location.newInstance((b+74.0)/2.0, (c+43.0)/2.0));
        expected2.add(Location.newInstance(13.0, 77.0));
        expected2.add(Location.newInstance((75.0+96.0+74.0)/3.0, (9.0+40.0+43.0)/3.0));
        expected2.add(Location.newInstance((b+74.0+75.0+96.0)/4.0, (c+43.0+9.0+40.0)/4.0));
        assertEquals(expected2, bundleHelper2.generateSpecialDepots(), "Special depots are generated incorrectly");
    }

    @Test
    public void testCheckAndRemoveSubset() {
        List<List<TransportRequest>> expected1 = new ArrayList<>();
        expected1.add(requestList3);
        assertEquals(expected1, bundleHelper1.checkAndRemoveSubset(bundleList));

    }
}
