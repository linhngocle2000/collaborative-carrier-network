package Auction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Map.Entry;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.util.EuclideanDistanceCalculator;

import Agent.CarrierAgent;
import Utils.TourPlanning;

public class BundleHelper {

   private class Winning {
      private String bidderUsername;
      List<TransportRequest> bundle;
      double averagePayPrice;

      Winning() {}
      
      Winning(String username, List<TransportRequest> bundle, double averagePayPrice) {
         this.bidderUsername = username;
         this.bundle = new ArrayList<>(bundle);
         this.averagePayPrice = averagePayPrice;
      }

      void setAll(Winning w) {
         this.bidderUsername = w.bidderUsername;
         this.bundle = new ArrayList<>(w.bundle);
         this.averagePayPrice = w.averagePayPrice;
      }
   }

   private List<CarrierAgent> carriers;
   private List<TransportRequest> requests;
   private List<TransportRequest> unsoldList;

   public BundleHelper(List<CarrierAgent> carriers, List<TransportRequest> requests) {
      this.carriers = carriers;
      this.requests = requests;
   }

   public List<TransportRequest> getUnsoldList() {
      return this.unsoldList;
   }


////////
//       Generating special depot location
////////

   private double radiusDepot = 10;
   private final int maxDepotSize = 5;

   /**
    * This method is used to generate the list of special depot loc
    */
   private List<Location> generateSpecialDepots() {
      List<Location> specialDepotList = getDepotList();
      List<Location> tempDepotList;
      while (specialDepotList.size() > maxDepotSize) {
         tempDepotList = generateSpecialDepots(specialDepotList);
         if (specialDepotList.equals(tempDepotList)) {
            /**
             * At this point if the same list is generated again,
             * then we can double the radius or just break the loop and accept the list.
             */
            if (radiusDepot == 80) {
               return specialDepotList;
            }
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
      for (CarrierAgent carrier : carriers) {
         depotList.add(Location.newInstance(carrier.getDepotX(), carrier.getDepotY()));
      }
      return depotList;
   }


////////
//       Generating a list of bundle using solution 1: 
//       With a hugh dataset, this might take time to finish.
////////

   private double radiusRequest = 20;
   private final int minBundleSize = 3;
   private final int maxBundleSize = 5;
   private final double minProfit = 0;

   public List<List<TransportRequest>> generateBundles() {
      List<List<TransportRequest>> bundleList = new ArrayList<>();
      List<Location> specialDepotList = generateSpecialDepots();
      List<TransportRequest> bundle;
      // Form bundles depending on the special depot locations
      for (Location specialDepot : specialDepotList) {
         bundle = formingBundle(specialDepot);
         bundleList.add(bundle);
      }
      bundleList = checkAndRemoveSubset(bundleList);

      // Add transport requests that haven't been formed into a bundle
      for (TransportRequest request : requests) {
         if (bundleList.stream().noneMatch(x -> x.contains(request))) {
            List<TransportRequest> list = new ArrayList<>();
            list.add(request);
            bundleList.add(list);
         }
      }

      return bundleList;
   }

   /**
    * This method picks only the requests which make profit to special depot location.
    */
   private List<TransportRequest> formingBundle (Location specialDepot) {
      TourPlanning tour = new TourPlanning();
      List<TransportRequest> bundle = new ArrayList<>();
      List<TransportRequest> gatherList;
      tour.setDepot(specialDepot);
      while (bundle.size() < minBundleSize) {
         if (radiusRequest == 50) {
            break;
         }
         bundle.clear();
         radiusRequest += 10;
         gatherList = gatherRequestList(specialDepot);
         tour.setRequests(gatherList);
         for (TransportRequest request : gatherList) {
            if (tour.getProfit(request) > minProfit) {
               bundle.add(request);
               if (bundle.size() == maxBundleSize) {
                  return bundle;
               }
            }
         }
      }
      return bundle;
   }

   /**
    * This method gathers all the request within a radius around a special depot.
    */
   private List<TransportRequest> gatherRequestList(Location specialDepot) {
      double distancePickup, distanceDeliver;
      // Location requestLoc;
      // double requestX, requestY;
      List<TransportRequest> gatherList = new ArrayList<>();
      for (TransportRequest request : requests) {
         /**
          * Consider a request is a location which is mean value of pickup location and deliver location.
          */
         // requestX = (request.getPickupX() + request.getDeliveryX()) / 2;
         // requestY = (request.getPickupY() + request.getDeliveryY()) / 2;
         // requestLoc = Location.newInstance(requestX , requestY);
         // distance = EuclideanDistanceCalculator.calculateDistance(specialDepot.getCoordinate(), requestLoc.getCoordinate());

         /**
          * Consider both pickup and deliver locations of the request.
          */
         distancePickup = EuclideanDistanceCalculator.calculateDistance(specialDepot.getCoordinate(), request.getPickup().getCoordinate());
         distanceDeliver = EuclideanDistanceCalculator.calculateDistance(specialDepot.getCoordinate(), request.getDelivery().getCoordinate());
         if (distancePickup <= radiusRequest && distanceDeliver <= radiusRequest) {
            gatherList.add(request);
         }
      }
      return gatherList;
   }

   /**
    * Bundles are form into a list of bundle.
    * This method help to remove all bundle that is subset of another.
    */
   private List<List<TransportRequest>> checkAndRemoveSubset(List<List<TransportRequest>> bundleList){
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
//       Decision Making part
////////

   public List<Auction> decisionMaking(List<Auction> auctionList) {
      List<Auction> bundleAuctions = new ArrayList<>();
      for (Auction auction : auctionList) {
         if (auction.getTransportRequests().size() != 1) {
            bundleAuctions.add(auction);
         }
      }
      List<Winning> winningList = new ArrayList<>();
      String username;
      double averagePayingPrice;
      List<List<TransportRequest>> bestBundleList = new ArrayList<>();
      for (Auction auction : bundleAuctions) {
         averagePayingPrice = auction.getWinningBid().getPayPrice() / auction.getTransportRequests().size();
         username = auction.getWinningBid().getBidder().getUsername();
         winningList.add(new Winning(username, auction.getTransportRequests(), averagePayingPrice));
      }
      winningList = formingBestCombination(winningList);
      for (Winning w : winningList) {
         bestBundleList.add(w.bundle);
      }
      unsoldList = new ArrayList<>();
      for (Auction auction : bundleAuctions) {
         if (!bestBundleList.contains(auction.getTransportRequests())) {
            for (TransportRequest request : auction.getTransportRequests()) {
               unsoldList.add(request);
            }
            auctionList.remove(auction);
         }
      }
      List<TransportRequest> temp = new ArrayList<>(unsoldList);
      for (TransportRequest request : temp) {
         for (List<TransportRequest> bundle : bestBundleList) {
            if (bundle.contains(request)) {
               unsoldList.remove(request);
               break;
            }
         }
      }
      return auctionList;
   }

   private List<Winning> formingBestCombination(List<Winning> winningList) {
      Winning temp = new Winning();
      List<Winning> tempList;
      List<Winning> sameBidderList;
      List<List<Winning>> tempNestedList = new ArrayList<>();
      HashMap<List<Winning>, Double> uncommonBundleMap = new HashMap<>();
      double totalAverageBid;
      /**
       * Forming all the combination of uncommon bundles
       */
      for (Winning w1 : winningList) {
         tempList = new ArrayList<>();
         sameBidderList = new ArrayList<>();
         tempList.add(w1);
         totalAverageBid = w1.averagePayPrice;
         for (Winning w2 : winningList) {
            if (Objects.equals(w1, w2)) {
               continue;
            }
            if (Objects.equals(w1.bidderUsername, w2.bidderUsername)) {
               sameBidderList.add(w2);
               continue;
            }
            temp.setAll(w1);
            temp.bundle.retainAll(w2.bundle);
            if (temp.bundle.isEmpty()) {
               tempList.add(w2);
               totalAverageBid += w2.averagePayPrice;
            }
         }
         if (!sameBidderList.isEmpty()) {
            List<Winning> compareList = new ArrayList<>(tempList.subList(1, tempList.size()));
            for (Winning w21 : sameBidderList) {
               int count = 0;
               for (Winning w22 : compareList) {
                  temp.setAll(w21);
                  temp.bundle.retainAll(w22.bundle);
                  if (!temp.bundle.isEmpty()) {
                     count ++;
                     temp.setAll(w22);
                  }
               }
               switch (count) {
                  case 0:
                     tempList.add(w21);
                     totalAverageBid += w21.averagePayPrice;
                     break;
                  case 1:
                     if (sameBidderList.size() == 1) {
                        if (w21.averagePayPrice >= temp.averagePayPrice) {
                           tempList.remove(temp);
                           totalAverageBid -= temp.averagePayPrice;
                           tempList.add(w21);
                           totalAverageBid += w21.averagePayPrice;
                        }
                     } else {
                        tempList.remove(temp);
                        totalAverageBid -= temp.averagePayPrice;
                        tempList.add(w21);
                        totalAverageBid += w21.averagePayPrice;
                     }
                     break;
                  default:
                     break;
               }
            }
            if (sameBidderList.size() > tempList.size() - 1) {
               compareList = new ArrayList<>(tempList.subList(1, tempList.size()));
               tempList.removeAll(compareList);
               for (Winning w : compareList) {
                  totalAverageBid -= w.averagePayPrice;
               }
               tempList.addAll(sameBidderList);
               for (Winning w : sameBidderList) {
                  totalAverageBid += w.averagePayPrice;
               }
            }
         }
         uncommonBundleMap.put(tempList, totalAverageBid);
         tempNestedList.add(tempList);
      }
      for (List<Winning> wl1 : tempNestedList) {
         tempNestedList = tempNestedList.subList(1, tempNestedList.size());
         for (List<Winning> wl2 : tempNestedList) {
            if (wl1.containsAll(wl2)) {
               uncommonBundleMap.remove(wl1);
            } else if (wl2.containsAll(wl1)) {
               uncommonBundleMap.remove(wl2);
            }
         }
      }
      /**
       * Get the best combination
       */
      double highestPrice = (Collections.max(uncommonBundleMap.values()));
      for (Entry<List<Winning>, Double> entry : uncommonBundleMap.entrySet()) {
         if (entry.getValue() == highestPrice) {
            winningList = entry.getKey();
            break;
         }
      }
      return winningList;
   }
}
