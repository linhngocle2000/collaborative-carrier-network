package UIResource;

public class TableData {
    private static String[] auctionColumnNames = {"Transport request", "Owner", "Iteration"};
    private static String[] requestColumnNames = {"Transport request", "Profit (\u20AC)"};

    public static String[] getAuctionColumnNames() {
        return auctionColumnNames;
    }

    public static String[] getRequestColumnNames() {
        return requestColumnNames;
    }
}
