package UIResource;

public class TableData {
    private static String[] auctionColumnNames = {"Transport request", "Owner", "Start price (\u20AC)", "Iteration"};
    private static String[] requestColumnNames = {"Transport request", "Profit"};

    public static String[] getAuctionColumnNames() {
        return auctionColumnNames;
    }

    public static String[] getRequestColumnNames() {
        return requestColumnNames;
    }
}
