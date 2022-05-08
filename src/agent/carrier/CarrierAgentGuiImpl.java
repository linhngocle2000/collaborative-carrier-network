package agent.carrier;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


/**
 J2SE (Swing-based) implementation of the GUI of the agent that wants to join/exit an auction
 */
public class CarrierAgentGuiImpl extends JFrame implements CarrierAgentGui{

    private CarrierAgent myAgent;

    // Text field to enter auction name
    private JTextField titleTF;

    // Radio buttons to choose "Join"/"Exit" auction
    private JRadioButton joinButton, exitButton;

    // Buttons: Send, Reset, Exit
    private JButton sendB, resetB, exitB;

    // Print logs
    private JTextArea logTA;

    public CarrierAgentGuiImpl() {
        super();

        // Terminate agent when GUI window is closed
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myAgent.doDelete();
            }
        } );

        // Set size of the upper part of GUI (everything above logTA)
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setMinimumSize(new Dimension(330, 100));
        rootPanel.setPreferredSize(new Dimension(330, 100));

///////////
// Line 0
///////////

        // Set position of title "Auction name" in the GUI
        JLabel l = new JLabel("Auction name:");
        l.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 3);
        rootPanel.add(l, gridBagConstraints);

        // Set position of text field to enter auction name in the GUI
        titleTF = new JTextField(64);
        titleTF.setMinimumSize(new Dimension(222, 20));
        titleTF.setPreferredSize(new Dimension(222, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(5, 3, 0, 3);
        rootPanel.add(titleTF, gridBagConstraints);

///////////
// Line 1
///////////

        // Set position of title "Request" in the GUI
        l = new JLabel("Request:");
        l.setHorizontalAlignment(SwingConstants.LEFT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 0, 3);
        rootPanel.add(l, gridBagConstraints);

        // Set position of radio button "Join" in the GUI
        joinButton = new JRadioButton("Join");
        joinButton.setActionCommand("Join");
        joinButton.setSelected(false);
        joinButton.setMinimumSize(new Dimension(70, 20));
        joinButton.setPreferredSize(new Dimension(70, 20));
        joinButton.setHorizontalAlignment(SwingConstants.LEFT);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 0, 3);
        rootPanel.add(joinButton, gridBagConstraints);

        // Set position of exit-auction button in the GUI
        exitButton = new JRadioButton("Exit");
        exitButton.setActionCommand("Exit");
        exitButton.setSelected(false);
        exitButton.setHorizontalAlignment(SwingConstants.LEFT);
        exitButton.setMinimumSize(new Dimension(70, 20));
        exitButton.setPreferredSize(new Dimension(70, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 3, 0, 3);
        rootPanel.add(exitButton, gridBagConstraints);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(joinButton);
        buttonGroup.add(exitButton);

        rootPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

        getContentPane().add(rootPanel, BorderLayout.NORTH);

        // Set size and position of logTA in the GUI
        logTA = new JTextArea();
        logTA.setEnabled(false);
        JScrollPane jsp = new JScrollPane(logTA);
        jsp.setMinimumSize(new Dimension(300, 205));
        jsp.setPreferredSize(new Dimension(300, 205));
        JPanel p = new JPanel();
        p.setBorder(new BevelBorder(BevelBorder.LOWERED));
        p.add(jsp);
        getContentPane().add(p, BorderLayout.CENTER);

        // Set reaction of GUI when send-button is pressed
        p = new JPanel();
        sendB = new JButton("Send");
        sendB.addActionListener(e -> {
            String title = titleTF.getText();
            boolean join = joinButton.isSelected();
            boolean cancel = exitButton.isSelected();

            // If no auction is available
            if (myAgent.getAuctioneerAgent() == null) {
                JOptionPane.showMessageDialog(CarrierAgentGuiImpl.this, "No auction available at the moment", "WARNING", JOptionPane.WARNING_MESSAGE);
            } else {
                // If an auction name is specified
                if (title != null && title.length() > 0) {
                    // If request is "Join"
                    if (join) {
                        // Send "Join" request to auctioneer agent
                        myAgent.sendJoinRequest(title + " Join");
                        notifyUser("Request sent: " + "Join auction " + title);
                    }
                    // If request is "Exit"
                    else if (cancel) {
                        // Send "Exit" request to auctioneer agent
                        myAgent.sendExitRequest(title + " Cancel");
                        notifyUser("Request sent: " + "Exit auction " + title);
                    }
                    // No request is specified, show error message
                    else {
                        JOptionPane.showMessageDialog(CarrierAgentGuiImpl.this, "Request not specified", "WARNING", JOptionPane.WARNING_MESSAGE);
                    }
                }
                // No auction name specified, show error message
                else {
                    JOptionPane.showMessageDialog(CarrierAgentGuiImpl.this, "No auction specified", "WARNING", JOptionPane.WARNING_MESSAGE);
                }
        }
        });

        // Set reaction of GUI when reset-button is pressed
        resetB = new JButton("Reset");
        resetB.addActionListener(e -> {

            // Reset text field
            titleTF.setText("");

            // Reset radio buttons
            buttonGroup.clearSelection();
        });

        // Set reaction of GUI when exit-button is pressed
        exitB = new JButton("Exit");
        exitB.addActionListener(e -> {
            // Terminate carrier agent
            myAgent.doDelete();
        });

        sendB.setPreferredSize(resetB.getPreferredSize());
        exitB.setPreferredSize(resetB.getPreferredSize());

        p.add(sendB);
        p.add(resetB);
        p.add(exitB);

        p.setBorder(new BevelBorder(BevelBorder.LOWERED));
        getContentPane().add(p, BorderLayout.SOUTH);

        pack();

        setResizable(false);
    }

    /**
     * This method is to set the name showed at the top of GUI
     * @param a Carrier agent
     */
    public void setAgent(CarrierAgent a) {
        myAgent = a;
        // Set title of GUI to name of carrier agent
        setTitle(myAgent.getName());
    }

    /**
     * This method is to print a message in the GUI
     * @param message Message to print in GUI
     */
    public void notifyUser(String message) {
        logTA.append(message+"\n");
    }
}
