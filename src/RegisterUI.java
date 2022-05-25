import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RegisterUI extends JFrame {

    private static JButton backBtn, registerBtn;
    private JTextField nameText, trText;
    private JPasswordField passwordText;
    private JRadioButton carrierBtn, auctioneerBtn;

    public RegisterUI() {

        super();

        Color background = new Color(1f, 1f, 1f);

        setTitle("CCN");
        setSize(480, 530);
        setMinimumSize(new Dimension(480, 530));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);

        JLabel loginLabel = new JLabel("Registration");
        Font font = loginLabel.getFont();
        loginLabel.setFont(font.deriveFont(Font.BOLD, 15));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 3, 35, 3);
        rootPanel.add(loginLabel, constraints);


        JLabel nameLabel = new JLabel("Username");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(12, 3, 10, 5);
        rootPanel.add(nameLabel, constraints);

        // Join name text field
        nameText = new JTextField();
        nameText.setPreferredSize(new Dimension(100, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 5, 10, 3);
        rootPanel.add(nameText, constraints);


        // Join auction name label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(18, 3, 15, 5);
        rootPanel.add(passwordLabel, constraints);

        // Join auction name text field
        passwordText = new JPasswordField();
        passwordText.setPreferredSize(new Dimension(100, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(16, 5, 15, 3);
        rootPanel.add(passwordText, constraints);

        JLabel role = new JLabel("Role");
        role.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(16, 3, 10, 3);
        rootPanel.add(role, constraints);

        carrierBtn = new JRadioButton("Carrier");
        carrierBtn.setActionCommand("Carrier");
        carrierBtn.setSelected(false);
        carrierBtn.setFocusPainted(false);
        carrierBtn.setMinimumSize(new Dimension(70, 20));
        carrierBtn.setPreferredSize(new Dimension(70, 20));
        carrierBtn.setHorizontalAlignment(SwingConstants.LEFT);
        carrierBtn.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(13, 3, 10, 0);
        rootPanel.add(carrierBtn, constraints);

        auctioneerBtn = new JRadioButton("Auctioneer");
        auctioneerBtn.setActionCommand("Auctioneer");
        auctioneerBtn.setSelected(false);
        auctioneerBtn.setFocusPainted(false);
        auctioneerBtn.setHorizontalAlignment(SwingConstants.LEFT);
        auctioneerBtn.setMinimumSize(new Dimension(90, 20));
        auctioneerBtn.setPreferredSize(new Dimension(90, 20));
        auctioneerBtn.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(2, 3, 10, 3);
        rootPanel.add(auctioneerBtn, constraints);

        ButtonGroup group = new ButtonGroup();
        group.add(carrierBtn);
        group.add(auctioneerBtn);

        JLabel trLabel = new JLabel("Transport requests");
        trLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(18, 3, 15, 5);
        rootPanel.add(trLabel, constraints);


        trText = new JTextField();
        trText.setPreferredSize(new Dimension(260, 22));
        //trText.setText("<((pickup_x,pickup_y),(delivery_x,delivery_y)),...>");
        //trText.setForeground(Color.GRAY);
        trText.setEditable(false);

        trText.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (trText.getText().equals("<((pickup_x,pickup_y),(delivery_x,delivery_y)),...>") && trText.isEditable()) { // User has not entered text yet
                    trText.setText("");
                    trText.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (trText.getText().equals("") && trText.isEditable()) { // User did not enter text
                    trText.setText("<((pickup_x,pickup_y),(delivery_x,delivery_y)),...>");
                    trText.setForeground(Color.GRAY);
                }
            }
        });

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(16, 5, 15, 3);
        rootPanel.add(trText, constraints);

        JPanel p = new JPanel();

        registerBtn = new JButton();
        registerBtn.setText("Register");
        registerBtn.setFocusPainted(false);

        backBtn = new JButton();
        backBtn.setText("Back");
        backBtn.setFocusPainted(false);

        backBtn.setPreferredSize(registerBtn.getPreferredSize());

        p.add(registerBtn);
        p.add(backBtn);
        p.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(18, 10, 10, 3);
        rootPanel.add(p, constraints);

        carrierBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                trText.setEditable(true);
                if (trText.getText().equals("")) {
                    trText.setText("<((pickup_x,pickup_y),(delivery_x,delivery_y)),...>");
                    trText.setForeground(Color.GRAY);
                }

            }
        });

        auctioneerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                trText.setEditable(false);
                if (trText.getText().equals("<((pickup_x,pickup_y),(delivery_x,delivery_y)),...>")) {
                    trText.setText("");
                }
            }
        });

        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(new Color(1f, 0f, 0f));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 7;
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

    public static JButton getBackBtn() {
        return backBtn;
    }

    public void reset() {
        nameText.setText("");
        passwordText.setText("");
        carrierBtn.setSelected(false);
        auctioneerBtn.setSelected(false);
        //trText.setText("<((pickup_x,pickup_y),(delivery_x,delivery_y)),...>");
        //trText.setForeground(Color.GRAY);
        trText.setEditable(false);
    }


}
