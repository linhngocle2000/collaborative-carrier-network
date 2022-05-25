import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class AuctioneerUI extends JFrame {
    private static JButton startBtn, listOfAuctionsBtn, logoutBtn;
    private JTextField auctionText, requestText;

    public AuctioneerUI() {

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

        JLabel loginLabel = new JLabel("Log in as: ");
        Font font = loginLabel.getFont();
        loginLabel.setFont(font.deriveFont(Font.BOLD, 14));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 18, 0);
        rootPanel.add(loginLabel, constraints);

        listOfAuctionsBtn = new JButton();
        listOfAuctionsBtn.setText("<HTML><U>Auction requests</U></HTML>");
        listOfAuctionsBtn.setFocusPainted(false);
        Border emptyBorder = BorderFactory.createEmptyBorder();
        listOfAuctionsBtn.setBorder(emptyBorder);
        listOfAuctionsBtn.setBackground(background);
        Font auctionfont = listOfAuctionsBtn.getFont();
        listOfAuctionsBtn.setFont(auctionfont.deriveFont(Font.PLAIN, 12));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 20, 0);
        rootPanel.add(listOfAuctionsBtn, constraints);

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
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 5, 0, 3);
        rootPanel.add(auctionText, constraints);

        JLabel idLabel = new JLabel("Request ID");
        idLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(12, 3, 0, 5);
        rootPanel.add(idLabel, constraints);

        requestText = new JTextField();
        requestText.setPreferredSize(new Dimension(100, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 5, 5, 3);
        rootPanel.add(requestText, constraints);

        startBtn = new JButton();
        startBtn.setText("Start");
        startBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(20, 3, 0, 3);
        rootPanel.add(startBtn, constraints);

        logoutBtn = new JButton();
        logoutBtn.setText("<HTML><U>Logout</U></HTML>");
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(emptyBorder);
        logoutBtn.setBackground(background);
        Font btnfont = logoutBtn.getFont();
        logoutBtn.setFont(btnfont.deriveFont(Font.PLAIN, 12));
        logoutBtn.setForeground(Color.BLUE);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(10, 0, 10, 0);
        rootPanel.add(logoutBtn, constraints);

        getContentPane().add(rootPanel);
        getContentPane().setBackground(background);

        pack();

        setResizable(false);
    }

    public static JButton getLogoutBtn() {
        return logoutBtn;
    }

    public void reset() {
        auctionText.setText("");
    }
}
