package AuctioneerUI;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import Auction.Auction;
import Auction.TransportRequest;

public class AuctionTableModel extends AbstractTableModel {
	
	private List<Auction> auctions;
	private final String[] columnNames = new String[] { "ID", "Transport request", "Owner", "Iteration" };

	public AuctionTableModel(List<Auction> auctions) {
		this.auctions = auctions;
	}

	@Override
	public int getColumnCount() {
		return 4;
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
		TransportRequest request = auction.getTransportRequest();

		switch (column) {
			case 0:
				return request.getId();
			case 1:
				return request.getRouteString();
			case 2:
				return request.getOwner().getDisplayname();
			case 3:
				return Integer.toString(auction.getIteration());
		}

		return null;
	}

}
