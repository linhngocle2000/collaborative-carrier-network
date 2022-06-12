package AuctioneerUI;

import UIResource.UIData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

public class AuctionUI extends JFrame {

    private static JLabel trReq, owner, price,
            winner, iteration;

    private Color background = UIData.getBackground();
    private int width = UIData.getWidth();
    private int height = UIData.getHeight();
    private Font font = UIData.getFont();
    private AuctionTimer auctionTimer;

    public AuctionUI() {

        super();

        auctionTimer = new AuctionTimer();

///////////
// Frame
///////////
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (auctionTimer.timerRunning()) {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                } else {
                    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                }
            }
        } );

        setTitle("CCN");
        setSize(width, height);
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);

///////////
// Panels
///////////

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBackground(background);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBackground(background);

        Border innerBorder1 = BorderFactory.createTitledBorder("Auction overview");
        Border innerBorder2 = BorderFactory.createTitledBorder("Result");
        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        topPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder1));
        rootPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder2));

///////////
// Overview
///////////

        JLabel trReqLabel = new JLabel("Request: ");
        trReqLabel.setFont(font.deriveFont(Font.BOLD, 12));
        trReqLabel.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 15);
        topPanel.add(trReqLabel, constraints);

        trReq = new JLabel();
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

        owner = new JLabel();
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

        iteration = new JLabel();
        iteration.setFont(font.deriveFont(Font.PLAIN, 12));
        iteration.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 15, 0);
        topPanel.add(iteration, constraints);

///////////
// Running
///////////

        JLabel runningLabel = new JLabel("Auction is running... ");
        runningLabel.setFont(font.deriveFont(Font.ITALIC, 14));
        runningLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(0, 0, 0, 0);
        rootPanel.add(runningLabel, constraints);

/*        JLabel timerLabel = new JLabel("Remaining time: ");
        timerLabel.setFont(font.deriveFont(Font.BOLD, 12));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 15);
        bottomPanel.add(timerLabel, constraints);*/

///////////
// Timer
///////////

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(20, 0, 20, 0);
        bottomPanel.add(auctionTimer, constraints);

///////////
// Result
///////////

        new Timer(11000, (e) -> {
            if (!auctionTimer.timerRunning()) {
                runningLabel.setVisible(false);

                JLabel winnerLabel = new JLabel("Winner: ");
                winnerLabel.setFont(font.deriveFont(Font.BOLD, 12));
                winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);

                GridBagConstraints constraints1 = new GridBagConstraints();
                constraints1.gridx = 0;
                constraints1.gridy = 0;
                constraints1.anchor = GridBagConstraints.NORTHWEST;
                constraints1.insets = new Insets(15, 0, 0, 15);
                rootPanel.add(winnerLabel, constraints1);

                winner = new JLabel("Amy ");
                winner.setFont(font.deriveFont(Font.PLAIN, 12));
                winner.setHorizontalAlignment(SwingConstants.CENTER);

                GridBagConstraints constraints2 = new GridBagConstraints();
                constraints2.gridx = 1;
                constraints2.gridy = 0;
                constraints2.anchor = GridBagConstraints.NORTHWEST;
                constraints2.insets = new Insets(15, 0, 0, 0);
                rootPanel.add(winner, constraints2);

                JLabel priceLabel = new JLabel("Price (\u20AC): ");
                priceLabel.setFont(font.deriveFont(Font.BOLD, 12));
                priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

                GridBagConstraints constraints3 = new GridBagConstraints();
                constraints3.gridx = 0;
                constraints3.gridy = 2;
                constraints3.anchor = GridBagConstraints.NORTHWEST;
                constraints3.insets = new Insets(15, 0, 15, 15);
                rootPanel.add(priceLabel, constraints3);

                price = new JLabel("500.00 ");
                price.setFont(font.deriveFont(Font.PLAIN, 12));
                price.setHorizontalAlignment(SwingConstants.CENTER);

                GridBagConstraints constraints4 = new GridBagConstraints();
                constraints4.gridx = 1;
                constraints4.gridy = 2;
                constraints4.anchor = GridBagConstraints.NORTHWEST;
                constraints4.insets = new Insets(15, 0, 15, 0);
                rootPanel.add(price, constraints4);
            }
        }).start();

///////////
// Combine
///////////

        getContentPane().add(topPanel, "North");
        getContentPane().add(rootPanel, "Center");
        getContentPane().add(bottomPanel, "South");
        getContentPane().setBackground(background);

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
}


class AuctionTimer extends JLabel {

    private Timer timer;
    private long startTime = -1;
    private long duration = 10000;
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

    public boolean timerRunning() {
        return timer.isRunning();
    }

}

