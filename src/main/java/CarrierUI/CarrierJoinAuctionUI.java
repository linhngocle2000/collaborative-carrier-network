package CarrierUI;

import UIResource.UIData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CarrierJoinAuctionUI extends JFrame {

    private static JButton bidBtn, activeAuctionsBtn, logoutBtn;
    private JTextField auctionText, priceText;
    private JLabel errorLabel, nameLabel;

    private Color background = UIData.getBackground();
    private int width = UIData.getWidth();
    private int height = UIData.getHeight();
    private Font font = UIData.getFont();
    private Color errorColor = UIData.getErrorColor();
    private Border emptyBorder = UIData.getEmptyBorder();


    public CarrierJoinAuctionUI() {

        super();

        setTitle("CCN");
        setSize(width, height);
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

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


        JLabel auctionLabel = new JLabel("Auction name");
        auctionLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(12, 3, 0, 5);
        rootPanel.add(auctionLabel, constraints);

        auctionText = new JTextField();
        auctionText.setPreferredSize(new Dimension(100, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 5, 0, 3);
        rootPanel.add(auctionText, constraints);

        JLabel bidLabel = new JLabel("Price to bid");
        bidLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(12, 3, 0, 5);
        rootPanel.add(bidLabel, constraints);

        priceText = new JTextField();
        priceText.setPreferredSize(new Dimension(100, 22));

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
        constraints.insets = new java.awt.Insets(20, 3, 10, 3);
        rootPanel.add(bidBtn, constraints);

        activeAuctionsBtn = new JButton();
        activeAuctionsBtn.setText("<HTML><U>Active auctions</U></HTML>");
        activeAuctionsBtn.setFocusPainted(false);
        activeAuctionsBtn.setBorder(emptyBorder);
        activeAuctionsBtn.setBackground(background);
        activeAuctionsBtn.setFont(font.deriveFont(Font.PLAIN, 12));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 25, 0);
        rootPanel.add(activeAuctionsBtn, constraints);

        logoutBtn = new JButton();
        logoutBtn.setText("<HTML><U>Logout</U></HTML>");
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(emptyBorder);
        logoutBtn.setBackground(background);
        logoutBtn.setFont(font.deriveFont(Font.PLAIN, 12));
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

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().add(rootPanel, BorderLayout.CENTER);
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

    public void reset() {
        auctionText.setText("");
        priceText.setText("");
        errorLabel.setText("");
    }

}
