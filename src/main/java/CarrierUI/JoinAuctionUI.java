package CarrierUI;

import UIResource.UIData;
import UIResource.HTTPResource.HTTPRequests;
import UIResource.scrollbar.ScrollBarCustom;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import Agent.CarrierAgent;
import Auction.Auction;
import Auction.Bid;
import AuctioneerUI.AuctionTableModel;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JoinAuctionUI extends JFrame {

    private JButton bidBtn, logoutBtn, myTRBtn;
    private JTextField priceText;
    private JLabel errorLabel, nameLabel;
    private JTable table;
    private JScrollPane scrollPane;

    private Color background = UIData.getBackground();
    private int width = UIData.getWidth();
    private int height = UIData.getHeight();
    private Font font = UIData.getFont();
    private Color errorColor = UIData.getErrorColor();
    private Border emptyBorder = UIData.getEmptyBorder();

    private CarrierAgent agent;
    private Auction selectedAuction;

    public JoinAuctionUI() {

        ///////////
        // Frame
        ///////////

        setTitle("CCN");
        setSize(width, height);
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        ///////////
        // Panels
        ///////////

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(background);
        leftPanel.setPreferredSize(new Dimension(width - 100, height));

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBackground(background);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBackground(background);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(width + 100, height));

        JPanel rightTablePanel = new JPanel();
        rightTablePanel.setLayout(new GridBagLayout());
        rightTablePanel.setBackground(background);

        JPanel rightTopPanel = new JPanel();
        rightTopPanel.setLayout(new GridBagLayout());
        rightTopPanel.setBackground(background);

        ///////////
        // Left-Top
        ///////////

        JLabel loginLabel = new JLabel("Login as: ");
        loginLabel.setFont(font.deriveFont(Font.BOLD, 14));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(20, 0, 0, 0);
        topPanel.add(loginLabel, constraints);

        nameLabel = new JLabel();
        nameLabel.setFont(font.deriveFont(Font.BOLD, 14));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(20, 0, 0, 0);
        topPanel.add(nameLabel, constraints);

        ///////////
        // Left-middle
        ///////////

        myTRBtn = new JButton();
        myTRBtn.setText("<HTML><U>My transport requests</U></HTML>");
        myTRBtn.setFocusPainted(false);
        myTRBtn.setBorder(emptyBorder);
        myTRBtn.setBackground(background);
        myTRBtn.setFont(font.deriveFont(Font.PLAIN, 12));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 25, 0);
        rootPanel.add(myTRBtn, constraints);

        JLabel bidLabel = new JLabel("Bid (\u20AC)");
        bidLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(12, 3, 0, 5);
        rootPanel.add(bidLabel, constraints);

        priceText = new JTextField();
        priceText.setPreferredSize(new Dimension(80, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 5, 5, 3);
        rootPanel.add(priceText, constraints);

        bidBtn = new JButton();
        bidBtn.setText("Bid");
        bidBtn.setFocusPainted(false);
        bidBtn.setEnabled(false);
        bidBtn.addActionListener(e -> {
            errorLabel.setText("");
            List<Bid> bids = HTTPRequests.getBids(selectedAuction);
            assert bids != null;
            Optional<Bid> bid = bids.stream().filter(x -> x.getBidder().getUsername().equals(agent.getUsername())).findFirst();
            if (bid.isPresent()) {
                errorLabel.setText("You can only bid once");
            } else {
                try {
                    int price = Integer.parseInt(priceText.getText());
                    Bid bidValid = HTTPRequests.addBid(selectedAuction, agent, price);
                    if (bidValid == null) {
                        errorLabel.setText(HTTPRequests.getLastError().getMessage());
                    } else {
                        bidBtn.setEnabled(false);
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    errorLabel.setText(ex.getMessage());
                }
            }
        });

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(30, 3, 10, 3);
        rootPanel.add(bidBtn, constraints);

        ///////////
        // Left-bottom
        ///////////

        logoutBtn = new JButton();
        logoutBtn.setText("<HTML><U>Logout</U></HTML>");
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(emptyBorder);
        logoutBtn.setBackground(background);
        logoutBtn.setFont(font.deriveFont(Font.PLAIN, 13));
        logoutBtn.setForeground(Color.BLUE);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 20, 0);
        bottomPanel.add(logoutBtn, constraints);

        ///////////
        // Log
        ///////////

        errorLabel = new JLabel();
        errorLabel.setForeground(errorColor);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // Invalid price input
        // Invalid auction name
        errorLabel.setText("");

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = java.awt.GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(10, 3, 10, 3);
        rootPanel.add(errorLabel, constraints);

        ///////////
        // Combine left
        ///////////

        leftPanel.add(topPanel, BorderLayout.NORTH);
        leftPanel.add(bottomPanel, BorderLayout.SOUTH);
        leftPanel.add(rootPanel, BorderLayout.CENTER);

        ///////////
        // Table
        ///////////

        JLabel tableHeader = new JLabel("Active auctions");
        tableHeader.setFont(font.deriveFont(Font.BOLD, 15));
        tableHeader.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 15, 0);
        rightTablePanel.add(tableHeader, constraints);

        table = new JTable() {
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent) c;
                Border lastBorder = new MatteBorder(0,0,1,0,Color.BLACK);
                // Add a border to the selected row
                if (isRowSelected(row)) {
                    jc.setBorder(emptyBorder);
                }
                if(row == getModel().getRowCount()-1 && row<12) {
                    jc.setBorder(lastBorder);
                }
                return c;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(false);
        table.setSelectionBackground(new Color(222, 222, 222, 255));
        table.setRowHeight(25);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.clearSelection();
        table.getSelectionModel().addListSelectionListener(event -> {
            int row = table.getSelectedRow();
            CarrierTableModel model = (CarrierTableModel)table.getModel();
            selectedAuction = model.getAuction(row);

            // Check if a bid has already been placed on the selected auction
            bidBtn.setEnabled(selectedAuction != null);
        });

        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(350, 273));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 0, 0);
        rightTablePanel.add(scrollPane, constraints);

///////////
// Combine right
///////////

        rightPanel.add(rightTopPanel, BorderLayout.NORTH);
        rightPanel.add(rightTablePanel, BorderLayout.CENTER);

///////////
// Combine all
///////////

        getContentPane().add(leftPanel, BorderLayout.LINE_START);
        getContentPane().add(rightPanel, BorderLayout.CENTER);

        pack();

        setResizable(false);
    }

    /**
     * Starts periodically updating list of auctions.
     * Maybe implement this as reload button like in StartAuctionUI instead of
     * periodic updates 😐
     */
    public void loadTable() {

        // Update table model
        var auctions = HTTPRequests.getActiveAuctions();
        var model = new CarrierTableModel(auctions);
        table.setModel(model);

        assert auctions != null;
        scrollPane.setVerticalScrollBar(new ScrollBarCustom(10, auctions.size()));
        table.invalidate();
        scrollPane.repaint();


        // Set column width and scrollbar length
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(270);
        for (int i = 0; i < 2; i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }

    }

    public JButton getLogoutBtn() {
        return logoutBtn;
    }

    public JButton getMyTRBtn() {
        return myTRBtn;
    }

    public void setAgent(CarrierAgent agent) {
        this.agent = agent;
        nameLabel.setText(agent.getDisplayname());
    }

    public void reset() {
        table.clearSelection();
        priceText.setText("");
        errorLabel.setText("");
    }

}
