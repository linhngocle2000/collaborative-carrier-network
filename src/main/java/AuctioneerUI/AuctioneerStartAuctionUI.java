package AuctioneerUI;

import UIResource.AuctionRequestData;

import UIResource.UIData;
import UIResource.scrollbar.ScrollBarCustom;
import javax.swing.*;
import javax.swing.border.Border;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;


public class AuctioneerStartAuctionUI extends JFrame {
    private static JButton logoutBtn;
    private JLabel errorLabel;
    private String name;
    private JScrollPane scrollPane;

    Object[][] data = {
            {"1", "((0,1),(2,3))", "Smith", "0"},
            {"2", "((0,1),(2,3))", "Amy", "0"},
            {"3", "((0,1),(2,3))", "Linh", "0"},
            {"4", "((0,1),(2,3))", "Peter", "0"},
            {"5", "((0,1),(2,3))", "Paul", "0"},
            {"6", "((0,1),(2,3))", "Jack", "0"},
            {"7", "((0,1),(2,3))", "Daniel", "0"},
            {"8", "((0,1),(2,3))", "Julia", "0"},
            {"9", "((0,1),(2,3))", "Emily", "0"},
            {"10", "((0,1),(2,3))", "Rachel", "0"},
            {"11", "((4,5),(5,6))", "John", "0"},
            {"12", "((4,5),(5,6))", "Josh", "0"},
            {"13", "((4,5),(5,6))", "May", "0"}
    };

    String date = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date(System.currentTimeMillis()));


    public AuctioneerStartAuctionUI() {

        super();

        setTitle("CCN");
        setSize(540, 580);
        setMinimumSize(new Dimension(540, 580));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

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


        JLabel loginLabel = new JLabel("Login as: " + date);
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


        JButton startBtn = new JButton();
        startBtn.setText("Start");
        startBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(10, 0, 20, 0);
        bottomPanel.add(startBtn, constraints);

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
        reloadBtn.setForeground(Color.GRAY);
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
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(0).setCellRenderer(centerRenderer);
        columnModel.getColumn(1).setPreferredWidth(130);
        columnModel.getColumn(1).setCellRenderer(centerRenderer);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(2).setCellRenderer(centerRenderer);
        columnModel.getColumn(3).setPreferredWidth(40);
        columnModel.getColumn(3).setCellRenderer(centerRenderer);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);

        if (data.length <= 12) {
            scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, data.length*25+23));
        } else {
            scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 323));
            scrollPane.setVerticalScrollBar(new ScrollBarCustom(data.length));
        }

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

    public void setName(String s) {
        name = s;
    }

}
