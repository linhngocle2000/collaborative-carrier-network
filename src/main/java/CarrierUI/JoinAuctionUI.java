package CarrierUI;

import UIResource.TableData;
import UIResource.UIData;
import UIResource.scrollbar.ScrollBarCustom;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class JoinAuctionUI extends JFrame {

    private JButton bidBtn, logoutBtn, myTRBtn;
    private JTextField auctionText, priceText;
    private JLabel errorLabel, nameLabel;

    private Color background = UIData.getBackground();
    private int width = UIData.getWidth();
    private int height = UIData.getHeight();
    private Font font = UIData.getFont();
    private Color errorColor = UIData.getErrorColor();
    private Border emptyBorder = UIData.getEmptyBorder();

    Object[][] data = {
            {"01", "((0,1),(2,3))"},
            {"02", "((4,5),(8,9))"},
            {"03", "((32,5),(13,53))"},
            {"04", "((76,32),(-87,34))"},
            {"05", "((76,22),(4,93))"},
            {"06", "((62,13),(76,23))"},
            {"07", "((20,46),(78,23))"},
            {"08", "((10,3),(82,6))"},
            {"09", "((9,34),(29,98))"},
            {"10", "((14,97),(2,41))"},
            {"11", "((83,37),(25,6))"},
            {"12", "((94,87),(85,45))"},
            {"13", "((43,1),(36,64))"}
    };


    public JoinAuctionUI() {

        super();

        setTitle("CCN");
        setSize(width, height);
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(background);
        leftPanel.setPreferredSize(new Dimension(width-100, height));

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBackground(background);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBackground(background);


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
        nameLabel.setFont(font.deriveFont(Font.BOLD,14));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(20, 0, 0, 0);
        topPanel.add(nameLabel, constraints);


        JLabel auctionLabel = new JLabel("Request ID");
        auctionLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(12, 3, 0, 5);
        rootPanel.add(auctionLabel, constraints);

        auctionText = new JTextField();
        auctionText.setPreferredSize(new Dimension(80, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 5, 0, 3);
        rootPanel.add(auctionText, constraints);

        JLabel bidLabel = new JLabel("Bid (\u20AC)");
        bidLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(12, 3, 0, 5);
        rootPanel.add(bidLabel, constraints);

        priceText = new JTextField();
        priceText.setPreferredSize(new Dimension(80, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 5, 5, 3);
        rootPanel.add(priceText, constraints);

        bidBtn = new JButton();
        bidBtn.setText("Bid");
        bidBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(30, 3, 10, 3);
        rootPanel.add(bidBtn, constraints);

        myTRBtn = new JButton();
        myTRBtn.setText("<HTML><U>My transport requests</U></HTML>");
        myTRBtn.setFocusPainted(false);
        myTRBtn.setBorder(emptyBorder);
        myTRBtn.setBackground(background);
        myTRBtn.setFont(font.deriveFont(Font.PLAIN, 12));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 25, 0);
        rootPanel.add(myTRBtn, constraints);



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

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridBagLayout());
        rightPanel.setBackground(background);
        rightPanel.setPreferredSize(new Dimension(width+100, height));

        JLabel tableHeader = new JLabel("Active auctions");
        tableHeader.setFont(font.deriveFont(Font.BOLD, 15));
        tableHeader.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 15, 0);
        rightPanel.add(tableHeader, constraints);

        JTable table = new JTable(data, new String[]{"Request ID","Transport request"}) {

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

        table.setRowHeight(25);
        table.setIntercellSpacing(new Dimension(0, 0));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);
        columnModel.getColumn(1).setPreferredWidth(250);
        for (int i = 0; i<2; i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowSelectionAllowed(false);
        table.setShowHorizontalLines(false);

        if (data.length <= 10) {
            scrollPane.setPreferredSize(new Dimension(350, data.length*25+23));
        } else {
            scrollPane.setPreferredSize(new Dimension(350, 273));
            scrollPane.setVerticalScrollBar(new ScrollBarCustom(10, data.length));
        }

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 0, 0);


        rightPanel.add(scrollPane, constraints);

        leftPanel.add(topPanel, BorderLayout.NORTH);
        leftPanel.add(bottomPanel, BorderLayout.SOUTH);
        leftPanel.add(rootPanel, BorderLayout.CENTER);

        getContentPane().add(leftPanel, BorderLayout.LINE_START);
        getContentPane().add(rightPanel, BorderLayout.CENTER);

        pack();

        setResizable(false);
    }

    public JButton getLogoutBtn() {
        return logoutBtn;
    }

    public JButton getMyTRBtn() {
        return myTRBtn;
    }

    public void setNameLabel(String s) {
        nameLabel.setText(s);
    }

    public void reset() {
        auctionText.setText("");
        priceText.setText("");
        errorLabel.setText("");
    }


}
