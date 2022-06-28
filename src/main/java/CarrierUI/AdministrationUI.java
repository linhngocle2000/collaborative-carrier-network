package CarrierUI;

import Auction.TransportRequest;
import UIResource.scrollbar.ScrollBarCustom;
import Utils.Converter;
import Utils.TourPlanning;
import UIResource.UIData;
import UIResource.HTTPResource.HTTPRequests;
import Agent.CarrierAgent;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdministrationUI extends JFrame {

    private Color background = UIData.getBackground();
    private Font font = UIData.getFont();
    private Border emptyBorder = UIData.getEmptyBorder();
    private VisualizationUI visUI;
    private JPanel leftVisualPanel, rightVisualPanel;
    private JTable table;
    private JButton logoutBtn, setBtn;
    private JTextField minProfitText, maxProfitText;
    private JLabel msgLabel;

    private CarrierAgent carrier;
    private TourPlanning tour;
    private TransportRequestTableModel model;
    private Color errorColor = UIData.getErrorColor();
    private Color successColor = UIData.getSuccessColor();

    private static Logger LOGGER = LoggerFactory.getLogger(AdministrationUI.class);

    public AdministrationUI(CarrierAgent carrier) {
        this.carrier = carrier;
        try {
            tour = new TourPlanning(carrier);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model = new TransportRequestTableModel(tour);
        visUI = new VisualizationUI();
        leftVisualPanel = visUI.getLeftVisualPanel();
        rightVisualPanel = visUI.getRightVisualPanel();

///////////
// Frame
///////////

        setMinimumSize(new Dimension(630, 770));
        setPreferredSize(new Dimension(630, 770));
        setTitle("CCN");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

///////////
// Panels
///////////

        JPanel rootPanel = new JPanel();
        rootPanel.setBackground(background);
        rootPanel.setLayout(new GridBagLayout());

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(background);
        bottomPanel.setLayout(new GridBagLayout());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(background);
        btnPanel.setLayout(new GridBagLayout());

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

        table = new JTable(model);
        table.setRowSelectionAllowed(false);
        table.setCellSelectionEnabled(false);
        table.setShowHorizontalLines(false);
        table.setRowHeight(25);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(230);
        columnModel.getColumn(2).setPreferredWidth(125);
        columnModel.getColumn(3).setPreferredWidth(125);
        for (int i = 0; i<4; i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);

        if (model.getRowCount() <= 12) {
            scrollPane.setPreferredSize(new Dimension(530, model.getRowCount()*25+23));
        } else {
            scrollPane.setPreferredSize(new Dimension(530, 323));
            scrollPane.setVerticalScrollBar(new ScrollBarCustom(12, model.getRowCount()));
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
        totalEarning.setText(String.format("%.2f", tour.getTotalIn()).replace(",", "."));
        totalRevenue.setText(String.format("%.2f", tour.getRevenueTotal()).replace(",", "."));
        totalProfit.setText(String.format("%.2f", tour.getRevenueSum()).replace(",", "."));

///////////
// Buttons
///////////

        JLabel minProfit = new JLabel("Min. profit to bid (\u20AC): ");
        minProfit.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(0, 0, 0, 10);
        bottomPanel.add(minProfit, constraints);

        minProfitText = new JTextField();
        minProfitText.setText(String.format("%.1f", carrier.getMinProfit()).replace(',', '.'));
        minProfitText.setPreferredSize(new Dimension(100,22));
        minProfitText.setHorizontalAlignment(SwingConstants.CENTER);
        minProfitText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                checkBtn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                checkBtn();
            }
        });


        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(0, 0, 0, 20);
        bottomPanel.add(minProfitText, constraints);

        JLabel maxProfit = new JLabel("Max. profit to auction off (\u20AC): ");
        maxProfit.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(13, 0, 0, 10);
        bottomPanel.add(maxProfit, constraints);

        maxProfitText = new JTextField();
        maxProfitText.setText(String.format("%.1f", carrier.getMaxProfit()).replace(',', '.'));
        maxProfitText.setPreferredSize(new Dimension(100,22));
        maxProfitText.setHorizontalAlignment(SwingConstants.CENTER);
        maxProfitText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                checkBtn();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                checkBtn();
            }
        });


        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(13, 0, 0, 20);
        bottomPanel.add(maxProfitText, constraints);

        setBtn = new JButton("Set");
        setBtn.setFocusPainted(false);
        setBtn.setEnabled(false);
        setBtn.addActionListener(e -> {
            msgLabel.setText("");
            if ((!minProfitText.getText().trim().isEmpty() && Converter.checkPriceFormat(minProfitText.getText().trim())) ||
                    (!maxProfitText.getText().trim().isEmpty() && Converter.checkPriceFormat(maxProfitText.getText().trim()))) {
                msgLabel.setText("Invalid price format");
                msgLabel.setForeground(errorColor);
            } else {
                try {
                    HTTPRequests.addMinProfitToBid(carrier, Double.parseDouble(minProfitText.getText()));
                    HTTPRequests.addMaxProfitToAuctionOff(carrier, Double.parseDouble(maxProfitText.getText()));
                    msgLabel.setText("Profit set!");
                    msgLabel.setForeground(successColor);
                } catch (NumberFormatException | JSONException | IOException | InterruptedException e1) {
                    LOGGER.warn(e1.getMessage());
                    e1.printStackTrace();
                    msgLabel.setText("An error occured. Please try again");
                    msgLabel.setForeground(errorColor);
                }
            }
        });

        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(10, 0, 0, 20);
        bottomPanel.add(setBtn, constraints);

        msgLabel = new JLabel();

        msgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        msgLabel.setText("");

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(15, 0, 0, 0);
        bottomPanel.add(msgLabel, constraints);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(20, 0, 10, 0);
        rootPanel.add(bottomPanel, constraints);


        logoutBtn = new JButton("Logout");
        logoutBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 20, 0);
        btnPanel.add(logoutBtn, constraints);

        List<TransportRequest> oldRequests = null;
        try {
            oldRequests = HTTPRequests.getStashedTransportRequests(carrier);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
        TourPlanning oldTour = new TourPlanning(carrier,oldRequests);


        leftVisualPanel.removeAll();
        leftVisualPanel.add(oldTour.visualize());
        leftVisualPanel.revalidate();
        LOGGER.info("Before tour visualized");


        rightVisualPanel.removeAll();
        rightVisualPanel.add(tour.visualize());
        rightVisualPanel.revalidate();
        LOGGER.info("After tour visualized");




///////////
// Combine
///////////
        getContentPane().add(rootPanel, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);

        pack();

        setResizable(false);

    }

    private void checkBtn() {
        boolean value1 = !minProfitText.getText().trim().isEmpty();
        boolean value2 = !maxProfitText.getText().trim().isEmpty();
        setBtn.setEnabled(value1||value2);
    }


    public JButton getLogoutBtn() {
        return logoutBtn;
    }

    public VisualizationUI getVisUI() {
        return visUI;
    }


}
