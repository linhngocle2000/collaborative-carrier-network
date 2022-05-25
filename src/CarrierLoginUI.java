import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CarrierLoginUI extends JFrame {

    private static JButton joinAuctionBtn, administrationBtn, logoutBtn;

    public CarrierLoginUI() {

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
        constraints.insets = new java.awt.Insets(0, 0, 20, 0);
        rootPanel.add(loginLabel, constraints);

        joinAuctionBtn = new JButton();
        joinAuctionBtn.setText("Join Auction");
        joinAuctionBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(10, 0, 10, 0);
        rootPanel.add(joinAuctionBtn, constraints);

        administrationBtn = new JButton();
        administrationBtn.setText("Administration");
        administrationBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(10, 0, 10, 0);
        rootPanel.add(administrationBtn, constraints);

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
        constraints.gridy = 3;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(10, 0, 10, 0);
        rootPanel.add(logoutBtn, constraints);

        getContentPane().add(rootPanel);
        getContentPane().setBackground(background);

        pack();

        setResizable(false);
    }

    public static JButton getAdministrationBtn() {
        return administrationBtn;
    }

    public static JButton getJoinAuctionBtn() {
        return joinAuctionBtn;
    }

    public static JButton getLogoutBtn() {
        return logoutBtn;
    }


}
