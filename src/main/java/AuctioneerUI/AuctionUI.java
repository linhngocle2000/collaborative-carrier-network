package AuctioneerUI;

import UIResource.UIData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

public class AuctionUI extends JFrame {

    private static JLabel trReq, owner, lowestBid,
            currentBidder, iteration;

    private Color background = UIData.getBackground();
    private int width = UIData.getWidth();
    private int height = UIData.getHeight();
    private Font font = UIData.getFont();
    private Color errorColor = UIData.getErrorColor();
    private Border emptyBorder = UIData.getEmptyBorder();

    public AuctionUI() {

        super();

        setTitle("CCN");
        setSize(width, height);
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBackground(background);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBackground(background);

        Border innerBorder1 = BorderFactory.createTitledBorder("Transport request");
        Border innerBorder2 = BorderFactory.createTitledBorder("Auction");
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        topPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder1));
        rootPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder2));

        JLabel trReqLabel = new JLabel("Request: ");
        trReqLabel.setFont(font.deriveFont(Font.BOLD, 12));
        trReqLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 15);
        topPanel.add(trReqLabel, constraints);

        trReq = new JLabel("((0,1),(2,3))");
        trReq.setFont(font.deriveFont(Font.PLAIN, 12));
        trReq.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 0);
        topPanel.add(trReq, constraints);

        JLabel ownerLabel = new JLabel("Owner: ");
        ownerLabel.setFont(font.deriveFont(Font.BOLD, 12));
        ownerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 15);
        topPanel.add(ownerLabel, constraints);

        owner = new JLabel("Peter");
        owner.setFont(font.deriveFont(Font.PLAIN, 12));
        owner.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 0);
        topPanel.add(owner, constraints);


        JLabel iterationLabel = new JLabel("Iteration: ");
        iterationLabel.setFont(font.deriveFont(Font.BOLD, 12));
        iterationLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 15, 15);
        topPanel.add(iterationLabel, constraints);

        iteration = new JLabel("1");
        iteration.setFont(font.deriveFont(Font.PLAIN, 12));
        iteration.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 15, 0);
        topPanel.add(iteration, constraints);

        JLabel currentBidderLabel = new JLabel("Current bidder: ");
        currentBidderLabel.setFont(font.deriveFont(Font.BOLD, 12));
        currentBidderLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 15);
        rootPanel.add(currentBidderLabel, constraints);

        currentBidder = new JLabel("Amy ");
        currentBidder.setFont(font.deriveFont(Font.PLAIN, 12));
        currentBidder.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 0);
        rootPanel.add(currentBidder, constraints);

        JLabel lowestBidLabel = new JLabel("Lowest bid (\u20AC): ");
        lowestBidLabel.setFont(font.deriveFont(Font.BOLD, 12));
        lowestBidLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 15, 15);
        rootPanel.add(lowestBidLabel, constraints);

        lowestBid = new JLabel("500.00 ");
        lowestBid.setFont(font.deriveFont(Font.PLAIN, 12));
        lowestBid.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 15, 0);
        rootPanel.add(lowestBid, constraints);

/*        JLabel timerLabel = new JLabel("Remaining time: ");
        timerLabel.setFont(font.deriveFont(Font.BOLD, 12));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 15);
        bottomPanel.add(timerLabel, constraints);*/

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(20, 0, 20, 0);
        bottomPanel.add(new AuctionTimer(), constraints);


        getContentPane().add(topPanel, "North");
        getContentPane().add(rootPanel, "Center");
        getContentPane().add(bottomPanel, "South");
        getContentPane().setBackground(background);

        new Timer(180_000, (e) -> {setVisible(false); dispose(); }).start();


        pack();
        setResizable(false);
    }

    public void setTrReq(String s) {
        trReq.setText(s);
    }

    public void setOwner(String s) {
        owner.setText(s);
    }


    public void setIteration(String s) {
        iteration.setText(s);
    }

    public void setCurrentBidder(String s) {
        currentBidder.setText(s);
    }

    public void setLowestBid(String s) {
        lowestBid.setText(s);
    }
}


class AuctionTimer extends JLabel {

    private Timer timer;
    private long startTime = -1;
    private long duration = 180000;
    private Font font = UIData.getFont();


    public AuctionTimer() {
        setLayout(new GridBagLayout());
        setFont(font.deriveFont(Font.BOLD, 14));
        timer = new Timer(10, e -> {
            if (startTime < 0) {
                startTime = System.currentTimeMillis();
            }
            long now = System.currentTimeMillis();
            long clockTime = now - startTime;
            if (clockTime >= duration) {
                clockTime = duration;
                timer.stop();
            }
            SimpleDateFormat df = new SimpleDateFormat("mm:ss");
            setText(df.format(duration - clockTime));
        });
        timer.setInitialDelay(0);
        timer.start();
    }

}

