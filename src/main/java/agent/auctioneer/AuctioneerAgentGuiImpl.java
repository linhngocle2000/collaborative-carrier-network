package main.java.agent.auctioneer;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.*;


/**
 J2SE (Swing-based) implementation of the GUI of the main.java.agent that offers an auction
 */
public class AuctioneerAgentGuiImpl extends JFrame implements AuctioneerAgentGui {

    private AuctioneerAgent myAgent;

    // Text field to type in name of auction
    private JTextField titleTF;

    // Buttons: Start, End, Exit
    private JButton endB;

    // Print logs
    private JTextArea logTA;


    public AuctioneerAgentGuiImpl() {
        super();

        // Terminate agent when GUI window is closed
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myAgent.endAuction();
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

        // Set position of the title "Auction name" in the GUI
        JLabel l = new JLabel("Auction name:");
        l.setHorizontalAlignment(SwingConstants.LEFT);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 0, 3);
        rootPanel.add(l, gridBagConstraints);

        // Set position of the text field for auction name in the GUI
        titleTF = new JTextField(64);
        titleTF.setMinimumSize(new Dimension(222, 20));
        titleTF.setPreferredSize(new Dimension(222, 20));
        titleTF.setEditable(false);
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

        // Set reaction of GUI when start-button is pressed
        p = new JPanel();

        // Set reaction of GUI when end-button is pressed
        endB = new JButton("End");
        endB.addActionListener(e -> {
            myAgent.endAuction();
            myAgent.doDelete();
            dispose();
        });

        p.add(endB);

        p.setBorder(new BevelBorder(BevelBorder.LOWERED));
        getContentPane().add(p, BorderLayout.SOUTH);

        pack();

        setResizable(false);
    }

    public void setAuctionField(String auction) {
        titleTF.setText(auction);
        titleTF.setEditable(false);
    }

    /**
     * This method is to set the name showed at the top of GUI
     * @param a Auctioneer agent
     */
    public void setAgent(AuctioneerAgent a) {
        myAgent = a;
        // Set title of GUI to name of auctioneer agent
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