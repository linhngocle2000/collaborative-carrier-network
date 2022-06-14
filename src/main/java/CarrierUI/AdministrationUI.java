package CarrierUI;

import Auction.Auction;
import Auction.TransportRequest;
import Utils.TourPlanning;
import UIResource.UIData;
import UIResource.HTTPResource.HTTPRequests;
import UIResource.TextIcon;
import Agent.CarrierAgent;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

import org.openide.awt.*;

public class AdministrationUI extends JFrame {

    private Color background = UIData.getBackground();
    private Font font = UIData.getFont();
    private Color errorColor = UIData.getErrorColor();
    private Border emptyBorder = UIData.getEmptyBorder();
    private VisualizationUI visUI;
    private JPanel leftVisualPanel, rightVisualPanel;
    private JTable table;
    
    private CarrierAgent carrier;
    private TourPlanning tour;
    private TransportRequestTableModel model;
    private CalculatorUI costCalcUI;

    public AdministrationUI(CarrierAgent carrier) {
        this.carrier = carrier;
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

        JPanel topPanel = new JPanel();
        topPanel.setBackground(background);
        topPanel.setLayout(new GridBagLayout());

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
// Panels
///////////

        JButton reloadBtn = new JButton("\u27f3");
        reloadBtn.setFont(font.deriveFont(Font.BOLD, 28));
        reloadBtn.setBorder(emptyBorder);
        reloadBtn.setBackground(background);
        reloadBtn.setHorizontalAlignment(SwingConstants.CENTER);
        reloadBtn.addActionListener(e -> {
            reloadRequests();
        });

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.insets = new Insets(10, 0, 0, 35);
        topPanel.add(reloadBtn, constraints);

///////////
// Table
///////////

        JLabel tableHeader = new JLabel("Transport requests");
        tableHeader.setFont(font.deriveFont(Font.BOLD, 16));
        tableHeader.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 30, 0);
        rootPanel.add(tableHeader, constraints);

        table = new JTable(model) {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Border lastBorder = new MatteBorder(0,0,1,0,Color.BLACK);
                Component c = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent)c;
                // Add a border to the selected row
                if (isRowSelected(row)) {
                    jc.setBorder(emptyBorder);
                }
                if (row == getModel().getRowCount()-1) {
                    jc.setBorder(lastBorder);
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
        scrollPane.setPreferredSize(new Dimension(550, 323));

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

        JLabel totalCost = new JLabel("-");
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

        JLabel totalEarning = new JLabel("-");
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

        JLabel totalRevenue = new JLabel("-");
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

        JLabel totalProfit = new JLabel("-");
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
        
        totalCost.setText(String.format("%.2f", tour.getTotalOut()).replace(",", "."));
        totalEarning.setText(String.format("%.2f", tour.getTotalIn()).replace(", ", "."));
        totalRevenue.setText(String.format("%.2f", tour.getRevenueTotal()).replace(",", "."));
        totalProfit.setText(String.format("%.2f", tour.getRevenueSum()).replace(",", "."));

///////////
// Buttons
///////////

        JButton auctionOff = new JButton("Auction off");
        auctionOff.setFocusPainted(false);
        auctionOff.setEnabled(false);
        table.getSelectionModel().addListSelectionListener(event -> {
            if (table.getSelectedRowCount()==1) {
                TransportRequest request = model.getRequest(table.getSelectedRow());
                auctionOff.setEnabled(request != null && !request.isInAuction());
            } else {
                auctionOff.setEnabled(false);
            }
        });
        auctionOff.addActionListener(e -> {
            auctionOff.setEnabled(false);
            TransportRequest request = model.getRequest(table.getSelectedRow());
            Auction auction = HTTPRequests.addAuction();
            if (auction != null && HTTPRequests.addTransportRequestToAuction(auction, request)) {
                reloadRequests();
            } else {
                auctionOff.setEnabled(true);
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
            TourPlanning currentTour = new TourPlanning(carrier.getUsername());
            int[] selectedRow = table.getSelectedRows();
            for (int row : selectedRow) {
                TransportRequest request = model.getRequest(row);
                currentTour.addRequest(request);
            }
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
            TourPlanning currentTour = new TourPlanning(carrier.getUsername());
            int[] selectedRow = table.getSelectedRows();
            for (int row : selectedRow) {
                TransportRequest request = model.getRequest(row);
                currentTour.addRequest(request);
            }
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
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(rootPanel, BorderLayout.CENTER);

        pack();

        setResizable(false);

    }

    private void reloadRequests() {
        tour.refreshRequests();
        model.refreshTour();
    }

}
