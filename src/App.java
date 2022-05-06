import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class App extends JFrame
{
	static public void main(String args[]) 
	{
		(new App()).setVisible(true);
	}

	public App(String title) 
	{
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
	DatabaseConnection db;

	public App()
	{
		background = new Color(1f, 1f, 1f);

		setTitle("Auctioneer Agent Platform");
		setSize(720, 400);
		setLocation(50, 50);
		setBackground(background);

		createWelcomeUI();
		// createUI();

		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				dispose();
			}
		});

		// db = new DatabaseConnection();
		// db.Connect();
	}

	private void createWelcomeUI()
	{
		label1 = new JLabel("No auctions available", JLabel.CENTER);
		label1.setVisible(true);

		buttonAuctioneer = new JButton();
		buttonAuctioneer.setText("Create an auction");
		buttonAuctioneer.setVisible(true);

		buttonBidder = new JButton();
		buttonBidder.setText("Join an auction");
		buttonBidder.setVisible(true);

		JPanel panel = new JPanel();
		panel.setBackground(background);
		panel.add(label1);
		panel.add(buttonAuctioneer);
		panel.add(buttonBidder);

		getContentPane().add(panel);
		getContentPane().setBackground(background);
	}

	private void createUI() {
		textArea1 = new JTextArea("", 0, 0);
		textArea1.setLineWrap(true);
		textArea1.setText("Agents will be listed here");

		Border brd = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black);
		textArea1.setBorder(brd);

		appointmentScrollPane = new JScrollPane(textArea1, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		port = appointmentScrollPane.getViewport();
		appointmentScrollPane.add(appointmentScrollPane.createVerticalScrollBar());
		appointmentScrollPane.setBackground(background);
		
		textArea1.setMinimumSize(new Dimension(350, 100));
		textArea1.setPreferredSize(new Dimension(350, 100));
		textArea1.setMaximumSize(new Dimension(550, 250));

		label2 = new JLabel("Starting On", JLabel.CENTER);
		label2.setFont(new Font("Dialog", Font.BOLD, 12));
		label4 = new JLabel("Ending With", JLabel.CENTER);
		label4.setFont(new Font("Dialog", Font.BOLD, 12));

		label1 = new JLabel("Known Persons", JLabel.CENTER);
		label1.setFont(new Font("Dialog", Font.BOLD, 12));
		label3 = new JLabel("Invited Persons", JLabel.CENTER);
		label3.setFont(new Font("Dialog", Font.BOLD, 12));
		buttonAddPerson = new JButton(">>");
		buttonAddPerson.setBackground(new Color(12632256));
		buttonRemovePerson = new JButton("<<");
		buttonRemovePerson.setBackground(new Color(12632256));
		label5 = new JLabel("Agent list", JLabel.CENTER);
		label5.setFont(new Font("Dialog", Font.BOLD, 12));
		buttonOk = new JButton();
		buttonOk.setText("Ok");
		buttonOk.setBackground(new Color(12632256));
		buttonExit = new JButton();
		buttonExit.setText("Exit");
		buttonExit.setBackground(new Color(12632256));
		textFieldErrMsg = new JLabel();
		textFieldErrMsg.setVisible(true);
		errFrame = new JFrame();
		errFrame.setTitle("Error");
		errFrame.setSize(300, 120);
		errFrame.setVisible(false);
		errFrame.getContentPane().setLayout(new BoxLayout(errFrame.getContentPane(), BoxLayout.Y_AXIS));
		errFrame.getContentPane().add(Box.createRigidArea(new Dimension(0, 15)));
		errFrame.getContentPane().add(textFieldErrMsg);
		errPanel = new JPanel();
		errPanel.setLayout(new BoxLayout(errPanel, BoxLayout.X_AXIS));
		errPanel.add(Box.createHorizontalGlue());
		errPanel.add(Box.createHorizontalGlue());
		errFrame.getContentPane().add(Box.createRigidArea(new Dimension(0, 15)));
		errFrame.getContentPane().add(errPanel);
		errFrame.getContentPane().add(Box.createRigidArea(new Dimension(0, 15)));

		// PANELS
		JPanel p1 = new JPanel();
		p1.setBackground(background);
		JPanel p2 = new JPanel();
		p2.setBackground(background);
		JPanel p3 = new JPanel();
		p3.setBackground(background);
		JPanel p4 = new JPanel();
		p4.setBackground(background);
		JPanel p5 = new JPanel();
		p5.setBackground(background);
		JPanel p6 = new JPanel();
		p6.setBackground(background);
		JPanel p7 = new JPanel();
		p7.setBackground(background);
		JPanel p8 = new JPanel();
		p8.setBackground(background);
		JPanel p9 = new JPanel();
		p9.setBackground(background);
		JPanel p10 = new JPanel();
		p10.setBackground(background);
		JPanel p11 = new JPanel();
		p11.setBackground(background);

		p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
		p2.add(Box.createRigidArea(new Dimension(0, 20)));
		label5.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		p2.add(label5);
		p2.add(Box.createRigidArea(new Dimension(0, 10)));
		p11.add(Box.createRigidArea(new Dimension(10, 0)));
		p11.add(appointmentScrollPane);
		p11.add(Box.createRigidArea(new Dimension(10, 0)));
		p2.add(p11);
		p3.setLayout(new BoxLayout(p3, BoxLayout.Y_AXIS));
		p3.add(label2);
		label2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		p3.add(Box.createRigidArea(new Dimension(0, 5)));
		p4.setLayout(new BoxLayout(p4, BoxLayout.Y_AXIS));
		label4.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		p4.add(label4);
		p4.add(Box.createRigidArea(new Dimension(0, 5)));
		// p4.add(calendar2);
		p5.setLayout(new BoxLayout(p5, BoxLayout.X_AXIS));
		p5.add(Box.createRigidArea(new Dimension(30, 0)));
		p5.add(p3);
		p5.add(Box.createRigidArea(new Dimension(30, 0)));
		p5.add(p4);
		p5.add(Box.createRigidArea(new Dimension(30, 0)));
		p6.setLayout(new BoxLayout(p6, BoxLayout.Y_AXIS));
		label1.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		p6.add(label1);
		p6.add(Box.createRigidArea(new Dimension(0, 5)));
		p7.setLayout(new BoxLayout(p7, BoxLayout.Y_AXIS));
		label3.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		p7.add(label3);
		p7.add(Box.createRigidArea(new Dimension(0, 5)));
		p8.setLayout(new BoxLayout(p8, BoxLayout.Y_AXIS));
		p8.add(Box.createRigidArea(new Dimension(0, 5)));
		p8.add(buttonAddPerson);
		p8.add(Box.createRigidArea(new Dimension(0, 5)));
		p8.add(buttonRemovePerson);
		p8.add(Box.createRigidArea(new Dimension(0, 5)));
		p8.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		p9.setLayout(new BoxLayout(p9, BoxLayout.X_AXIS));
		p9.add(Box.createRigidArea(new Dimension(30, 0)));
		p9.add(p6);
		p9.add(Box.createRigidArea(new Dimension(30, 0)));
		p9.add(p8);
		p9.add(Box.createRigidArea(new Dimension(30, 0)));
		p9.add(p7);
		p9.add(Box.createRigidArea(new Dimension(30, 0)));
		p10.setLayout(new BoxLayout(p10, BoxLayout.X_AXIS));
		p10.add(Box.createHorizontalGlue());
		p10.add(buttonOk);
		p10.add(Box.createRigidArea(new Dimension(10, 0)));
		p10.add(buttonExit);
		p10.add(Box.createHorizontalGlue());
		p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
		p2.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		p1.add(p2);
		p1.add(Box.createRigidArea(new Dimension(0, 30)));
		p1.add(p5);
		p1.add(Box.createVerticalGlue());
		p1.add(p9);
		p1.add(Box.createRigidArea(new Dimension(0, 30)));
		p1.add(p10);
		p1.add(Box.createRigidArea(new Dimension(0, 20)));
		
		getContentPane().add(p1);
		getContentPane().setBackground(background);

		buttonExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}
}