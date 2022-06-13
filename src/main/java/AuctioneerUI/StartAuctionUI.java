package AuctioneerUI;

import UIResource.UIData;
import UIResource.HTTPResource.HTTPRequests;
import UIResource.scrollbar.ScrollBarCustom;
import javax.swing.*;
import javax.swing.border.Border;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import Agent.AuctioneerAgent;
import Auction.Auction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class StartAuctionUI extends JFrame {

    private JButton logoutBtn;
    private JLabel errorLabel, nameLabel;
    private JTable table;
    private JScrollPane scrollPane;
    private Color background = UIData.getBackground();
    private Border emptyBorder = UIData.getEmptyBorder();

    private AuctioneerAgent agent;
    AuctionUI auctionUI;
    private Auction selectedAuction;

    public StartAuctionUI() {

///////////
// Frame
///////////

        setTitle("CCN");
        setSize(620, 580);
        setMinimumSize(new Dimension(620, 580));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

///////////
// Panels
///////////

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBackground(background);

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBackground(background);

///////////
// Top
///////////

        JLabel loginLabel = new JLabel("Login as:");
        Font font = UIData.getFont();
        loginLabel.setFont(font.deriveFont(Font.BOLD, 13));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(20, 30, 0, 0);
        topPanel.add(loginLabel, constraints);

        nameLabel = new JLabel("");
        nameLabel.setFont(font.deriveFont(Font.BOLD, 13));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(20, 92, 0, 0);
        topPanel.add(nameLabel, constraints);

///////////
// Table
///////////

        JLabel tableHeader = new JLabel("Auction requests");
        tableHeader.setFont(font.deriveFont(Font.BOLD, 16));
        tableHeader.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 0, 0, 0);
        topPanel.add(tableHeader, constraints);

        table = new JTable() {
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
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(false);
        table.setSelectionBackground(new Color(222, 222, 222, 255));
        table.setRowHeight(25);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.clearSelection();


        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(520, 323));


        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(0, 0, 0, 0);
        rootPanel.add(scrollPane, constraints);

///////////
// Bottom
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
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(20, 30, 10, 0);
        bottomPanel.add(logoutBtn, constraints);

        JButton startBtn = new JButton();
        startBtn.setText("Start");
        startBtn.setFocusPainted(false);
        startBtn.setEnabled(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 0, 20, 0);
        bottomPanel.add(startBtn, constraints);

        table.getSelectionModel().addListSelectionListener(event -> {
            int row = table.getSelectedRow();
            var model = (AuctionTableModel)table.getModel();
            selectedAuction = model.getAuction(row);
            startBtn.setEnabled(selectedAuction != null);
        });

        JButton reloadBtn = new JButton("\u27f3");
        reloadBtn.setFont(font.deriveFont(Font.BOLD, 28));
        reloadBtn.setBorder(emptyBorder);
        reloadBtn.setBackground(background);
        reloadBtn.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.insets = new Insets(8, 0, 27, 30);
        bottomPanel.add(reloadBtn, constraints);

        startBtn.addActionListener(e -> {
            auctionUI = new AuctionUI();
            auctionUI.setAuction(selectedAuction);
            auctionUI.setVisible(true);
            startBtn.setEnabled(false);
        });

        reloadBtn.addActionListener(e -> loadAuctions());

        // TODO: React to auctionUI close and remove auction from list or refresh table model

///////////
// Log
///////////

        errorLabel = new JLabel();
        Color errorColor = UIData.getErrorColor();
        errorLabel.setForeground(errorColor);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // Auction already started
        errorLabel.setText("");

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = java.awt.GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(20, 0, 0, 0);
        rootPanel.add(errorLabel, constraints);

//        int delay = 10000; //milliseconds
//        ActionListener taskPerformer = new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//                loadAuctions();
//            }
//        };
//        Timer reload = new Timer(delay, taskPerformer);
//        reload.start();
//        reload.setRepeats(true);

///////////
// Combine
///////////

        getContentPane().add(rootPanel, BorderLayout.CENTER);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        pack();

        setResizable(false);
    }

    public void loadAuctions() {
        // Load auctions
        List<Auction> auctions = HTTPRequests.getAllAuctions();
        AuctionTableModel model = new AuctionTableModel(auctions);
        table.setModel(model);
        // Set scrollbar

        scrollPane.setVerticalScrollBar(new ScrollBarCustom(12, auctions.size()));
        table.invalidate();
        scrollPane.repaint();

        // Set columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(180);
        columnModel.getColumn(3).setPreferredWidth(60);
        for (int i = 0; i < 4; i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }

    }

    public JButton getLogoutBtn() {
        return logoutBtn;
    }

    public void setAgent(AuctioneerAgent agent) {
        this.agent = agent;
        nameLabel.setText(agent.getDisplayname());
    }

}
