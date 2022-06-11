//package Auction;
//
//import java.util.List;
//
//import Agent.CarrierAgent;
//import UIResource.HTTPResource.HTTPRequests;
//
//public class Auction {
//   private List<CarrierAgent> bidderList;
//   private CarrierAgent topBidder;
//   private CarrierAgent secondBidder;
//
//   public Auction() {}
//
//   private void refreshBidderList() {
//      bidderList = HTTPRequests.getCarrierAgents();
//      for (CarrierAgent bidder : bidderList) {
//         if (bidder.getBidValue() == 0) {
//            bidderList.remove(bidder);
//         }
//      }
//   }
//
//   private void computeWinner() {
//      refreshBidderList();
//      for (CarrierAgent bidder : bidderList) {
//         if (topBidder == null) {
//            topBidder = bidder;
//         }
//         if (bidder.getBidValue() > topBidder.getBidValue()) {
//            secondBidder = checkSecondBidder(secondBidder, topBidder);
//            topBidder = bidder;
//         } else {
//            secondBidder = checkSecondBidder(secondBidder, bidder);
//         }
//      }
//   }
//
//   private CarrierAgent checkSecondBidder(CarrierAgent secondBidder, CarrierAgent compareBidder) {
//      if (secondBidder.getBidValue() < compareBidder.getBidValue() || secondBidder == null) {
//         return compareBidder;
//      } else {
//         return secondBidder;
//      }
//   }
//
//   public CarrierAgent getTopBidder() {
//      computeWinner();
//      return this.topBidder;
//   }
//
//   public CarrierAgent getSecondBidValue() {
//      computeWinner();
//      return this.secondBidder.getBidValue();
//   }
//
//}
