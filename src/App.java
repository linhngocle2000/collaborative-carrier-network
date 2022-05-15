import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
//import jade.wrapper.ContainerController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import main.java.agent.auctioneer.AuctioneerAgent;
import main.java.agent.carrier.CarrierAgent;



public class App extends JFrame {
	static public void main(String[] args) {
		(new App()).setVisible(true);
	}

	public App(String title) {
		this();
		setTitle(title);
	}

	// Background color for UI elements
	Color background;

	// Database connection
	// DatabaseConnection db;

	// Jade
	jade.core.Runtime runtime;
	private List<ContainerController> containers;



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
		containers = new ArrayList<>();

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

		// Root panel
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new GridBagLayout());
		rootPanel.setMinimumSize(new Dimension(330, 100));
		rootPanel.setPreferredSize(new Dimension(330, 100));
		rootPanel.setBackground(background);

		// Label for error messages
		JLabel errorLabel = new JLabel();
		errorLabel.setForeground(new Color(1f, 0f, 0f));
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 8;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = java.awt.GridBagConstraints.CENTER;
		constraints.insets = new java.awt.Insets(10, 3, 0, 3);
		rootPanel.add(errorLabel, constraints);


		// Join label
		JLabel joinLabel = new JLabel("Join an auction");
		Font font = joinLabel.getFont();
		joinLabel.setFont(font.deriveFont(Font.BOLD, 14));
		joinLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = java.awt.GridBagConstraints.CENTER;
		constraints.insets = new java.awt.Insets(10, 3, 0, 3);
		rootPanel.add(joinLabel, constraints);

		// Join name label
		JLabel joinNameLabel = new JLabel("Your name");
		joinNameLabel.setHorizontalAlignment(SwingConstants.LEFT);

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(10, 3, 0, 3);
		rootPanel.add(joinNameLabel, constraints);

		// Join name text field
		JTextField joinNameText = new JTextField();
		joinNameText.setPreferredSize(new Dimension(150, 20));

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(0, 3, 0, 3);
		rootPanel.add(joinNameText, constraints);

		// Join host label
		JLabel joinHostLabel = new JLabel("Host");
		joinHostLabel.setHorizontalAlignment(SwingConstants.LEFT);

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(10, 3, 0, 3);
		rootPanel.add(joinHostLabel, constraints);

		// Join host text field
		JTextField joinHostText = new JTextField();
		joinHostText.setPreferredSize(new Dimension(150, 20));

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(0, 3, 0, 3);
		rootPanel.add(joinHostText, constraints);

		// Join port label
		JLabel joinPortLabel = new JLabel("Port");
		joinPortLabel.setHorizontalAlignment(SwingConstants.LEFT);

		constraints = new GridBagConstraints();
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(10, 3, 0, 3);
		rootPanel.add(joinPortLabel, constraints);

		// Join port text field
		JTextField joinPortText = new JTextField();
		joinPortText.setPreferredSize(new Dimension(50, 20));

		constraints = new GridBagConstraints();
		constraints.gridx = 2;
		constraints.gridy = 2;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(0, 3, 0, 3);
		rootPanel.add(joinPortText, constraints);


		// Join button
		JButton buttonBidder = new JButton();
		buttonBidder.setText("Join");
		buttonBidder.addActionListener(e -> {
			try {
				errorLabel.setText("");

				String name = joinNameText.getText();
				if (name == null || name.trim().length() == 0) {
					throw new Exception("Enter a name to join an auction");
				}
				name = name.trim();

				String host = joinHostText.getText();
				if (host == null || host.trim().length() == 0) {
					throw new Exception("Enter an auction name");
				}
				host = host.trim();

				String port = joinPortText.getText();
				if (port == null || port.trim().length() == 0) {
					throw new Exception("Enter a port to join an auction");
				}
				port = port.trim();

				// Parse int to throw an exception if port is not an int
				Integer.parseInt(port);

				
				// Create profile
//				Profile prof = new ProfileImpl();
//				prof.setParameter(Profile.CONTAINER_NAME, "Carrier_" + name);
//				prof.setParameter(Profile.MAIN_HOST, host);
//				prof.setParameter(Profile.MAIN_PORT, port);
				ProfileImpl pContainer;
				pContainer = new ProfileImpl("127.0.0.1", 8888, "Ithaq");
				pContainer.setParameter(Profile.CONTAINER_NAME,"DistantContainer");



				// Create new main container
				ContainerController container = runtime.createAgentContainer(pContainer);
				containers.add(container);

				// Instantiate agent
				CarrierAgent agent = new CarrierAgent();
				AgentController controller = container.acceptNewAgent(name, agent);
				controller.start();
			} catch (NumberFormatException ex) {
				errorLabel.setText("Enter a valid port number");
			} catch (Exception ex) {
				errorLabel.setText(ex.getMessage());
				ex.printStackTrace();
				System.out.println("Could not join auction :(");
			}
		});

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = java.awt.GridBagConstraints.CENTER;
		constraints.insets = new java.awt.Insets(10, 3, 10, 3);
		rootPanel.add(buttonBidder, constraints);

		// Auction label
		JLabel auctionLabel = new JLabel("Create an auction");
		font = joinLabel.getFont();
		auctionLabel.setFont(font.deriveFont(Font.BOLD, 14));
		auctionLabel.setHorizontalAlignment(SwingConstants.CENTER);

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = java.awt.GridBagConstraints.CENTER;
		constraints.insets = new java.awt.Insets(30, 3, 0, 3);
		rootPanel.add(auctionLabel, constraints);

		// Your name label
		JLabel yourNameLabel = new JLabel("Your name");
		yourNameLabel.setHorizontalAlignment(SwingConstants.LEFT);

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(10, 3, 0, 3);
		rootPanel.add(yourNameLabel, constraints);

		// Your name text field
		JTextField yourNameText = new JTextField();
		yourNameText.setPreferredSize(new Dimension(150, 20));

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(0, 3, 0, 3);
		rootPanel.add(yourNameText, constraints);

		// Auction name label
		JLabel createNameLabel = new JLabel("Auction name");
		createNameLabel.setHorizontalAlignment(SwingConstants.LEFT);

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(10, 3, 0, 3);
		rootPanel.add(createNameLabel, constraints);
		
		// Auction name text field
		JTextField createNameText = new JTextField();
		createNameText.setPreferredSize(new Dimension(150, 20));

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 6;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(0, 3, 0, 3);
		rootPanel.add(createNameText, constraints);
		
		// Auction port label
		JLabel createPortLabel = new JLabel("Port");
		createPortLabel.setHorizontalAlignment(SwingConstants.LEFT);

		constraints = new GridBagConstraints();
		constraints.gridx = 2;
		constraints.gridy = 5;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(10, 3, 0, 3);
		rootPanel.add(createPortLabel, constraints);

		// Auction port text field
		JTextField createPortText = new JTextField();
		createPortText.setPreferredSize(new Dimension(50, 20));

		constraints = new GridBagConstraints();
		constraints.gridx = 2;
		constraints.gridy = 6;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(0, 3, 0, 3);
		rootPanel.add(createPortText, constraints);

		// Auction button
		JButton buttonAuctioneer = new JButton();
		buttonAuctioneer.setText("Create");
		buttonAuctioneer.setVisible(true);
		buttonAuctioneer.addActionListener(e -> {
			try {
				errorLabel.setText("");

				String name = yourNameText.getText();
				if (name == null || name.trim().length() == 0) {
					throw new Exception("Enter your name to create an auction");
				}
				name = name.trim();

				String host = joinHostText.getText().trim();

				String auctionName = createNameText.getText();
				if (auctionName == null || auctionName.trim().length() == 0) {
					throw new Exception("Enter an auction name to create an auction");
				}
				auctionName = auctionName.trim();

				String port = createPortText.getText();
				if (port == null || port.trim().length() == 0) {
					throw new Exception("Enter a port to create an auction");
				}
				port = port.trim();

				// Parse int to throw an exception if port is not an int
				Integer.parseInt(port);

				// Create profile
				Profile prof = new ProfileImpl("127.0.0.1", 8888, "Ithaq");


				// Create a main container
				AgentContainer mainContainerRef = runtime.createMainContainer(prof);
				//ContainerController container = runtime.createMainContainer(prof);
				containers.add(mainContainerRef);

				AgentController rma;
				rma = mainContainerRef.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
				rma.start();

				AgentController snif;
				snif= mainContainerRef.createNewAgent("sniffeur", "jade.tools.sniffer.Sniffer",new Object[0]);
				snif.start();



				// Instantiate agent
				AuctioneerAgent agent = new AuctioneerAgent(auctionName);
				AgentController controller = mainContainerRef.acceptNewAgent(name, agent);
				controller.start();
			} catch (NumberFormatException ex) {
				errorLabel.setText("Enter a valid port number");
			} catch (Exception ex) {
				errorLabel.setText(ex.getMessage());
				ex.printStackTrace();
				System.out.println("Could not create auction :(");
			}
		});

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 7;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = java.awt.GridBagConstraints.CENTER;
		constraints.insets = new java.awt.Insets(10, 3, 10, 3);
		rootPanel.add(buttonAuctioneer, constraints);

		getContentPane().add(rootPanel);
		getContentPane().setBackground(background);
	}
}