import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import agent.auctioneer.AuctioneerAgent;
import agent.carrier.CarrierAgent;

public class App extends JFrame {
	static public void main(String args[]) {
		(new App()).setVisible(true);
	}

	public App(String title) {
		this();
		setTitle(title);
	}

	// Background color for UI elements
	Color background;

	// Initial view
	JButton buttonAuctioneer, buttonBidder;

	// Java swing UI elements
	JTextArea textArea1;
	JLabel label1, label2, label3, label4, label5;
	JButton buttonAddPerson, buttonRemovePerson, buttonOk, buttonExit;
	JFrame errFrame;
	JLabel textFieldErrMsg;
	JScrollPane invitedScrollPane;
	JScrollPane knownScrollPane;
	JScrollPane appointmentScrollPane;
	JPanel errPanel;
	JViewport port;

	// Database connection
	// DatabaseConnection db;

	// Jade
	jade.core.Runtime runtime;
	List<ContainerController> containers;

	public App() {
		background = new Color(1f, 1f, 1f);

		setTitle("Auctioneer Agent Platform");
		setSize(720, 400);
		setLocation(50, 50);
		setBackground(background);

		createWelcomeUI();

		// Setup jade environment
		runtime = jade.core.Runtime.instance();
		runtime.setCloseVM(true); // Exit jade after the last container is killed
		containers = new ArrayList<ContainerController>();

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);

				// Kill all containers
				for (ContainerController container : containers) {
					try {
						container.kill();
					} catch (StaleProxyException ex) {
						ex.printStackTrace();
					}
				}

				dispose();
			}
		});

		// db = new DatabaseConnection();
		// db.Connect();
	}

	private void createWelcomeUI()
	{
		// label1 = new JLabel("No auctions available", JLabel.CENTER);
		// label1.setVisible(true);

		buttonAuctioneer = new JButton();
		buttonAuctioneer.setText("Create an auction");
		buttonAuctioneer.setVisible(true);
		buttonAuctioneer.addActionListener(e -> {
			try {
				// Create profile
				Profile prof = new ProfileImpl();
				prof.setParameter(Profile.CONTAINER_NAME, "TestContainer");
				// prof.setParameter(Profile.MAIN_HOST, "localhost"); // The address of host needs to be read from the database or user input

				// Create new main container
				ContainerController container = runtime.createMainContainer(prof);
				containers.add(container);

				// Instantiate agent
				AuctioneerAgent agent = new AuctioneerAgent();
				AgentController controller = container.acceptNewAgent("Nick", agent);
				controller.start();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Could not create auction :(");
			}
		});

		buttonBidder = new JButton();
		buttonBidder.setText("Join an auction");
		buttonBidder.setVisible(true);
		buttonBidder.addActionListener(e -> {
			try {
				// Create profile
				Profile prof = new ProfileImpl();
				prof.setParameter(Profile.CONTAINER_NAME, "TestContainer");
				prof.setParameter(Profile.MAIN_HOST, "localhost"); // The address of host needs to be read from the database or user input

				// Create new main container
				ContainerController container = runtime.createAgentContainer(prof);
				containers.add(container);

				// Instantiate agent
				CarrierAgent agent = new CarrierAgent();
				AgentController controller = container.acceptNewAgent("Amy", agent);
				controller.start();
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("Could not join auction :(");
			}
		});

		JPanel panel = new JPanel();
		panel.setBackground(background);
		panel.add(buttonAuctioneer);
		panel.add(buttonBidder);

		getContentPane().add(panel);
		getContentPane().setBackground(background);
	}
}