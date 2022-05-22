package main.java.agent.carrier;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.border.*;


/**
 J2SE (Swing-based) implementation of the GUI of the main.java.agent that wants to join/exit an auction
 */
public class CarrierAgentGuiImpl extends JFrame implements CarrierAgentGui{

    private CarrierAgent myAgent;

    // Text field to enter auction name
    private JTextField titleTF;

    private JButton exitB;

    // Print logs
    private JTextArea logTA;

    public CarrierAgentGuiImpl() {
        super();

        // Terminate agent when GUI window is closed
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myAgent.sendExitRequest();
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
        titleTF.setEditable(false);
        titleTF.setMinimumSize(new Dimension(222, 20));
        titleTF.setPreferredSize(new Dimension(222, 20));
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new Insets(5, 3, 0, 3);
        rootPanel.add(titleTF, gridBagConstraints);

        rootPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

        getContentPane().add(rootPanel, BorderLayout.NORTH);

        // Set size and position of logTA in the GUI
        logTA = new JTextArea();
        logTA.setEditable(false);
        logTA.setForeground(Color.GRAY);
        JScrollPane jsp = new JScrollPane(logTA);
        jsp.setMinimumSize(new Dimension(300, 205));
        jsp.setPreferredSize(new Dimension(300, 205));
        JPanel p = new JPanel();
        p.setBorder(new BevelBorder(BevelBorder.LOWERED));
        p.add(jsp);
        getContentPane().add(p, BorderLayout.CENTER);

        // Set reaction of GUI when send-button is pressed
        p = new JPanel();

        // Set reaction of GUI when exit-button is pressed
        exitB = new JButton("Exit");
        exitB.addActionListener(e -> {
            // Terminate carrier agent
            myAgent.sendExitRequest();
            myAgent.doDelete();
            dispose();
        });

        p.add(exitB);

        p.setBorder(new BevelBorder(BevelBorder.LOWERED));
        getContentPane().add(p, BorderLayout.SOUTH);

        pack();

        setResizable(false);
    }


    public void setAuctionField(String auction) {
        titleTF.setText(auction);
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
