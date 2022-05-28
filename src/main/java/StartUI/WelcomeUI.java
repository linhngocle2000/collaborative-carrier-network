package StartUI;

import UIResource.UIData;

import javax.swing.*;
import java.awt.*;


public class WelcomeUI extends JFrame {

    private static JButton loginBtn;
    private static JButton registerBtn;

    private Color background = UIData.getBackground();
    private int width = UIData.getWidth();
    private int height = UIData.getHeight();
    private Font font = UIData.getFont();

    public WelcomeUI() {

        super();

        setTitle("CCN");
        setSize(width, height);
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);

        JLabel titleLabel = new JLabel("<HTML>Collaborative Carrier<br><center>Network</center></HTML>");
        titleLabel.setFont(font.deriveFont(Font.BOLD, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(5, 0, 15, 0);
        rootPanel.add(titleLabel , constraints);

        registerBtn = new JButton();
        registerBtn.setText("Register");
        registerBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(20, 0, 10, 0);
        rootPanel.add(registerBtn, constraints);

        JLabel orLabel = new JLabel("or");
        orLabel.setFont(font.deriveFont(Font.BOLD, 14));
        orLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(10, 0, 10, 0);
        rootPanel.add(orLabel, constraints);

        loginBtn = new JButton();
        loginBtn.setText("Login");
        loginBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(10, 0, 15, 0);
        rootPanel.add(loginBtn, constraints);

        getContentPane().add(rootPanel);
        getContentPane().setBackground(background);

        pack();

        setResizable(false);
    }


    public static JButton getRegisterBtn() {
        return registerBtn;
    }

    public static JButton getLoginBtn() {
        return loginBtn;
    }

}
