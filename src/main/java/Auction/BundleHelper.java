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
   private final double radiusDepotMax = 80;
   private final int maxDepotSize = 5;

   /**
    * This method is used to generate the list of special depot loc
    */
   List<Location> generateSpecialDepots() {
      List<Location> specialDepotList = getDepotList();
      List<Location> tempDepotList;
      // specialDepotList must have more than maxDepotSize (=5) elements
      while (specialDepotList.size() > maxDepotSize) {
         // generate list of special depots from specialDepotList
         tempDepotList = generateSpecialDepots(specialDepotList);
         if (specialDepotList.equals(tempDepotList)) {
            /**
             * At this point if the same list is generated again,
             * then we can double the radius or just break the loop and accept the list.
             */
            if (radiusDepot == radiusDepotMax) {
               return specialDepotList;
            }
            radiusDepot *= 2;
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
      // for each location a in depotList
      for (Location focusedDepot : depotList) {
         // calculate special depot of a as focused loc and all locations that within 5(length unit) from a
         specialDepot = getSpecialDepot(gatherDepot(focusedDepot, depotList));
         // check if calculated special depot already existed in result
         if (specialDepotList.contains(specialDepot)) {
            continue;
         }
         // add to result
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
      // dor each location a in depot list
      for (Location depot : depotList) {
         distance = EuclideanDistanceCalculator.calculateDistance(focusedDepot.getCoordinate(), depot.getCoordinate());
         // if distance between a and focused depot <= radiusDepot (=5)
         if (distance <= radiusDepot) {
            // add to result
            gatherList.add(depot);
         }
      }
      return gatherList;
   }

   /**
    * This method calculate a special depot loc from a gather list of depot loc
    */
   private Location getSpecialDepot(List<Location> gatherList) {
      // gatherList only has 1 element -> return
      if (gatherList.size() == 1) {
         return gatherList.get(0);
      }
      double depotX = 0, depotY = 0;
      // sum all x-coordinate and y-coordinate of all locations in gatherList
      for (Location depot : gatherList) {
         depotX += depot.getCoordinate().getX();
         depotY += depot.getCoordinate().getY();
      }
      // return a location that has the average x and y coordinate of all locations in gatherList
      return Location.newInstance(depotX / gatherList.size(), depotY / gatherList.size());
   }

   /**
    * This method get all depots of registered carrier
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
   private final double radiusRequestMax = 50;
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
            bundle = new ArrayList<>();
            bundle.add(request);
            bundleList.add(bundle);
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
      // bundle size must be smaller or equals to 3
      while (bundle.size() < minBundleSize) {
         if (radiusRequest == radiusRequestMax) {
            break;
         }
         bundle.clear();

         // gather all request within radiusRequest from specialDepot
         gatherList = gatherRequestList(specialDepot);
         // add requests to tour
         tour.setRequests(gatherList);
         // calculate profit of each request in tour
         for (TransportRequest request : gatherList) {
            if (tour.getProfit(request) > minProfit) {
               // if makes profit add to result
               bundle.add(request);
               // if bundle is full (=3) -> return
               if (bundle.size() == maxBundleSize) {
                  return bundle;
               }
            }
         }
         // increase radiusRequest, initially 10
         radiusRequest += 10;
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
      // for each transport req a
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
         // add to result if pickup and delivery of a within radiusRequest from specialDepot
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
   List<List<TransportRequest>> checkAndRemoveSubset(List<List<TransportRequest>> bundleList){
      List<List<TransportRequest>> returnBundleList = new ArrayList<>(bundleList);
      for (List<TransportRequest> bundle1 : bundleList) {
         // if bundleList contains only 1 element, don't check
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
               if (!unsoldList.contains(request)) {
                  unsoldList.add(request);
               }
            }
            auctionList.remove(auction);
         }
      }
      List<String> preUsername = new ArrayList<>();
      double payPrice;
      List<TransportRequest> temp;
      List<TransportRequest> newBundle;
      List<List<TransportRequest>> bundleList;
      for (Winning w1 : winningList) {
         if (preUsername.contains(w1.bidderUsername)) {
            continue;
         }
         preUsername.add(w1.bidderUsername);
         newBundle = new ArrayList<>(w1.bundle);
         bundleList = new ArrayList<>();
         bundleList.add(w1.bundle);
         payPrice = w1.averagePayPrice * w1.bundle.size();
         for (Winning w2 : winningList) {
            if (Objects.equals(w1, w2)) {
               continue;
            }
            if (Objects.equals(w1.bidderUsername, w2.bidderUsername)) {
               temp = new ArrayList<>(newBundle);
               bundleList.add(w2.bundle);
               temp.retainAll(w2.bundle);
               newBundle.removeAll(temp);
               newBundle.addAll(w2.bundle);
               payPrice += w2.averagePayPrice * (w2.bundle.size() - temp.size());
            }
         }
         if (bundleList.size() != 1) {
            Auction tempAuction = new Auction();
            for (Auction auction : bundleAuctions) {
               if (bundleList.contains(auction.getTransportRequests())) {
                  auctionList.remove(auction);
                  tempAuction = auction;
               }
            }
            tempAuction.setTransportRequests(newBundle);
            tempAuction.setWinningPayPrice(payPrice);
            auctionList.add(tempAuction);
         }
      }
      temp = new ArrayList<>(unsoldList);
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
