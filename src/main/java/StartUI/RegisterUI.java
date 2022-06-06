package StartUI;

import UIResource.UIData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class RegisterUI extends JFrame {

    private static JButton backBtn, registerBtn;
    private JTextField nameText, trText, usernameText, baseRateAText, baseRateBText, baseInRateText;
    private JPasswordField passwordText;
    private JLabel errorLabel,successLabel;
    private JComboBox<String> roleOptions;

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
        loginLabel.setFont(font.deriveFont(Font.BOLD, 16));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 15, 0);
        rootPanel.add(loginLabel, constraints);


        JLabel nameLabel = new JLabel("Name");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel namePanel = new JPanel();
        namePanel.add(nameLabel);
        namePanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 10, 0, 0);
        rootPanel.add(namePanel, constraints);

        // Join name text field
        nameText = new JTextField();
        nameText.setPreferredSize(new Dimension(160, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 15, 0, 3);
        rootPanel.add(nameText, constraints);


        JLabel usernameLabel = new JLabel("Username");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel usernamePanel = new JPanel();
        usernamePanel.add(usernameLabel);
        usernamePanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 0, 0, 0);
        rootPanel.add(usernamePanel, constraints);

        // Join name text field
        usernameText = new JTextField();
        usernameText.setPreferredSize(new Dimension(160, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 5;
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
        constraints.gridy = 4;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 10, 0, 0);
        rootPanel.add(passwordPanel, constraints);

        passwordText = new JPasswordField();
        passwordText.setPreferredSize(new Dimension(160, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 15, 0, 0);
        rootPanel.add(passwordText, constraints);

        JLabel role = new JLabel("Role");
        role.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel rolePanel = new JPanel();
        rolePanel.add(role);
        rolePanel.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 0, 0, 0);
        rootPanel.add(rolePanel, constraints);

        String[] optionsToChoose = {"Auctioneer", "Carrier"};
        roleOptions = new JComboBox<>(optionsToChoose);
        roleOptions.setPreferredSize(new Dimension(90,22));
        roleOptions.setBackground(background);
        roleOptions.addItemListener(e -> {
            if (roleOptions.getSelectedIndex()==1) {
                trText.setEditable(true);
                baseRateAText.setEditable(true);
                baseRateBText.setEditable(true);
                baseInRateText.setEditable(true);
            } else {
                trText.setEditable(false);
                baseRateAText.setEditable(false);
                baseRateBText.setEditable(false);
                baseInRateText.setEditable(false);
            }
        });

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 5, 0, 0);
        rootPanel.add(roleOptions, constraints);

        JLabel trLabel = new JLabel("Transport requests");
        trLabel.setHorizontalAlignment(SwingConstants.LEFT);

        trText = new JTextField();
        trText.setPreferredSize(new Dimension(335, 22));
        trText.setEditable(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 5, 0, 0);
        rootPanel.add(trText, constraints);

        JButton infoBtn = new JButton();
        infoBtn.setText("\u24D8");
        infoBtn.setFocusPainted(false);

        infoBtn.setBorder(emptyBorder);
        infoBtn.setBackground(background);

        infoBtn.setFont(font.deriveFont(Font.BOLD, 14));
        infoBtn.setForeground(new Color(5, 170, 255, 255));
        infoBtn.setToolTipText("<HTML>Tranport requests are enclosed in<br>" +
                "\"&lt;&gt;\" and separated by commas.<br>Each request " +
                "is of the form<br>((x1,y1),(x2,y2)), where " +
                "(x1,y1) is<br>the coordinate of the pickup point<br>" +
                "and (x2,y2) is of the delivery point.");
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

        JPanel panel1 = new JPanel();
        panel1.add(trLabel);
        panel1.add(infoBtn);
        panel1.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 0, 0, 0);
        rootPanel.add(panel1, constraints);

        JLabel baseRateALabel = new JLabel("Base rate A (\u20AC)");
        baseRateALabel.setHorizontalAlignment(SwingConstants.LEFT);

        baseRateAText = new JTextField();
        baseRateAText.setPreferredSize(new Dimension(105, 22));
        baseRateAText.setEditable(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 5, 0, 0);
        rootPanel.add(baseRateAText, constraints);

        JButton infoBtn2 = new JButton();
        infoBtn2.setText("\u24D8");
        infoBtn2.setFocusPainted(false);

        infoBtn2.setBorder(emptyBorder);
        infoBtn2.setBackground(background);

        infoBtn2.setFont(font.deriveFont(Font.BOLD, 14));
        infoBtn2.setForeground(new Color(5, 170, 255, 255));
        infoBtn2.setToolTipText("<HTML>Base rate to reach<br>the pickup point");
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

        JPanel panel2 = new JPanel();
        panel2.add(baseRateALabel);
        panel2.add(infoBtn2);
        panel2.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 0, 0, 0);
        rootPanel.add(panel2, constraints);

        JLabel baseRateBLabel = new JLabel("Base rate B (\u20AC)");
        baseRateBLabel.setHorizontalAlignment(SwingConstants.LEFT);

        baseRateBText = new JTextField();
        baseRateBText.setPreferredSize(new Dimension(105, 22));
        baseRateBText.setEditable(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 120, 0, 0);
        rootPanel.add(baseRateBText, constraints);

        JButton infoBtn3 = new JButton();
        infoBtn3.setText("\u24D8");
        infoBtn3.setFocusPainted(false);

        infoBtn3.setBorder(emptyBorder);
        infoBtn3.setBackground(background);

        infoBtn3.setFont(font.deriveFont(Font.BOLD, 14));
        infoBtn3.setForeground(new Color(5, 170, 255, 255));
        infoBtn3.setToolTipText("<HTML>Base rate for (un-)loading");
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

        JPanel panel3 = new JPanel();
        panel3.add(baseRateBLabel);
        panel3.add(infoBtn3);
        panel3.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 115, 0, 0);
        rootPanel.add(panel3, constraints);

        JLabel baseInRateLabel = new JLabel("Internal rate (\u20AC/km)");
        baseInRateLabel.setHorizontalAlignment(SwingConstants.LEFT);

        baseInRateText = new JTextField();
        baseInRateText.setPreferredSize(new Dimension(105, 22));
        baseInRateText.setEditable(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 235, 0, 0);
        rootPanel.add(baseInRateText, constraints);

        JPanel panel4 = new JPanel();
        panel4.add(baseInRateLabel);
        panel4.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.gridwidth = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(15, 230, 0, 0);
        rootPanel.add(panel4, constraints);

        registerBtn = new JButton();
        registerBtn.setText("Register");
        registerBtn.setFocusPainted(false);


        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 11;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(20, 5, 0, 10);
        rootPanel.add(registerBtn, constraints);

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
        constraints.gridy = 10;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = java.awt.GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(20, 0, 0, 0);
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

    public void deactivate() {
        roleOptions.setEnabled(false);
        nameText.setEditable(false);
        usernameText.setEditable(false);
        passwordText.setEditable(false);
        trText.setEditable(false);
        baseRateBText.setEditable(false);
        baseRateAText.setEditable(false);
        baseInRateText.setEditable(false);
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

    public boolean isAuctioneer() {
        return roleOptions.getSelectedIndex()==0;
    }

    public boolean areAllFieldsFilled() {
        if (getNameText().equals("")) {
            return false;
        } else if (getUsernameText().equals("")) {
            return false;
        } else if (getPasswordText().equals("")) {
            return false;
        } else {
            return (roleOptions.getSelectedIndex() == 0) ||
                    !trText.getText().trim().equals("") &&
                            !baseRateAText.getText().trim().equals("") &&
                            !baseRateBText.getText().trim().equals("") &&
                            !baseInRateText.getText().trim().equals("");
        }
    }

    public void setErrorLabel(String s) {
        errorLabel.setText(s);
    }

    public boolean verifyTRInput() {
        String text = trText.getText().trim();
        return text.matches("^<\\(\\((-?)(0|([1-9][0-9]*))(\\.[0-9]+)?,(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?\\),\\((-?)(0|([1-9][0-9]*))(\\.[0-9]+)?,(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?\\)\\)(,\\(\\((-?)(0|([1-9][0-9]*))(\\.[0-9]+)?,(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?\\),\\((-?)(0|([1-9][0-9]*))(\\.[0-9]+)?,(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?\\)\\))*?>$");
    }

    public boolean verifyPriceInput() {
        String regex = "^[1-9][0-9]*?(\\.[0-9][0-9]?)?$";
        String price1 = baseRateAText.getText().trim();
        String price2 = baseRateBText.getText().trim();
        String price3 = baseInRateText.getText().trim();
        return price1.matches(regex) &&
                price2.matches(regex) &&
                price3.matches(regex);
    }

    public void showSuccessLabel() {
        successLabel.setVisible(true);
    }



}
