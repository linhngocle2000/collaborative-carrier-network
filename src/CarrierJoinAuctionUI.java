import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CarrierJoinAuctionUI extends JFrame {

    private static JButton bidBtn, activeAuctionsBtn, logoutBtn;
    private JTextField auctionText, priceText;


    public CarrierJoinAuctionUI() {

        super();

        Color background = new Color(1f, 1f, 1f);

        setTitle("CCN");
        setSize(250, 320);
        setMinimumSize(new Dimension(250, 320));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);

        JLabel loginLabel = new JLabel("Login as: ");
        Font font = loginLabel.getFont();
        loginLabel.setFont(font.deriveFont(Font.BOLD, 14));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 3, 15, 3);
        rootPanel.add(loginLabel, constraints);


        JLabel auctionLabel = new JLabel("Auction");
        auctionLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(12, 3, 0, 5);
        rootPanel.add(auctionLabel, constraints);

        auctionText = new JTextField();
        auctionText.setPreferredSize(new Dimension(100, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 5, 0, 3);
        rootPanel.add(auctionText, constraints);

        JLabel bidLabel = new JLabel("Price");
        bidLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(22, 3, 5, 5);
        rootPanel.add(bidLabel, constraints);

        priceText = new JTextField();
        priceText.setPreferredSize(new Dimension(50, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(20, 5, 5, 5);
        rootPanel.add(priceText, constraints);

        bidBtn = new JButton();
        bidBtn.setText("Bid");
        bidBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(20, 5, 0, 0);
        rootPanel.add(bidBtn, constraints);

        activeAuctionsBtn = new JButton();
        activeAuctionsBtn.setText("Active Auctions");
        activeAuctionsBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(20, 3, 0, 3);
        rootPanel.add(activeAuctionsBtn, constraints);

        logoutBtn = new JButton();
        logoutBtn.setText("<HTML><U>Logout</U></HTML>");
        logoutBtn.setFocusPainted(false);
        Border emptyBorder = BorderFactory.createEmptyBorder();
        logoutBtn.setBorder(emptyBorder);
        logoutBtn.setBackground(background);
        Font btnfont = logoutBtn.getFont();
        logoutBtn.setFont(btnfont.deriveFont(Font.PLAIN, 12));
        logoutBtn.setForeground(Color.BLUE);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(15, 3, 0, 3);
        rootPanel.add(logoutBtn, constraints);

        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(new Color(1f, 0f, 0f));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = java.awt.GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(15, 3, 10, 3);
        rootPanel.add(errorLabel, constraints);

        errorLabel.setText("");

        getContentPane().add(rootPanel, BorderLayout.CENTER);
        getContentPane().setBackground(background);

        pack();

        setResizable(false);
    }

    public static JButton getLogoutBtn() {
        return logoutBtn;
    }

    public void reset() {
        auctionText.setText("");
        priceText.setText("");
    }

}
