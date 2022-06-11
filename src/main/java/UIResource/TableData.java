package UIResource;

import Agent.CarrierAgent;
import Auction.TransportRequest;
import Utils.TourPlanning;

import java.util.List;

public class TableData {
    private static String[] auctionColumnNames = {"Transport request", "Owner", "Iteration"};
    private static String[] requestColumnNames = {"ID", "Transport request", "Profit (\u20AC)"};

    public static String[] getAuctionColumnNames() {
        return auctionColumnNames;
    }

    public static String[] getRequestColumnNames() {
        return requestColumnNames;
    }

    public static Object[][] createRequestObject(List<TransportRequest> trList, float[] profit) {
        Object[][] res = new Object[trList.size()][3];
        for (int i = 0; i < trList.size(); i++) {
            res[i][0] = trList.get(i).getId();
            res[i][1] = "((" + trList.get(i).getPickupX() + "," +
                    trList.get(i).getPickupX() + "),(" +
                    trList.get(i).getDeliveryX() + "," +
                    trList.get(i).getDeliveryY() + "))";
            res[i][2] = String.format("%.2f", profit[i]).replace(",",".");
        }
        return res;
    }

}
