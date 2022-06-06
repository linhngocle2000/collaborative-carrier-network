package AuctioneerUI;

import UIResource.AuctionRequestData;

import UIResource.UIData;
import UIResource.scrollbar.ScrollBarCustom;
import javax.swing.*;
import javax.swing.border.Border;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;


public class StartAuctionUI extends JFrame {
    private static JButton logoutBtn;
    private JLabel errorLabel, nameLabel;
    private boolean auctionStarted;

    Object[][] data = {
            {"((0,1),(2,3))", "Smith", "1000", "0"},
            {"((0,1),(2,3))", "Amy", "1000", "0"},
            {"((0,1),(2,3))", "Linh", "1000", "0"},
            {"((0,1),(2,3))", "Peter", "1000", "0"},
            {"((0,1),(2,3))", "Paul", "1000", "0"},
            {"((0,1),(2,3))", "Jack", "1000", "0"},
            {"((0,1),(2,3))", "Daniel", "1000", "0"},
            {"((0,1),(2,3))", "Julia", "1000", "0"},
            {"((0,1),(2,3))", "Emily", "1000", "0"},
            {"((0,1),(2,3))", "Rachel", "1000", "0"},
            {"((4,5),(5,6))", "John", "1000", "0"},
            {"((4,5),(5,6))", "Josh", "1000", "0"},
            {"((4,5),(5,6))", "May", "1000", "0"}
    };

    public StartAuctionUI() {

        super();

        setTitle("CCN");
        setSize(540, 580);
        setMinimumSize(new Dimension(540, 580));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        auctionStarted = false;

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        Color background = UIData.getBackground();
        topPanel.setBackground(background);

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBackground(background);


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


        logoutBtn = new JButton();
        logoutBtn.setText("<HTML><U>Logout</U></HTML>");
        logoutBtn.setFocusPainted(false);
        Border emptyBorder = UIData.getEmptyBorder();
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

        JTable table = new JTable(data, AuctionRequestData.getColumnNames()) {
            private Border outside = new MatteBorder(1, 0, 1, 0, Color.BLACK);
            private Border inside = new EmptyBorder(0, 1, 0, 1);
            private Border highlight = new CompoundBorder(outside, inside);

            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent)c;

                // Add a border to the selected row

                if (isRowSelected(row)) {
                    jc.setBorder(highlight);
                }

                return c;
            }
        };

        table.changeSelection(0, 0, false, false);

        table.setRowHeight(25);
        table.setIntercellSpacing(new Dimension(0, 0));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(125);
        columnModel.getColumn(1).setPreferredWidth(95);
        columnModel.getColumn(2).setPreferredWidth(80);
        columnModel.getColumn(3).setPreferredWidth(30);
        for (int i = 0; i<4; i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.clearSelection();

        if (data.length <= 12) {
            scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, data.length*25+23));
        } else {
            scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 323));
            scrollPane.setVerticalScrollBar(new ScrollBarCustom(data.length));
        }

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

        table.getSelectionModel().addListSelectionListener(event -> startBtn.setEnabled(table.getSelectedRowCount() == 1 && !auctionStarted));

        startBtn.addActionListener(e -> {
            AuctionUI auctionUI = new AuctionUI();
            int iter = Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 3).toString());
            auctionUI.setTrReq(table.getModel().getValueAt(table.getSelectedRow(), 0).toString());
            auctionUI.setOwner(table.getModel().getValueAt(table.getSelectedRow(), 1).toString());
            auctionUI.setStartPrice(table.getModel().getValueAt(table.getSelectedRow(), 2).toString());
            auctionUI.setIteration(Integer.toString(iter+1));
            auctionUI.setVisible(true);
            startBtn.setEnabled(false);
            auctionStarted = true;
            new Timer(180_000, (eBtn) -> {
                auctionStarted = false;
                if (table.getSelectedRowCount()==1) {
                    startBtn.setEnabled(true);
                }
            }).start();
        });

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(0, 0, 0, 0);
        rootPanel.add(scrollPane, constraints);

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

//        int delay = 1000; //milliseconds
//        ActionListener taskPerformer = new ActionListener() {
//            public void actionPerformed(ActionEvent evt) {
//
//            }
//        };
//        new Timer(delay, taskPerformer).start();


        getContentPane().add(rootPanel, BorderLayout.CENTER);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().setBackground(background);

        pack();

        setResizable(false);
    }


    public static JButton getLogoutBtn() {
        return logoutBtn;
    }

    public void setNameLabel(String s) {
        nameLabel.setText(s);
    }

}
