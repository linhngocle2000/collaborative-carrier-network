import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    private static JButton backBtn, loginBtn;
    private JTextField nameText;
    private JPasswordField passwordText;


    public LoginUI() {

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

        JLabel loginLabel = new JLabel("Login as");
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


        JLabel nameLabel = new JLabel("Username");
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(12, 3, 0, 5);
        rootPanel.add(nameLabel, constraints);

        // Join name text field
        nameText = new JTextField();
        nameText.setPreferredSize(new Dimension(100, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(10, 5, 0, 3);
        rootPanel.add(nameText, constraints);


        // Join auction name label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(22, 3, 5, 5);
        rootPanel.add(passwordLabel, constraints);

        // Join auction name text field
        passwordText = new JPasswordField();
        passwordText.setPreferredSize(new Dimension(100, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.insets = new java.awt.Insets(20, 5, 5, 3);
        rootPanel.add(passwordText, constraints);

        JPanel p = new JPanel();

        loginBtn = new JButton();
        loginBtn.setText("Login");
        loginBtn.setFocusPainted(false);

        backBtn = new JButton();
        backBtn.setText("Back");
        backBtn.setFocusPainted(false);



        backBtn.setPreferredSize(loginBtn.getPreferredSize());

        p.add(loginBtn);
        p.add(backBtn);
        p.setBackground(background);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(20, 10, 5, 3);
        rootPanel.add(p, constraints);

        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(new Color(1f, 0f, 0f));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 4;
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

    public static JButton getLoginBtn() {
        return loginBtn;
    }

    public void reset() {
        nameText.setText("");
        passwordText.setText("");
    }
}
