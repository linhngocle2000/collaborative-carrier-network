package AuctioneerUI;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import Auction.Auction;
import Auction.TransportRequest;

public class AuctionTableModel extends AbstractTableModel {
	
	private List<Auction> auctions;
	private final String[] columnNames = new String[] { "ID", "Transport request", "Owner", "Type of auction", "Iteration" };

	public AuctionTableModel(List<Auction> auctions) {
		this.auctions = auctions;
	}

	@Override
	public int getColumnCount() {
		return 5;
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

		switch (column) {
			case 0:
				return Integer.toString(auction.getID());
			case 1:
				return auction.getDefaultTransportRequest().getRouteString();
			case 2:
				return auction.getDefaultTransportRequest().getOwner().getDisplayname();
			case 3:
				return auction.getType();
			case 4:
				return Integer.toString(auction.getIteration());
		}

		return null;
	}

}
