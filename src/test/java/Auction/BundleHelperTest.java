package Auction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import Agent.CarrierAgent;
import com.graphhopper.jsprit.core.problem.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BundleHelperTest {

    BundleHelper bundleHelper1, bundleHelper2, bundleHelper3, bundleHelper4;
    List<CarrierAgent> carrierList1, carrierList2;
    float[] depotXs = {27,5,16,66,13,75,96,74,60,68,(float)18.75};
    float[] depotYs = {19,5,2, 76,77,9, 40,43,83,69,(float)11.25};
    TransportRequest req1, req2, req3, req4, req5, req6, req7, req8, req9, req10, req11, req12, req13;
    List<TransportRequest> requestList1, requestList2, requestList3,
            requestList4, requestList5, requestList6, requestList7,
            auctionRequests1, auctionRequests2, auctionRequests3,
            auctionRequests4,  auctionRequests5, auctionRequests6,
            auctionRequests7, auctionRequests8;
    List<List<TransportRequest>> bundleList;
    Auction auction1, auction2, auction3, auction4, auction5, auction6, auction7, auction8;
    List<Auction> auctionList1, auctionList2, auctionList3, auctionList4;

    @Before
    public void setUp() {
        carrierList1 = new ArrayList<>();
        for (int i=0; i<10; i++) {
            carrierList1.add(new CarrierAgent("carrier"+ i, "carrier"+ i,
                    0, 0, depotXs[i], depotYs[i],0,0,0,0));
        }

        carrierList2 = new ArrayList<>();
        carrierList2.add(new CarrierAgent("carrier1", "carrier1",
                0, 0, depotXs[10], depotYs[10], 0, 0, 0, 0));
        carrierList2.add(new CarrierAgent("carrier2", "carrier2",
                0, 0, depotXs[4], depotYs[4], 0, 0, 0, 0));


        req1 = new TransportRequest(0, carrierList1.get(1),0,0,0,0);
        req2 = new TransportRequest(0, carrierList1.get(1),1,1,1,1);
        req3 = new TransportRequest(0, carrierList1.get(1),2,2,2,2);

        req4 = new TransportRequest(0, carrierList1.get(1),10,11,15,11);
        req5 = new TransportRequest(1, carrierList1.get(1),5,10,18,19);
        req6 = new TransportRequest(2, carrierList1.get(1),4,7,15,20);
        req7 = new TransportRequest(3, carrierList1.get(1),3,10,23,24);
        req8 = new TransportRequest(4, carrierList1.get(1),0,10,20,18);
        req9 = new TransportRequest(5, carrierList1.get(1),5,7,17,13);
        req10 = new TransportRequest(6, carrierList1.get(1),5,18,22,45);
        req11 = new TransportRequest(7, carrierList1.get(1),10,40,28,50);
        req12 = new TransportRequest(8, carrierList1.get(1),11,41,29,61);
        req13 = new TransportRequest(9, carrierList1.get(1),45,50,12,76);

        requestList1 = new ArrayList<>();
        requestList2 = new ArrayList<>();
        requestList3 = new ArrayList<>();
        requestList4 = new ArrayList<>();
        requestList5 = new ArrayList<>();
        requestList6 = new ArrayList<>();
        requestList7 = new ArrayList<>();
        auctionRequests1 = new ArrayList<>();
        auctionRequests2 = new ArrayList<>();
        auctionRequests3 = new ArrayList<>();
        auctionRequests4 = new ArrayList<>();
        auctionRequests5 = new ArrayList<>();
        auctionRequests6 = new ArrayList<>();
        auctionRequests7 = new ArrayList<>();
        auctionRequests8 = new ArrayList<>();
        bundleList = new ArrayList<>();

        requestList2.add(req1);
        requestList3.add(req1);requestList3.add(req2);requestList3.add(req3);
        requestList4.add(req2);requestList4.add(req3);
        requestList5.add(req1);requestList5.add(req2);requestList5.add(req3);

        bundleList.add(requestList2);
        bundleList.add(requestList3);
        bundleList.add(requestList4);
        bundleList.add(requestList5);

        requestList6.add(req4);requestList6.add(req5);requestList6.add(req6);
        requestList6.add(req7);requestList6.add(req8);requestList6.add(req9);

        requestList7.add(req10);requestList7.add(req11);requestList7.add(req12);requestList7.add(req13);

        // TestDecisionMaking 1
        auctionRequests1.add(req1);auctionRequests1.add(req2);auctionRequests1.add(req3);
        auctionRequests2.add(req2);auctionRequests2.add(req3);auctionRequests2.add(req4);

        auction1 = new Auction();
        auction1.setAuctionStrategy(new VickreyAuction());
        auction1.setTransportRequests(auctionRequests1);
        auction1.addBid(new Bid(auction1,carrierList1.get(0),120));

        auction2 = new Auction();
        auction2.setAuctionStrategy(new VickreyAuction());
        auction2.setTransportRequests(auctionRequests2);
        auction2.addBid(new Bid(auction2,carrierList1.get(1),240));

        auctionList1 = new ArrayList<>();
        auctionList1.add(auction1); auctionList1.add(auction2);

        // TestDecisionMaking 2
        auctionRequests3.add(req1);auctionRequests3.add(req2);
        auctionRequests4.add(req2);auctionRequests4.add(req3);
        auctionRequests5.add(req3);auctionRequests5.add(req4);

        auction3 = new Auction();
        auction3.setAuctionStrategy(new VickreyAuction());
        auction3.setTransportRequests(auctionRequests3);
        auction3.addBid(new Bid(auction3,carrierList1.get(0),20));

        auction4 = new Auction();
        auction4.setAuctionStrategy(new VickreyAuction());
        auction4.setTransportRequests(auctionRequests4);
        auction4.addBid(new Bid(auction4,carrierList1.get(1),80));

        auction5 = new Auction();
        auction5.setAuctionStrategy(new VickreyAuction());
        auction5.setTransportRequests(auctionRequests5);
        auction5.addBid(new Bid(auction5,carrierList1.get(2),40));

        auctionList2 = new ArrayList<>();
        auctionList2.add(auction3); auctionList2.add(auction4); auctionList2.add(auction5);

        // TestDecisionMaking 3
        auctionRequests6.add(req1);auctionRequests6.add(req2);
        auctionRequests7.add(req2);auctionRequests7.add(req3);

        auction6 = new Auction();
        auction6.setAuctionStrategy(new VickreyAuction());
        auction6.setTransportRequests(auctionRequests6);
        auction6.addBid(new Bid(auction6,carrierList1.get(0),20));

        auction7 = new Auction();
        auction7.setAuctionStrategy(new VickreyAuction());
        auction7.setTransportRequests(auctionRequests7);
        auction7.addBid(new Bid(auction7,carrierList1.get(0),80));

        auctionList3 = new ArrayList<>();
        auctionList3.add(auction6); auctionList3.add(auction7);

        // TestDecisionMaking 4
        auctionRequests8.add(req1);auctionRequests8.add(req2);

        auction8 = new Auction();
        auction8.setAuctionStrategy(new VickreyAuction());
        auction8.setTransportRequests(auctionRequests8);

        auctionList4 = new ArrayList<>();
        auctionList4.add(auction8);

        bundleHelper1 = new BundleHelper(carrierList1.subList(0,5), requestList1);
        bundleHelper2 = new BundleHelper(carrierList1, requestList1);
        bundleHelper3 = new BundleHelper(carrierList2, requestList6);
        bundleHelper4 = new BundleHelper(carrierList2, requestList7);
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
        assertEquals(expected1, bundleHelper1.checkAndRemoveSubset(bundleList), "Subsets are not removed");
    }

    @Test
    public void testFormingBundle() {
        List<List<TransportRequest>> expected2 = new ArrayList<>();
        expected2.add(requestList6.subList(0,5));
        expected2.add(requestList6.subList(5,6));
        assertEquals(expected2, bundleHelper3.generateBundles(), "Size of bundles exceeded (>5)");

        List<List<TransportRequest>> expected3 = new ArrayList<>();
        expected3.add(requestList7.subList(0,2));
        expected3.add(requestList7.subList(1,3));
        expected3.add(requestList7.subList(3,4));
        assertEquals(expected3, bundleHelper4.generateBundles(), "Bundles don't contain same requests");
    }

    @Test
    public void testDecisionMaking() {
        List<Auction> expected1 = new ArrayList<>();
        expected1.add(auction2);
        assertEquals(expected1,bundleHelper1.decisionMaking(auctionList1), "Winner with highest pay price didn't win");
        List<TransportRequest> expected2 = new ArrayList<>();
        expected2.add(req1);
        assertEquals(expected2,bundleHelper1.getUnsoldList(), "Unsold requests weren't put in unsoldList");
        assertEquals(240.0, bundleHelper1.decisionMaking(auctionList1).get(0).getWinningBid().getPayPrice(),
                "Payment is calculated correctly");

        List<Auction> expected3 = new ArrayList<>();
        expected3.add(auction4);
        assertEquals(expected3,bundleHelper2.decisionMaking(auctionList2), "Winner with highest pay price didn't win");
        List<TransportRequest> expected4 = new ArrayList<>();
        expected4.add(req1);
        expected4.add(req4);
        assertEquals(expected4,bundleHelper2.getUnsoldList(), "Unsold requests weren't put in unsoldList");
        assertEquals(80.0, bundleHelper2.decisionMaking(auctionList2).get(0).getWinningBid().getPayPrice(),
                "Payment is calculated correctly");

        List<TransportRequest> expected5 = new ArrayList<>();
        expected5.add(req1);expected5.add(req2);expected5.add(req3);
        assertTrue(bundleHelper3.decisionMaking(auctionList3).get(0).getTransportRequests().containsAll(expected5),
                "Multi-auctions-winner doesn't have same request deducted from their winning list");
        assertTrue(expected5.containsAll(bundleHelper3.decisionMaking(auctionList3).get(0).getTransportRequests()),
                "Multi-auctions-winner doesn't have same request deducted from their winning list");
        assertTrue(bundleHelper3.getUnsoldList().isEmpty(), "unsoldList isn't empty");
        assertEquals(90.0, bundleHelper3.decisionMaking(auctionList3).get(0).getWinningBid().getPayPrice(),
                "Multi-auctions-winner doesn't have payment recalculated based on same requests");

        List<TransportRequest> expected6 = new ArrayList<>();
        expected6.add(req1);expected6.add(req2);
        List<TransportRequest> expected7 = new ArrayList<>();
        assertEquals(expected7,bundleHelper4.decisionMaking(auctionList4),
                "Unsold bundles contain in solution");
        assertEquals(expected6,bundleHelper4.getUnsoldList(),
                "Unsold bundles don't contain in unsoldList");
    }
}
