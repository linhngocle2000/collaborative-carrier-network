package CarrierUI;

import Auction.TransportRequest;
import Utils.TourPlanning;
import Utils.TourVisual;
import UIResource.UIData;
import UIResource.scrollbar.ScrollBarCustom;
import UIResource.TextIcon;
import Agent.CarrierAgent;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.List;

import com.graphhopper.jsprit.core.problem.Location;
import org.openide.awt.*;

public class AdministrationUI extends JFrame {

    private Color background = UIData.getBackground();
    private Font font = UIData.getFont();
    private Color errorColor = UIData.getErrorColor();
    private Border emptyBorder = UIData.getEmptyBorder();
    private VisualizationUI visUI;
    private JPanel leftVisualPanel, rightVisualPanel;
    
    private TourPlanning tour;
    private TransportRequestTableModel model;
    private CalculatorUI costCalcUI;

    public AdministrationUI(CarrierAgent carrier) {

        tour = new TourPlanning(carrier);
        model = new TransportRequestTableModel(tour);
        visUI = new VisualizationUI();
        leftVisualPanel = visUI.getLeftVisualPanel();
        rightVisualPanel = visUI.getRightVisualPanel();
        costCalcUI = new CalculatorUI(tour);

///////////
// Frame
///////////

        setMinimumSize(new Dimension(650, 720));
        setPreferredSize(new Dimension(650, 720));
        setTitle("CCN");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

///////////
// Panels
///////////

        JPanel rootPanel = new JPanel();
        rootPanel.setBackground(background);
        rootPanel.setLayout(new GridBagLayout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(background);

        JPanel totalPanel = new JPanel();
        TitledBorder totalTitle = new TitledBorder("Revenue report");
        totalTitle.setTitleJustification(TitledBorder.CENTER);
        totalTitle.setTitlePosition(TitledBorder.TOP);
        totalTitle.setTitleFont(font.deriveFont(Font.BOLD, 14));
        totalPanel.setBorder(totalTitle);
        totalPanel.setLayout(new GridBagLayout());
        totalPanel.setMinimumSize(new Dimension(500, 110));
        totalPanel.setPreferredSize(new Dimension(500, 110));

///////////
// Table
///////////

        JLabel tableHeader = new JLabel("Transport requests");
        tableHeader.setFont(font.deriveFont(Font.BOLD, 16));
        tableHeader.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 30, 0);
        rootPanel.add(tableHeader, constraints);

        JTable table = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent)c;
                // Add a border to the selected row
                if (isRowSelected(row)) {
                    jc.setBorder(emptyBorder);
                }
                return c;
            }
        };
        table.setShowHorizontalLines(false);
        table.setSelectionBackground(new Color(222, 222, 222, 255));
        table.setRowHeight(25);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.clearSelection();
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(70);
        columnModel.getColumn(1).setPreferredWidth(230);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(100);
        for (int i = 0; i<4; i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        if (tour.getRequests().size() <= 12) {
            scrollPane.setPreferredSize(new Dimension(550, tour.getRequests().size()*25+23));
        } else {
            scrollPane.setPreferredSize(new Dimension(550, 323));
            scrollPane.setVerticalScrollBar(new ScrollBarCustom(12, tour.getRequests().size()));
        }

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 0, 0);
        rootPanel.add(scrollPane, constraints);

///////////
// Report
///////////

        JLabel totalCostLabel = new JLabel("Total cost (\u20AC):");
        totalCostLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(20, 0, 10, 5);
        totalPanel.add(totalCostLabel, constraints);

        JLabel totalCost = new JLabel();
        totalCost.setText(String.format("%.2f", tour.getTotalOut()).replace(",","."));
        totalCost.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(20, 0, 10, 70);
        totalPanel.add(totalCost, constraints);

        JLabel totalEarningLabel = new JLabel("Total earning (\u20AC):");
        totalEarningLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(20, 0, 10, 5);
        totalPanel.add(totalEarningLabel, constraints);

        JLabel totalEarning = new JLabel();
        totalEarning.setText(String.format("%.2f", tour.getTotalIn()).replace(",","."));
        totalEarning.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(20, 0, 10, 0);
        totalPanel.add(totalEarning, constraints);

        JLabel totalRevenueLabel = new JLabel("Total revenue (\u20AC):");
        totalRevenueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(10, 0, 20, 5);
        totalPanel.add(totalRevenueLabel, constraints);

        JLabel totalRevenue = new JLabel();
        totalRevenue.setText(String.format("%.2f", tour.getRevenueTotal()).replace(",","."));
        totalRevenue.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(10, 0, 20, 70);
        totalPanel.add(totalRevenue, constraints);

        JLabel totalProfitLabel = new JLabel("Total profit (\u20AC):");
        totalProfitLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(10, 0, 20, 5);
        totalPanel.add(totalProfitLabel, constraints);

        JLabel totalProfit = new JLabel();
        totalProfit.setText(String.format("%.2f", tour.getRevenueSum()).replace(",","."));
        totalProfit.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(10, 0, 20, 0);
        totalPanel.add(totalProfit, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(30, 0, 0, 0);
        rootPanel.add(totalPanel, constraints);

///////////
// Buttons
///////////

        JButton auctionOff = new JButton("Auction off");
        auctionOff.setFocusPainted(false);
        auctionOff.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(event -> {
            if (table.getSelectedRowCount()==1) {
                TransportRequest request = model.getRequest(table.getSelectedRow());
                auctionOff.setEnabled(request != null);
                // String note = table.getValueAt(table.getSelectedRow(),3).toString();
                // auctionOff.setEnabled(note.equals("ADDED") || note.equals(""));
            } else {
                auctionOff.setEnabled(false);
            }
        });

        JButton costCalc = new JButton("Cost calculator");
        costCalc.setFocusPainted(false);
        costCalc.addActionListener(e -> {
            if(!costCalcUI.isVisible()) {
                costCalcUI.setVisible(true);
            }
        });

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem visualizeInP1 = new JMenuItem("Panel 1");
        popupMenu.add(visualizeInP1);
        visualizeInP1.addActionListener(e -> {
            if(!visUI.isVisible()) {
                visUI.setVisible(true);
            }
            Location depot = tour.getDepot();
            TourVisual currentTour = new TourVisual(depot, carrier.getUsername());
            int[] selectedRow = table.getSelectedRows();
            // float[] tr;
            // String requestID;
            for (int row : selectedRow) {
                TransportRequest request = model.getRequest(row);
                currentTour.addRequest(request.getID(), request.getPickup(), request.getDelivery());
            }
            // for (int i = 0; i < table.getSelectedRowCount(); i++) {
            //     requestID = table.getValueAt(selectedRow[i],0).toString();
            //     tr = Converter.convertTransportRequests(table.getValueAt(selectedRow[i],1).toString());
            //     Location pickup = Location.newInstance(tr[0], tr[1]);
            //     Location deliver = Location.newInstance(tr[2], tr[3]);
            //     currentTour.addRequest(requestID, pickup, deliver);
            // }
            leftVisualPanel.removeAll();
            leftVisualPanel.add(currentTour.visualize());
            leftVisualPanel.revalidate();
        });

        JMenuItem visualizeInP2 = new JMenuItem("Panel 2");
        popupMenu.add(visualizeInP2);
        visualizeInP2.addActionListener(e -> {
            if(!visUI.isVisible()) {
                visUI.setVisible(true);
            }
            Location depot = tour.getDepot();
            TourVisual currentTour = new TourVisual(depot, carrier.getUsername());
            int[] selectedRow = table.getSelectedRows();
            // float[] tr;
            // String requestID;
            for (int row : selectedRow) {
                TransportRequest request = model.getRequest(row);
                currentTour.addRequest(request.getID(), request.getPickup(), request.getDelivery());
            }
            // for (int i = 0; i < table.getSelectedRowCount(); i++) {
            //     requestID = table.getValueAt(selectedRow[i],0).toString();
            //     tr = Converter.convertTransportRequests(table.getValueAt(selectedRow[i],1).toString());
            //     Location pickup = Location.newInstance(tr[0], tr[1]);
            //     Location deliver = Location.newInstance(tr[2], tr[3]);
            //     currentTour.addRequest(requestID, pickup, deliver);
            // }
            rightVisualPanel.removeAll();
            rightVisualPanel.add(currentTour.visualize());
            rightVisualPanel.revalidate();
        });

        TextIcon icon = new TextIcon(bottomPanel, "Show in", TextIcon.Layout.HORIZONTAL);
        icon.setFont(font.deriveFont(Font.BOLD, 12));
        JButton showIn = DropDownButtonFactory.createDropDownButton(icon, popupMenu);
        showIn.setFocusPainted(false);
        showIn.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(event -> showIn.setEnabled(table.getSelectedRowCount() > 0));

        auctionOff.setPreferredSize(costCalc.getPreferredSize());

        bottomPanel.add(auctionOff);
        bottomPanel.add(showIn);
        bottomPanel.add(costCalc);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(30, 0, 0, 0);
        rootPanel.add(bottomPanel, constraints);

///////////
// Combine
///////////

        getContentPane().add(rootPanel, BorderLayout.CENTER);

        pack();

        setResizable(false);

    }
}
