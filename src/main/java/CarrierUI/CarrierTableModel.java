package CarrierUI;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import Auction.Auction;
import Auction.TransportRequest;

public class CarrierTableModel extends AbstractTableModel {

    private List<Auction> auctions;
    private final String[] columnNames = new String[] { "ID", "Transport request"};

    public CarrierTableModel(List<Auction> auctions) {
        this.auctions = auctions;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public int getRowCount() {
        return auctions.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        Auction auction = auctions.get(row);
        TransportRequest request = auction.getDefaultTransportRequest();

        switch (column) {
            case 0:
                return Integer.toString(auction.getID());
            case 1:
                if (request == null) {
                    return "No transport request added yet";
                }
                return request.getRouteString();
        }

        return null;
    }

    public Auction getAuction(int row) {
        if (row < auctions.size() && row >= 0) {
            return auctions.get(row);
        }
        return null;
    }

}
