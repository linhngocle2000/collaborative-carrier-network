package CarrierUI;

import UIResource.UIData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CarrierLoginUI extends JFrame {

    private JButton joinAuctionBtn, administrationBtn, logoutBtn;
    private JLabel nameLabel;

    private Color background = UIData.getBackground();
    private int width = UIData.getWidth();
    private int height = UIData.getHeight();
    private Font font = UIData.getFont();
    private Border emptyBorder = UIData.getEmptyBorder();

    public CarrierLoginUI() {

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

        joinAuctionBtn = new JButton();
        joinAuctionBtn.setText("Join Auction");
        joinAuctionBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(20, 0, 20, 0);
        rootPanel.add(joinAuctionBtn, constraints);

        administrationBtn = new JButton();
        administrationBtn.setText("Administration");
        administrationBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(20, 0, 20, 0);
        rootPanel.add(administrationBtn, constraints);

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

        getContentPane().add(rootPanel, BorderLayout.CENTER);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().setBackground(background);

        pack();

        setResizable(false);
    }

    public JButton getAdministrationBtn() {
        return administrationBtn;
    }

    public JButton getJoinAuctionBtn() {
        return joinAuctionBtn;
    }

    public JButton getLogoutBtn() {
        return logoutBtn;
    }

    public void setNameLabel(String s) {
        nameLabel.setText(s);
    }


}
