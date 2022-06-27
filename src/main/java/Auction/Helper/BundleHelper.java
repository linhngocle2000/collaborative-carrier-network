package Auction.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.util.EuclideanDistanceCalculator;

import Agent.CarrierAgent;
import Auction.Auction;
import Auction.Bid;
import Auction.TransportRequest;
import UIResource.HTTPResource.HTTPRequests;
import Utils.TourPlanning;

public class BundleHelper {

   /**
    * Bundles are form into a list of bundle.
    * This method help to remove all bundle that is subset of another.
    */
   public List<List<TransportRequest>> checkAndRemoveSubset(List<List<TransportRequest>> bundleList){
      List<List<TransportRequest>> returnBundleList = new ArrayList<>(bundleList);
      for (List<TransportRequest> bundle1 : bundleList) {
         if (bundleList.size() == 1) {
            break;
         }
         bundleList = bundleList.subList(1, bundleList.size());
         for (List<TransportRequest> bundle2 : bundleList) {
            if (bundle1.containsAll(bundle2)) {
               returnBundleList.remove(bundle2);
            } else if (bundle2.containsAll(bundle1)) {
               returnBundleList.remove(bundle1);
            }
         }
      }
      return returnBundleList;
   }


////////
//       Generating special depot location
////////

   private double radiusDepot = 10;
   private final int maximumSize = 5;

   /**
    * This method is used to generate the list of special depot loc
    */
   public List<Location> generateSpecialDepots() {
      List<Location> specialDepotList = getDepotList();
      List<Location> tempDepotList;
      while (specialDepotList.size() > maximumSize) {
         tempDepotList = generateSpecialDepots(specialDepotList);
         if (specialDepotList.equals(tempDepotList)) {
            /**
             * At this point if the same list is generated again,
             * then we can double the radius or just break the loop and accept the list.
             */
            radiusDepot *= 2;
            // break;
         } else {
            specialDepotList = tempDepotList;
         }
      }
      return specialDepotList;
   }

   /**
    * This method helps to generate higher tier of special depot locs
    */
   private List<Location> generateSpecialDepots(List<Location> depotList) {
      List<Location> specialDepotList = new ArrayList<>();
      Location specialDepot;
      for (Location focusedDepot : depotList) {
         specialDepot = getSpecialDepot(gatherDepot(focusedDepot, depotList));
         if (specialDepotList.contains(specialDepot)) {
            continue;
         }
         specialDepotList.add(specialDepot);
      }
      return specialDepotList;
   }

   /**
    * This method helps to gather all depot loc within a circle around the focused depot
    */
   private List<Location> gatherDepot (Location focusedDepot, List<Location> depotList) {
      double distance;
      List<Location> gatherList = new ArrayList<>();
      for (Location depot : depotList) {
         distance = EuclideanDistanceCalculator.calculateDistance(focusedDepot.getCoordinate(), depot.getCoordinate());
         if (distance <= radiusDepot) {
            gatherList.add(depot);
         }
      }
      return gatherList;
   }

   /**
    * This method calculate a special depot loc from a gather list of depot loc
    */
   private Location getSpecialDepot(List<Location> gatherList) {
      if (gatherList.size() == 1) {
         return gatherList.get(0);
      }
      double depotX = 0, depotY = 0;
      for (Location depot : gatherList) {
         depotX += depot.getCoordinate().getX();
         depotY += depot.getCoordinate().getY();
      }
      return Location.newInstance(depotX / gatherList.size(), depotY / gatherList.size());
   }

   /**
    * This method get all depot of registered carrier
    */
   private List<Location> getDepotList() {
      List<Location> depotList = new ArrayList<>();
      List<CarrierAgent> carrierList = HTTPRequests.getCarrierAgents();
      for (CarrierAgent carrier : carrierList) {
         depotList.add(Location.newInstance(carrier.getDepotX(), carrier.getDepotY()));
      }
      return depotList;
   }


////////
//       Generating a list of bundle using solution 1: 
//       With a hugh dataset, this might take time to finish.
////////

   private double radiusRequest = 20;
   // private final int minimumSize = 5;

   public List<List<TransportRequest>> generateBundles() {
      List<List<TransportRequest>> bundleList = new ArrayList<>();
      List<Location> specialDepotList = generateSpecialDepots();
      for (Location specialDepot : specialDepotList) {
         List<TransportRequest> bundle = formingBundle(specialDepot);
         /**
          * The minimum number of request within a bundle
          */
         // while (bundle.size() < minimumSize) {
         //    radius *= 2;
         //    bundle = formingBundle(specialDepot);
         // }
         bundleList.add(bundle);
      }
      return bundleList;
   }

   /**
    * This method picks only the requests which make profit to special depot location.
    */
   private List<TransportRequest> formingBundle (Location specialDepot) {
      TourPlanning tour = new TourPlanning();
      List<TransportRequest> bundle = new ArrayList<>();
      List<TransportRequest> gatherRequestList = gatherRequestList(specialDepot);
      tour.setDepot(specialDepot);
      tour.setRequests(gatherRequestList);
      for (TransportRequest request : gatherRequestList) {
         if (tour.getProfit(request) > 0) {
            bundle.add(request);
         }
      }
      return bundle;
   }

   /**
    * This method gathers all the request within a radius around a special depot.
    */
   private List<TransportRequest> gatherRequestList(Location specialDepot) {
      double distance;
      // Location requestLoc;
      // double requestX, requestY;
      List<TransportRequest> gatherRequestList = new ArrayList<>();
      List<TransportRequest> requestsOnAuction = getAllRequestOnAuction();
      for (TransportRequest request : requestsOnAuction) {
         /**
          * Consider a request is a location which is mean value of pickup location and deliver location.
          */
         // requestX = (request.getPickupX() + request.getDeliveryX()) / 2;
         // requestY = (request.getPickupY() + request.getDeliveryY()) / 2;
         // requestLoc = Location.newInstance(requestX , requestY);
         // distance = EuclideanDistanceCalculator.calculateDistance(specialDepot.getCoordinate(), requestLoc.getCoordinate());

         /**
          * Consider only the pickup location of the request.
          */
         distance = EuclideanDistanceCalculator.calculateDistance(specialDepot.getCoordinate(), request.getPickup().getCoordinate());
         if (distance <= radiusRequest) {
            gatherRequestList.add(request);
         }
      }
      return gatherRequestList;
   }


////////
//       Decision Making part
////////

   public List<Auction> decisionMaking(List<Auction> bundleAuctionList) {
      List<Auction> returnList = new ArrayList<>(bundleAuctionList);
      HashMap<List<TransportRequest>, Double> winningList = new HashMap<>();
      List<List<TransportRequest>> bestBundleList;
      for (Auction auction : bundleAuctionList) {
         winningList.put(auction.getTransportRequests(), averagePayingPrice(auction.getWinningBid(), auction.getTransportRequests().size()));
      }
      bestBundleList = formingBestCombination(winningList);
      for (Auction auction : bundleAuctionList) {
         if (!bestBundleList.containsAll(auction.getTransportRequests())) {
            returnList.remove(auction);
         }
      }
      return returnList;
   }
   
   private double averagePayingPrice (Bid winningBid, int bundleSize) {
      return winningBid.getPayPrice() / bundleSize;
   }

   private List<List<TransportRequest>> formingBestCombination(HashMap<List<TransportRequest>, Double> winningList) {
      List<TransportRequest> temp;
      List<List<TransportRequest>> tempList = new ArrayList<>();
      HashMap<List<List<TransportRequest>>, Double> uncommonBundleList = new HashMap<>();
      Set<List<TransportRequest>> bundleList = new HashSet<>(winningList.keySet());
      double totalAverageBid;
      /**
       * Forming all the combination of uncommons bundles
       */
      for (List<TransportRequest> l1 : winningList.keySet()) {
         tempList.add(l1);
         totalAverageBid = winningList.get(l1);
         bundleList.remove(l1);
         for (List<TransportRequest> l2 : bundleList) {
            temp = new ArrayList<>(l1);
            if (!temp.retainAll(l2)) {
               tempList.add(l2);
               totalAverageBid += winningList.get(l2);
            }
         }
         uncommonBundleList.put(tempList, totalAverageBid);
         tempList.clear();
      }
      /**
       * Get the best combination
       */
      double highestPrice = (Collections.max(uncommonBundleList.values()));
      for (Entry<List<List<TransportRequest>>, Double> entry : uncommonBundleList.entrySet()) {
         if (entry.getValue() == highestPrice) {
            tempList = entry.getKey();
         }
      }
      return tempList;
   }

   // /**
   //  * Winning bundles of a carrier will be form into a map of request.
   //  * This method help to remove duplicate winning request if any.
   //  */
   //  public HashMap<TransportRequest, Double> formWinningRequest(List<TransportRequest> bundle, double price) {
   //    HashMap<TransportRequest, Double> winningRequest = new HashMap<>();
   //    double payingPrice = price / bundle.size();
   //    for (TransportRequest request : bundle) {
   //       if (winningRequest.containsKey(request)) {
   //          if (winningRequest.get(request) < payingPrice) {
   //             winningRequest.replace(request, payingPrice);
   //          }
   //       } else {
   //          winningRequest.put(request, payingPrice);
   //       }
   //    }
   //    return winningRequest;
   // }
}
