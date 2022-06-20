package Auction.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.util.EuclideanDistanceCalculator;

import Agent.CarrierAgent;
import Auction.TransportRequest;
import UIResource.HTTPResource.HTTPRequests;
import Utils.TourPlanning;

public class BundleHelper {

   /**
    * Bundles are form into a list of bundle.
    * This method help to remove all bundle that is subset of another.
    */
   public List<List<TransportRequest>> checkAndRemoveSubset(List<List<TransportRequest>> bundleList){
      List<List<TransportRequest>> returnBundleList = new ArrayList<>();
      returnBundleList.addAll(bundleList);
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

   /**
    * Winning bundles of a carrier will be form into a map of request.
    * This method help to remove duplicate winning request if any.
    */
   public HashMap<TransportRequest, Double> formWinningRequest(List<TransportRequest> bundle, double price) {
      HashMap<TransportRequest, Double> winningRequest = new HashMap<TransportRequest, Double>();
      double payingPrice = price / bundle.size();
      for (TransportRequest request : bundle) {
         if (winningRequest.containsKey(request)) {
            if (winningRequest.get(request) < payingPrice) {
               winningRequest.replace(request, payingPrice);
            }
         } else {
            winningRequest.put(request, payingPrice);
         }
      }
      return winningRequest;
   }

////////
//       Generating special depot location
////////

   private double radius = 10;
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
            radius *= 2;
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
         if (distance <= radius) {
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

   private double radius = 20;
   private final int minimumSize = 5;

   public List<List<TransportRequest>> generateBundles() {
      List<List<TransportRequest>> bundleList = new ArrayList<>();
      List<Location> specialDepotList = generateSpecialDepots();
      for (Location specialDepot : specialDepotList) {
         List<TransportRequest> bundle = formingBundle(specialDepot);
         while (bundle.size() < minimumSize) {
            radius *= 2;
            bundle = formingBundle(specialDepot);
         }
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
         if (distance <= radius) {
            gatherRequestList.add(request);
         }
      }
      return gatherRequestList;
   }


////////
//       Generating a list of bundle using solution 2: 
////////
}
