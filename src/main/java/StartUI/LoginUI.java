package StartUI;

import UIResource.UIData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class LoginUI extends JFrame {

    private JButton backBtn, loginBtn;
    private JTextField nameText;
    private JPasswordField passwordText;
    private JLabel errorLabel;

    private Color background = UIData.getBackground();
    private int width = UIData.getWidth();
    private int height = UIData.getHeight();
    private Font font = UIData.getFont();
    private Color errorColor = UIData.getErrorColor();
    private Border emptyBorder = UIData.getEmptyBorder();


    public LoginUI() {

        super();

///////////
// Frame
///////////

        setTitle("CCN");
        setSize(width, height);
        setMinimumSize(new Dimension(width, height));
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
// Input
///////////

        JLabel loginLabel = new JLabel("Login as");
        loginLabel.setFont(font.deriveFont(Font.BOLD, 16));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(20, 0, 35, 0);
        rootPanel.add(loginLabel, constraints);


        JLabel nameLabel = new JLabel("Username");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(12, 3, 0, 5);
        rootPanel.add(nameLabel, constraints);

        nameText = new JTextField();
        nameText.setPreferredSize(new Dimension(120, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 5, 0, 3);
        rootPanel.add(nameText, constraints);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(22, 3, 5, 5);
        rootPanel.add(passwordLabel, constraints);

        passwordText = new JPasswordField();
        passwordText.setPreferredSize(new Dimension(120, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(20, 5, 5, 3);
        rootPanel.add(passwordText, constraints);

///////////
// Button
///////////

        loginBtn = new JButton();
        loginBtn.setText("Login");
        loginBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(30, 0, 5, 0);
        rootPanel.add(loginBtn, constraints);

///////////
// Log
///////////

        errorLabel = new JLabel();
        errorLabel.setForeground(errorColor);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //Incorrect username/password.
        errorLabel.setText("");

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = java.awt.GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(15, 3, 0, 3);
        rootPanel.add(errorLabel, constraints);

///////////
// Botttom
///////////

        backBtn = new JButton();
        backBtn.setText("\u2190");
        backBtn.setFocusPainted(false);
        backBtn.setBorder(emptyBorder);
        backBtn.setBackground(background);
        backBtn.setFont(font.deriveFont(Font.BOLD, 25));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(0, 20, 10, 0);
        bottomPanel.add(backBtn, constraints);

///////////
// Combine
///////////


        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        getContentPane().add(rootPanel, BorderLayout.CENTER);
        getContentPane().setBackground(background);

        pack();

        setResizable(false);
    }

    public JButton getBackBtn() {
        return backBtn;
    }

    public JButton getLoginBtn() {
        return loginBtn;
    }

    public String getNameText() {
        return nameText.getText().trim();
    }

    public String getPasswordText() {
        return new String(passwordText.getPassword());
    }

    public void setErrorLabel(String s) {
        errorLabel.setText(s);
    }

    public void reset() {
        nameText.setText("");
        passwordText.setText("");
        errorLabel.setText("");
    }
}
