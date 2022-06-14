package CarrierUI;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import Auction.TransportRequest;
import Utils.TourPlanning;

public class TransportRequestTableModel extends AbstractTableModel {

	private final static String[] columns = { "ID", "Transport request", "Profit (\u20AC)", "Notes" };
	private HashMap<TransportRequest, Double> profitMap;
	private TourPlanning tour;

	public TransportRequestTableModel(TourPlanning tour) {
		this.tour = tour;
		profitMap = new HashMap<TransportRequest, Double>();
		for (TransportRequest t : tour.getRequests()) {
			profitMap.put(t, tour.getProfit(t));
		}
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return tour.getRequests().size();
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public Object getValueAt(int row, int column) {
		TransportRequest request = tour.getRequests().get(row);
		switch (column) {
			case 0:
				return request.getID();
			case 1:
				return request.getRouteString();
			case 2:
				if (profitMap.containsKey(request)) {
					return String.format("%.2f", profitMap.get(request));
				}
				return "-";
			case 3:
				return request.isInAuction() ? "Is in auction" : "";
		}
		return null;
	}

	public void refreshTour() {
		profitMap.clear();
		for (TransportRequest t : tour.getRequests()) {
			profitMap.put(t, tour.getProfit(t));
		}
		fireTableDataChanged();
	}

	public TransportRequest getRequest(int row) {
		if (row >= 0 && row < tour.getRequests().size()) {
			return tour.getRequests().get(row);
		}
		return null;
	}
	
}
