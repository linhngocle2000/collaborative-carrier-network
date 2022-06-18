package Auction.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.util.EuclideanDistanceCalculator;

import Agent.CarrierAgent;
import Auction.TransportRequest;
import UIResource.HTTPResource.HTTPRequests;

public class BundleHelper {

   /**
    * Bundle is form into a list of bundle.
    * This method help to remove all bundle that is subset of another.
    */
   public List<List<TransportRequest>> checkAndRemoveSubset(List<List<TransportRequest>> set){
      List<List<TransportRequest>> returnSet = new ArrayList<>();
      returnSet.addAll(set);
      for (List<TransportRequest> l1 : set) {
         if (set.size() == 1) {
            break;
         }
         set = set.subList(1, set.size());
         for (List<TransportRequest> l2 : set) {
            if (l1.containsAll(l2)) {
               returnSet.remove(l2);
            } else if (l2.containsAll(l1)) {
               returnSet.remove(l1);
            }
         }
      }
      return returnSet;
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

   /**
    * Generating special depot location
    */
   private final double radius = 10;
   private List<Location> depotList;

   public List<Location> generateDepot() {
      List<Location> specialDepot = new ArrayList<>();
      getDepotList();
      for (Location focusedDepot : this.depotList) {
         specialDepot.add(getMeanLocation(gatherDepot(focusedDepot)));
      }
      return specialDepot;
   }

   public List<Location> generateDepot(List<Location> depotList) {
      List<Location> specialDepot = new ArrayList<>();
      for (Location focusedDepot : depotList) {
         specialDepot.add(getMeanLocation(gatherDepot(focusedDepot)));
      }
      return specialDepot;
   }

   private void getDepotList() {
      this.depotList = new ArrayList<>();
      List<CarrierAgent> carrierList = HTTPRequests.getCarrierAgents();
      for (CarrierAgent carrier : carrierList) {
         this.depotList.add(Location.newInstance(carrier.getDepotX(), carrier.getDepotY()));
      }
   }

   private List<Location> gatherDepot (Location focusedDepot) {
      double distance;
      List<Location> gatherList = new ArrayList<>();
      for (Location depot : this.depotList) {
         distance = EuclideanDistanceCalculator.calculateDistance(focusedDepot.getCoordinate(), depot.getCoordinate());
         if (distance <= radius) {
            gatherList.add(depot);
         }
      }
      return gatherList;
   }

   private Location getMeanLocation(List<Location> gatherList) {
      double depotX = 0, depotY = 0;
      for (Location depot : gatherList) {
         depotX += depot.getCoordinate().getX();
         depotY += depot.getCoordinate().getY();
      }
      return Location.newInstance(depotX / gatherList.size(), depotY / gatherList.size());
   }
}
