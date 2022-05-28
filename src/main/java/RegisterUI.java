import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class RegisterUI extends JFrame {

    private static JButton backBtn, registerBtn;
    private JTextField nameText, trText, usernameText;
    private JPasswordField passwordText;
    private JRadioButton carrierBtn, auctioneerBtn;
    private JLabel errorLabel,successLabel;

    private Color background = UIData.getBackground();
    private Font font = UIData.getFont();
    private Color errorColor = UIData.getErrorColor();
    private Color successColor = UIData.getSuccessColor();
    private Border emptyBorder = UIData.getEmptyBorder();

    public RegisterUI() {

        super();

        setTitle("CCN");
        setSize(480, 530);
        setMinimumSize(new Dimension(480, 530));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBackground(background);

        backBtn = new JButton();
        backBtn.setText("\u2190");
        backBtn.setFocusPainted(false);
        backBtn.setBorder(emptyBorder);
        backBtn.setBackground(background);
        backBtn.setFont(font.deriveFont(Font.BOLD, 30));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 20, 10, 0);
        bottomPanel.add(backBtn, constraints);

        JLabel emptyLabel = new JLabel("");
        emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(10, 3, 35, 3);
        rootPanel.add(emptyLabel, constraints);

        JLabel loginLabel = new JLabel("Registration");
        loginLabel.setFont(font.deriveFont(Font.BOLD, 15));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 3, 25, 3);
        rootPanel.add(loginLabel, constraints);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(12, 3, 8, 5);
        rootPanel.add(nameLabel, constraints);

        // Join name text field
        nameText = new JTextField();
        nameText.setPreferredSize(new Dimension(120, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 5, 6, 3);
        rootPanel.add(nameText, constraints);


        JLabel usernameLabel = new JLabel("Username");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(18, 3, 12, 5);
        rootPanel.add(usernameLabel, constraints);

        // Join name text field
        usernameText = new JTextField();
        usernameText.setPreferredSize(new Dimension(120, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(16, 5, 10, 3);
        rootPanel.add(usernameText, constraints);


        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(14, 3, 12, 5);
        rootPanel.add(passwordLabel, constraints);

        passwordText = new JPasswordField();
        passwordText.setPreferredSize(new Dimension(120, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(12, 5, 10, 3);
        rootPanel.add(passwordText, constraints);

        JLabel role = new JLabel("Role");
        role.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(14, 3, 8, 3);
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
        constraints.gridy = 5;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(11, 3, 5, 0);
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
        constraints.gridy = 6;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 3, 8, 3);
        rootPanel.add(auctioneerBtn, constraints);

        ButtonGroup group = new ButtonGroup();
        group.add(carrierBtn);
        group.add(auctioneerBtn);

        JLabel trLabel = new JLabel("Transport requests");
        trLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(16, 3, 16, 5);
        rootPanel.add(trLabel, constraints);


        trText = new JTextField();
        trText.setPreferredSize(new Dimension(260, 22));
        trText.setEditable(false);


        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(14, 5, 14, 0);
        rootPanel.add(trText, constraints);

        JButton infoBtn = new JButton();
        infoBtn.setText("\u24D8");
        infoBtn.setFocusPainted(false);

        infoBtn.setBorder(emptyBorder);
        infoBtn.setBackground(background);

        infoBtn.setFont(font.deriveFont(Font.BOLD, 14));
        infoBtn.setForeground(new Color(5, 170, 255, 255));
        infoBtn.setToolTipText("<HTML>Tranport requests are enclosed in <br>" +
                "\"&lt;&gt;\" and separated by commas. <br>Each request " +
                "is of the form<br>((x1,y1),(x2,y2)), where " +
                "(x1,y1) is<br>the coordinate of the pickup point<br>" +
                "and (x2,y2) is of the delivery point.");
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);


        constraints = new GridBagConstraints();
        constraints.gridx = 3;
        constraints.gridy = 7;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(16, 5, 5, 0);
        rootPanel.add(infoBtn, constraints);



        registerBtn = new JButton();
        registerBtn.setText("Register");
        registerBtn.setFocusPainted(false);


        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(25, 10, 10, 3);
        rootPanel.add(registerBtn, constraints);

        carrierBtn.addActionListener(e -> trText.setEditable(true));

        auctioneerBtn.addActionListener(e -> trText.setEditable(false));

        errorLabel = new JLabel();
        errorLabel.setForeground(errorColor);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setText("");

        successLabel = new JLabel();
        successLabel.setForeground(successColor);
        successLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // <HTML>Successfully registered. Return and log in with<br><center>the created username/password.</center></HTML>
        successLabel.setText("<HTML>Successfully registered. Return and log in with<br><center>the created username/password.</center></HTML>");
        successLabel.setVisible(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = java.awt.GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(15, 0, 0, 0);
        rootPanel.add(errorLabel, constraints);
        rootPanel.add(successLabel, constraints);


        getContentPane().add(rootPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().setBackground(background);

        pack();

        setResizable(false);
    }

    public static JButton getBackBtn() {
        return backBtn;
    }

    public static JButton getRegisterBtn() {
        return registerBtn;
    }

    public void reset() {
        nameText.setText("");
        usernameText.setText("");
        passwordText.setText("");
        carrierBtn.setSelected(false);
        auctioneerBtn.setSelected(false);
        trText.setEditable(false);
        trText.setText("");
        errorLabel.setText("");
        successLabel.setText("");
    }

    public String getNameText() {
        return nameText.getText().trim();
    }

    public String getUsernameText() {
        return usernameText.getText().trim();
    }

    public String getPasswordText() {
        return new String(passwordText.getPassword());
    }

    public boolean isAuctioneer() {
        return !carrierBtn.isSelected();
    }

    public boolean areAllFieldsFilled() {
        if (getNameText().equals("")) {
            return false;
        } else if (getUsernameText().equals("")) {
            return false;
        } else if (getPasswordText().equals("")) {
            return false;
        } else return carrierBtn.isSelected() || auctioneerBtn.isSelected();
        // TODO: Check transport requests field
    }

    public void setErrorLabel(String s) {
        errorLabel.setText(s);
    }

    public void showSuccessLabel() {
        successLabel.setVisible(true);
    }


}
