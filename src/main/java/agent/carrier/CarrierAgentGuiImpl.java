package main.java.agent.carrier;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.border.*;

import main.java.agent.TourPlanning;


/**
 J2SE (Swing-based) implementation of the GUI of the main.java.agent that wants to join/exit an auction
 */
public class CarrierAgentGuiImpl extends JFrame implements CarrierAgentGui{

    private CarrierAgent myAgent;

    // Text field to enter auction name
    private JTextField titleTF;

    // Print logs
    private JTextArea logTextArea;

    public CarrierAgentGuiImpl() {
        super();

        // Terminate agent when GUI window is closed
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                myAgent.sendExitRequest();
                myAgent.doDelete();
            }
        } );

        
        GridBagConstraints c = new GridBagConstraints();
        GridBagLayout l = new GridBagLayout();

        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();

        leftPanel.setMinimumSize(new Dimension(600, 550));
        leftPanel.setPreferredSize(new Dimension(600, 550));
 
        // rightPanel.setMinimumSize(new Dimension(600, 500));
        // rightPanel.setPreferredSize(new Dimension(600, 500));

//////
//    Right Panel
//////

        // Visual Panel
        TitledBorder visualTitle = new TitledBorder("Visualization");
        visualTitle.setTitleJustification(TitledBorder.CENTER);
        visualTitle.setTitlePosition(TitledBorder.TOP);

        JPanel visualPanel = new JPanel();
        visualPanel.setBorder(visualTitle);
        visualPanel.setMinimumSize(new Dimension(590, 540));
        visualPanel.setPreferredSize(new Dimension(590, 540));


        // Visual Button
        JButton visualButton = new JButton("Visualization");
        visualButton.addActionListener(e -> {
            try (Connection conn = connectToDB()) {
                Statement stm = conn.createStatement();
                ResultSet result = stm.executeQuery("SELECT * FROM AGENTS WHERE Agent_ID =" + myAgent.getAgentID());
                myAgent.setVehicleID(result.getString("Vehicle_ID"));
                visualPanel.add(TourPlanning.tourVisualize(myAgent.getVehicleID(), myAgent.getDepotX(), myAgent.getDepotY()));
                revalidate();
                
            } catch (SQLException sqle) {
                notifyUser("Failed to connect to database. Please try again later.");
                sqle.printStackTrace();
            } catch (Exception ex) {
                notifyUser("Failed to visualize.");
                ex.printStackTrace();
            }
        });
        

        rightPanel.add(visualPanel);

//////
//    Bottom Panel
//////

        // Exit Button
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            myAgent.sendExitRequest();
            myAgent.doDelete();
            dispose();
        });

//////
//    Left Panel
//////

        // Agent request Panel
        TitledBorder requestTitle = new TitledBorder("Agent request");

        JScrollPane requestScrollPane = new JScrollPane();
        requestScrollPane.setMinimumSize(new Dimension(200, 150));
        requestScrollPane.setPreferredSize(new Dimension(200, 150));

        JPanel requestPanel = new JPanel();
        requestPanel.setBorder(requestTitle);
        requestPanel.add(requestScrollPane);


        // Depot location panel
        TitledBorder depotTitle = new TitledBorder("Agent Location");
        JLabel depotXLabel = new JLabel("X:");
        JLabel depotYLabel = new JLabel("Y:");
        JTextField depotXField = new JTextField();
        JTextField depotYField = new JTextField();
		depotXField.setPreferredSize(new Dimension(40, 20));
		depotYField.setPreferredSize(new Dimension(40, 20));

        JPanel locationPanel = new JPanel();
        c.gridx = 0;
        c.gridy = 0;
        locationPanel.add(depotXLabel, c);
        c.gridx = 1;
        locationPanel.add(depotXField, c);
        c.gridx = 2;
        locationPanel.add(depotYLabel, c);
        c.gridx = 3;
        locationPanel.add(depotYField, c);

        // Add depot location button
        JButton depotButton = new JButton("Add Location");
        depotButton.addActionListener(e -> {
            try {
                // int depotX = Integer.parseInt(depotXField.getText());
                // int depotY = Integer.parseInt(depotYField.getText());

                myAgent.setDepotX(Double.parseDouble(depotXField.getText()));
                myAgent.setDepotY(Double.parseDouble(depotYField.getText()));

                notifyUser("Your new location is set at (" + myAgent.getDepotX() + ", " + myAgent.getDepotY() + ")");

                // Statement stm = conn.createStatement();
                // stm.executeUpdate("UPDATE AGENTS SET Depot_X = " + depotX + ", Depot_Y = " + depotY + " WHERE Agent_ID = " + myAgent.getAgentID());
                // notifyUser("Your new location is set at (" + depotX + ", " + depotY + ")");
                
                // conn.close();
            // } catch (SQLException sqle) {
            //     notifyUser("Failed to connect to database. Please try again later.");
            //     sqle.printStackTrace();
            } catch (Exception ex) {
				// errorLabel.setText(ex.getMessage());
				ex.printStackTrace();
				notifyUser("Enter coordinates to add your location");
			}
        });

        JPanel depotPanel = new JPanel();
        depotPanel.setMinimumSize(new Dimension(220, 100));
        depotPanel.setPreferredSize(new Dimension(220, 100));
        depotPanel.setBorder(depotTitle);
        depotPanel.add(locationPanel, BorderLayout.CENTER);
        depotPanel.add(depotButton, BorderLayout.SOUTH);



        // Agent Panel
        JPanel agentPanel = new JPanel();
        agentPanel.setMinimumSize(new Dimension(225, 340));
        agentPanel.setPreferredSize(new Dimension(225, 340));
        agentPanel.add(requestPanel, BorderLayout.NORTH);
        agentPanel.add(depotPanel, BorderLayout.CENTER);
        agentPanel.add(visualButton, BorderLayout.SOUTH);


        // Bid request Panel
        TitledBorder bidRequestTitle = new TitledBorder("Bidding request");

        JScrollPane bidRequestScrollPane = new JScrollPane();
        bidRequestScrollPane.setMinimumSize(new Dimension(200, 150));
        bidRequestScrollPane.setPreferredSize(new Dimension(200, 150));

        JPanel bidRequestPanel = new JPanel();
        bidRequestPanel.setBorder(bidRequestTitle);
        bidRequestPanel.add(bidRequestScrollPane);


        // Auction Panel
        JPanel auctionPanel = new JPanel();
        auctionPanel.setMinimumSize(new Dimension(365, 340));
        auctionPanel.setPreferredSize(new Dimension(365, 340));
        c.gridx = 0;
        c.gridy = 0;
        auctionPanel.add(bidRequestPanel, c);


        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setMinimumSize(new Dimension(600, 330));
        mainPanel.setPreferredSize(new Dimension(600, 330));
        mainPanel.add(agentPanel, BorderLayout.WEST);
        mainPanel.add(auctionPanel, BorderLayout.EAST);
      
        leftPanel.add(mainPanel, BorderLayout.NORTH);


        // Log Panel
        TitledBorder logTitle = new TitledBorder("Log");

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setForeground(Color.GRAY);

        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        logScrollPane.setMinimumSize(new Dimension(570, 120));
        logScrollPane.setPreferredSize(new Dimension(570, 120));

        JPanel logPanel = new JPanel();
        logPanel.setMinimumSize(new Dimension(590, 155));
        logPanel.setPreferredSize(new Dimension(590, 155));
        logPanel.setBorder(logTitle);
        logPanel.add(logScrollPane);

        leftPanel.add(logPanel, BorderLayout.SOUTH);


        getContentPane().add(leftPanel, BorderLayout.WEST);
        getContentPane().add(rightPanel, BorderLayout.EAST);
        getContentPane().add(exitButton, BorderLayout.SOUTH);


        pack();      
        setResizable(false);
        setVisible(true);
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
        logTextArea.append(message+"\n");
    }

    private static Connection connectToDB() throws SQLException {
		String dburl = "jdbc:sqlite:data.db";
		return DriverManager.getConnection(dburl);
	}
}
