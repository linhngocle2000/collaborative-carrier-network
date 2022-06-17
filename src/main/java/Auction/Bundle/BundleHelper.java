package Auction.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Auction.TransportRequest;

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
}
