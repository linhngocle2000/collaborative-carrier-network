import java.awt.*;
import java.awt.event.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.*;

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

	// Jade
	jade.core.Runtime runtime;
	private AgentContainer maincontainer;
	private String ip;
	private String mtp;
	private boolean isPlatformCreated = false;
	private AgentController agents;

	private ArrayList<CarrierAgent> carrieragentlist = new ArrayList<>();
	private ArrayList<AuctioneerAgent> auctioneeragentlist = new ArrayList<>();
	private JLabel errorLabel;




	public App() {
		background = new Color(1f, 1f, 1f);

		setTitle("Auction Management Platform");
		setSize(620, 400);
		setLocationRelativeTo(null);
		setBackground(background);


		createWelcomeUI();

		// Setup jade environment
		runtime = jade.core.Runtime.instance();
		runtime.setCloseVM(true); // Exit jade after the last container is killed

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				removeTerminatedAgents();
				if (!carrieragentlist.isEmpty()) {
					for (CarrierAgent carrier:carrieragentlist) {
						if (carrier.isAgentAlive()) {
							errorLabel.setText("Close all agent windows before exiting platform.");
							setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
						}
					}
				} else if (!auctioneeragentlist.isEmpty()) {
					for (AuctioneerAgent auctioneer:auctioneeragentlist) {
						if (auctioneer.isAgentAlive()) {
							errorLabel.setText("Close all agent windows before exiting platform.");
							setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
						}
					}
				} else {
					// Kill main container
					try {
						if (maincontainer != null) {
							try {
								maincontainer.getPlatformController().kill();
								setDefaultCloseOperation(EXIT_ON_CLOSE);
							} catch (StaleProxyException ex) {
								ex.printStackTrace();
							}
						}
					} catch (ControllerException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
	}

	private void removeTerminatedAgents() {
		carrieragentlist.removeIf(s -> !s.isAgentAlive());
		auctioneeragentlist.removeIf(s -> !s.isAgentAlive());
	}

	private static String getIpAddress() throws UnknownHostException {
		return Inet4Address.getLocalHost().getHostAddress();
	}

	private static Connection connectToDB() throws SQLException {
		String dburl = "jdbc:postgresql://ec2-99-80-170-190.eu-west-1.compute.amazonaws.com:5432/d7p2aqlsbl8lc8";
		String dbuser = "mwmhqhpwgzqkkc";
		String dbpw = "694d944a63cbfe7db7e950765dcac3260bd859c0668abe43045555abfba8fd0f";
		return DriverManager.getConnection(dburl, dbuser, dbpw);
	}

	private void createWelcomeUI() {

		// Root panel
		JPanel rootPanel = new JPanel();
		rootPanel.setLayout(new GridBagLayout());
		rootPanel.setMinimumSize(new Dimension(220, 100));
		rootPanel.setPreferredSize(new Dimension(220, 100));
		rootPanel.setBackground(background);

		// Label for error messages
		errorLabel = new JLabel();
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


		// Join auction name label
		JLabel joinAuctionNameLabel = new JLabel("Auction name");
		joinAuctionNameLabel.setHorizontalAlignment(SwingConstants.LEFT);

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(10, 3, 0, 3);
		rootPanel.add(joinAuctionNameLabel, constraints);

		// Join auction name text field
		JTextField joinAuctionNameText = new JTextField();
		joinAuctionNameText.setPreferredSize(new Dimension(150, 20));

		constraints = new GridBagConstraints();
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		constraints.insets = new java.awt.Insets(0, 3, 0, 3);
		rootPanel.add(joinAuctionNameText, constraints);


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


				String auctionName = joinAuctionNameText.getText();
				if (auctionName == null || auctionName.trim().length() == 0) {
					throw new Exception("Enter an auction name to join");
				}
				auctionName = auctionName.trim().toLowerCase();

				Connection connection = connectToDB();
				String auctionmtp;
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM public.auctioneer_agent where auction='"+auctionName+"'");
				if (!resultSet.next()) {
					throw new Exception("Auction not available");
				} else {
					auctionmtp = resultSet.getString("mtp");
				}

				// Create profile
				if (!isPlatformCreated) {
					ip = getIpAddress();
					mtp = "http://"+ip+":7778/acc";
					Profile prof = new ProfileImpl(ip, 8888, "Auction");
					prof.setParameter(Profile.MTPS, "jade.mtp.http.MessageTransportProtocol("+mtp+")");
					prof.setParameter(Profile.GUI, "true");

					// Create a main container
					maincontainer = runtime.createMainContainer(prof);

					// Create sniffeur agent
					agents = maincontainer.createNewAgent("sniffeur", "jade.tools.sniffer.Sniffer",new Object[0]);

					// Instantiate agent
					CarrierAgent agent = new CarrierAgent(auctionName, auctionmtp);
					agents = maincontainer.acceptNewAgent(name, agent);
					carrieragentlist.add(agent);
					isPlatformCreated = true;
				} else {
					CarrierAgent agent = new CarrierAgent(auctionName, auctionmtp);
					agents = maincontainer.acceptNewAgent(name, agent);
					carrieragentlist.add(agent);
				}
				agents.start();
			} catch (UnknownHostException unknownHoste) {
				errorLabel.setText("Failed to retrieve IP address. Please try again later.");
				unknownHoste.printStackTrace();
			} catch (SQLException sqle) {
				errorLabel.setText("Failed to connect to database. Please try again later.");
				sqle.printStackTrace();
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

		// Auction name label
		JLabel createNameLabel = new JLabel("Auction name");
		createNameLabel.setHorizontalAlignment(SwingConstants.LEFT);

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new java.awt.Insets(10, 3, 0, 3);
		rootPanel.add(createNameLabel, constraints);
		
		// Auction name text field
		JTextField createNameText = new JTextField();
		createNameText.setPreferredSize(new Dimension(150, 20));

		constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 6;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new java.awt.Insets(0, 3, 0, 3);
		rootPanel.add(createNameText, constraints);

		// Auction button
		JButton buttonAuctioneer = new JButton();
		buttonAuctioneer.setText("Create");
		buttonAuctioneer.setVisible(true);
		buttonAuctioneer.addActionListener(e -> {
			try {
				errorLabel.setText("");

				String auctionName = createNameText.getText();
				if (auctionName == null || auctionName.trim().length() == 0) {
					throw new Exception("Enter an auction name to create");
				}
				auctionName = auctionName.trim().toLowerCase();

				Connection connection = connectToDB();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM public.auctioneer_agent WHERE auction='"+auctionName+"'");
				if (resultSet.next()) {
					throw new Exception("Auction name already existed");
				}
				if (!isPlatformCreated) {
					ip = getIpAddress();
					mtp = "http://"+ip+":7778/acc";
					statement.executeUpdate("INSERT INTO public.auctioneer_agent VALUES ('" + auctionName + "','" + mtp +"')");
					Profile prof = new ProfileImpl(ip, 8888, "Auction");
					prof.setParameter(Profile.MTPS, "jade.mtp.http.MessageTransportProtocol("+mtp+")");
					prof.setParameter(Profile.GUI, "true");

					// Create a main container
					maincontainer = runtime.createMainContainer(prof);

					// Create sniffeur agent
					agents = maincontainer.createNewAgent("sniffeur", "jade.tools.sniffer.Sniffer",new Object[0]);

					// Instantiate agent
					AuctioneerAgent agent = new AuctioneerAgent(auctionName);
					agents = maincontainer.acceptNewAgent(auctionName, agent);
					auctioneeragentlist.add(agent);
					isPlatformCreated = true;
				} else {
					statement.executeUpdate("INSERT INTO public.auctioneer_agent VALUES ('" + auctionName + "','" + mtp +"')");
					AuctioneerAgent agent = new AuctioneerAgent(auctionName);
					agents = maincontainer.acceptNewAgent(auctionName, agent);
					auctioneeragentlist.add(agent);
				}
				agents.start();
			} catch (UnknownHostException unknownHoste) {
				errorLabel.setText("Failed to retrieve IP address. Please try again later.");
				unknownHoste.printStackTrace();
			} catch (SQLException sqle) {
				errorLabel.setText("Failed to connect to database. Please try again later.");
				sqle.printStackTrace();
			} catch (Exception ex) {
				errorLabel.setText(ex.getMessage());
				ex.printStackTrace();
				System.out.println("Could not join auction :(");
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