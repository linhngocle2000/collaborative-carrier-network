package StartUI;

import UIResource.UIData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class RegisterUI extends JFrame {

    private JButton backBtn, registerBtn;
    private JTextField nameText, trText, usernameText, baseRateAText, basePriceText, baseRateBText, baseInRateText, depotLonText, depotLatText;
    private JPasswordField passwordText;
    private JLabel errorLabel,successLabel;

    private Color background = UIData.getBackground();
    private Font font = UIData.getFont();
    private Color errorColor = UIData.getErrorColor();
    private Color successColor = UIData.getSuccessColor();
    private Border emptyBorder = UIData.getEmptyBorder();

    public RegisterUI() {

        super();

///////////
// Frame
///////////

        setTitle("CCN");
        setSize(500, 650);
        setMinimumSize(new Dimension(500, 650));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

///////////
// Panels
///////////

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBackground(background);

///////////
// Basic
///////////

        JLabel loginLabel = new JLabel("Registration");
        loginLabel.setFont(font.deriveFont(Font.BOLD, 16));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(20, 0, 15, 0);
        rootPanel.add(loginLabel, constraints);


        JLabel nameLabel = new JLabel("Agent name");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 0, 0, 0);
        rootPanel.add(namePanel, constraints);

        // Join name text field
        nameText = new JTextField();
        nameText.setPreferredSize(new Dimension(335, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 5, 0, 0);
        rootPanel.add(nameText, constraints);

        JLabel usernameLabel = new JLabel("Username");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JPanel usernamePanel = new JPanel();
        usernamePanel.add(usernameLabel);
        usernamePanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 0, 0, 0);
        rootPanel.add(usernamePanel, constraints);

        usernameText = new JTextField();
        usernameText.setPreferredSize(new Dimension(160, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 5, 0, 0);
        rootPanel.add(usernameText, constraints);


        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);
        JPanel passwordPanel = new JPanel();
        passwordPanel.add(passwordLabel);
        passwordPanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 10, 0, 0);
        rootPanel.add(passwordPanel, constraints);

        passwordText = new JPasswordField();
        passwordText.setPreferredSize(new Dimension(160, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 4;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 15, 0, 0);
        rootPanel.add(passwordText, constraints);

///////////
// Carrier
///////////

        JLabel trLabel = new JLabel("Transport requests");
        trLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton trInfoBtn = new JButton();
        trInfoBtn.setText("\u24D8");
        trInfoBtn.setFocusPainted(false);
        trInfoBtn.setBorder(emptyBorder);
        trInfoBtn.setBackground(background);
        trInfoBtn.setFont(font.deriveFont(Font.BOLD, 14));
        trInfoBtn.setForeground(new Color(5, 170, 255, 255));
        trInfoBtn.setToolTipText("<HTML>Tranport requests are enclosed in<br>" +
                "\"&lt;&gt;\" and separated by commas.<br>Each request " +
                "is of the form<br>((x1,y1),(x2,y2)), where " +
                "(x1,y1) is<br>the coordinate of the pickup point<br>" +
                "and (x2,y2) is of the delivery point.");
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

        JPanel trPanel = new JPanel();
        trPanel.add(trLabel);
        trPanel.add(trInfoBtn);
        trPanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 0, 0, 0);
        rootPanel.add(trPanel, constraints);

        trText = new JTextField();
        trText.setPreferredSize(new Dimension(335, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 5, 0, 0);
        rootPanel.add(trText, constraints);

        JLabel baseRateALabel = new JLabel("Base rate A (\u20AC)");
        baseRateALabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton rateAInfoBtn = new JButton();
        rateAInfoBtn.setText("\u24D8");
        rateAInfoBtn.setFocusPainted(false);
        rateAInfoBtn.setBorder(emptyBorder);
        rateAInfoBtn.setBackground(background);
        rateAInfoBtn.setFont(font.deriveFont(Font.BOLD, 14));
        rateAInfoBtn.setForeground(new Color(5, 170, 255, 255));
        rateAInfoBtn.setToolTipText("<HTML>Base rate to reach<br>the pickup point</HTML>");

        JPanel rateAPanel = new JPanel();
        rateAPanel.add(baseRateALabel);
        rateAPanel.add(rateAInfoBtn);
        rateAPanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 0, 0, 0);
        rootPanel.add(rateAPanel, constraints);

        baseRateAText = new JTextField();
        baseRateAText.setPreferredSize(new Dimension(160, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 5, 0, 0);
        rootPanel.add(baseRateAText, constraints);

        JLabel basePriceLabel = new JLabel("Price (\u20AC/km)");
        basePriceLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel pricePanel = new JPanel();
        pricePanel.add(basePriceLabel);
        pricePanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 0, 0, 0);
        rootPanel.add(pricePanel, constraints);

        basePriceText = new JTextField();
        basePriceText.setPreferredSize(new Dimension(160, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 5, 0, 0);
        rootPanel.add(basePriceText, constraints);

        JLabel baseRateBLabel = new JLabel("Base rate B (\u20AC)");
        baseRateBLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton rateBInfoBtn = new JButton();
        rateBInfoBtn.setText("\u24D8");
        rateBInfoBtn.setFocusPainted(false);
        rateBInfoBtn.setBorder(emptyBorder);
        rateBInfoBtn.setBackground(background);
        rateBInfoBtn.setFont(font.deriveFont(Font.BOLD, 14));
        rateBInfoBtn.setForeground(new Color(5, 170, 255, 255));
        rateBInfoBtn.setToolTipText("<HTML>Base rate for (un-)loading</HTML>");

        JPanel rateBPanel = new JPanel();
        rateBPanel.add(baseRateBLabel);
        rateBPanel.add(rateBInfoBtn);
        rateBPanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 10, 0, 0);
        rootPanel.add(rateBPanel, constraints);

        baseRateBText = new JTextField();
        baseRateBText.setPreferredSize(new Dimension(160, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 8;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 15, 0, 0);
        rootPanel.add(baseRateBText, constraints);

        JLabel baseInRateLabel = new JLabel("Internal rate (\u20AC/km)");
        baseInRateLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel internalPanel = new JPanel();
        internalPanel.add(baseInRateLabel);
        internalPanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 9;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 10, 0, 0);
        rootPanel.add(internalPanel, constraints);

        baseInRateText = new JTextField();
        baseInRateText.setPreferredSize(new Dimension(160, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 10;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 15, 0, 0);
        rootPanel.add(baseInRateText, constraints);

        JLabel depotLatLabel = new JLabel("Depot latitude");
        depotLatLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel depotLatPanel = new JPanel();
        depotLatPanel.add(depotLatLabel);
        depotLatPanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 11;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 0, 0, 0);
        rootPanel.add(depotLatPanel, constraints);

        depotLatText = new JTextField();
        depotLatText.setPreferredSize(new Dimension(160, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 12;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 5, 0, 0);
        rootPanel.add(depotLatText, constraints);

        JLabel depotLonLabel = new JLabel("Depot longitude");
        depotLonLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel depotLonPanel = new JPanel();
        depotLonPanel.add(depotLonLabel);
        depotLonPanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 11;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 10, 0, 0);
        rootPanel.add(depotLonPanel, constraints);

        depotLonText = new JTextField();
        depotLonText.setPreferredSize(new Dimension(160, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 12;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 15, 0, 0);
        rootPanel.add(depotLonText, constraints);

///////////
// Button
///////////

        registerBtn = new JButton();
        registerBtn.setText("Register");
        registerBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 13;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(25, 5, 0, 10);
        rootPanel.add(registerBtn, constraints);

///////////
// Log
///////////

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
        constraints.gridy = 14;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = java.awt.GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(15, 0, 0, 0);
        rootPanel.add(errorLabel, constraints);
        rootPanel.add(successLabel, constraints);

///////////
// Bottom
///////////

        backBtn = new JButton();
        backBtn.setText("\u2190");
        backBtn.setFocusPainted(false);
        backBtn.setBorder(emptyBorder);
        backBtn.setBackground(background);
        backBtn.setFont(font.deriveFont(Font.BOLD, 30));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 20, 10, 0);
        bottomPanel.add(backBtn, constraints);

///////////
// Combine
///////////

        getContentPane().add(rootPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().setBackground(background);

        pack();

        setResizable(false);
    }

    public JButton getBackBtn() {
        return backBtn;
    }

    public JButton getRegisterBtn() {
        return registerBtn;
    }

    public void deactivate() {
        nameText.setEditable(false);
        usernameText.setEditable(false);
        passwordText.setEditable(false);
        trText.setEditable(false);
        baseRateBText.setEditable(false);
        baseRateAText.setEditable(false);
        baseInRateText.setEditable(false);
        basePriceText.setEditable(false);
        depotLonText.setEditable(false);
        depotLatText.setEditable(false);
        registerBtn.setEnabled(false);
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

    public String getTrText() {
        return trText.getText().trim().replaceAll(" ","");
    }

    public float getBaseRateAText() {
        String s = baseRateAText.getText().trim();
        return Float.parseFloat(s);
    }

    public float getBaseRateBText() {
        String s = baseRateBText.getText().trim();
        return Float.parseFloat(s);
    }

    public float getBasePriceText() {
        String s = basePriceText.getText().trim();
        return Float.parseFloat(s);
    }

    public float getBaseInRateText() {
        String s = baseInRateText.getText().trim();
        return Float.parseFloat(s);
    }

    public float getDepotLatText() {
        String s = depotLatText.getText().trim();
        return Float.parseFloat(s);
    }

    public float getDepotLonText() {
        String s = depotLonText.getText().trim();
        return Float.parseFloat(s);
    }


    public boolean areAllFieldsFilled() {
        if (getNameText().equals("")) {
            return false;
        } else if (getUsernameText().equals("")) {
            return false;
        } else if (getPasswordText().equals("")) {
            return false;
        } else {
            return !trText.getText().trim().equals("") &&
                            !baseRateAText.getText().trim().equals("") &&
                            !baseRateBText.getText().trim().equals("") &&
                            !baseInRateText.getText().trim().equals("") &&
                            !basePriceText.getText().trim().equals("") &&
                            !depotLatText.getText().trim().equals("") &&
                            !depotLonText.getText().trim().equals("");
        }
    }

    public void setErrorLabel(String s) {
        errorLabel.setText(s);
    }

    public boolean verifyTRInput() {
        String text = getTrText();
        return text.matches("^<\\(\\((-?)(0|([1-9][0-9]*))(\\.[0-9]+)?,(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?\\),\\((-?)(0|([1-9][0-9]*))(\\.[0-9]+)?,(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?\\)\\)(,\\(\\((-?)(0|([1-9][0-9]*))(\\.[0-9]+)?,(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?\\),\\((-?)(0|([1-9][0-9]*))(\\.[0-9]+)?,(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?\\)\\))*?>$");
    }

    public boolean verifyPriceInput() {
        String regex = "^[1-9][0-9]*?(\\.[0-9][0-9]?)?$";
        String price1 = baseRateAText.getText().trim();
        String price2 = baseRateBText.getText().trim();
        String price3 = baseInRateText.getText().trim();
        String price4 = basePriceText.getText().trim();
        return price1.matches(regex) &&
                price2.matches(regex) &&
                price3.matches(regex) &&
                price4.matches(regex);
    }

    public boolean verifyDepotInput() {
        String regex = "^-?[1-9][0-9]*?(\\.[0-9]+?)?$";
        String depotX = depotLatText.getText().trim();
        String depotY = depotLonText.getText().trim();
        return depotX.matches(regex) && depotY.matches(regex);
    }

    public void showSuccessLabel() {
        successLabel.setVisible(true);
    }
}
