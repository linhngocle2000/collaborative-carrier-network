package CarrierUI;
import Auction.TransportRequest;
import UIResource.UIData;
import Utils.TourPlanning;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CalculatorUI extends JFrame {

    private Color background = UIData.getBackground();
    private int width = UIData.getWidth();
    private int height = UIData.getHeight();
    private Font font = UIData.getFont();
    private Color errorColor = UIData.getErrorColor();
    private Border emptyBorder = UIData.getEmptyBorder();

    private JTextField pickupXText, pickupYText, deliverXText, deliverYText;

    public CalculatorUI(TourPlanning tour) {

        super();

///////////
// Frame
///////////

        setTitle("CCN");
        setSize(width, height);
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

///////////
// Panels
///////////

        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new GridBagLayout());
        rootPanel.setBackground(background);
        Border innerBorder = BorderFactory.createTitledBorder("Result");
        Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        rootPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        topPanel.setBackground(background);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridBagLayout());
        bottomPanel.setBackground(background);

///////////
// Input
///////////

        JLabel pickupX = new JLabel("Pickup latitude ");
        pickupX.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(25, 0, 0, 0);
        topPanel.add(pickupX, constraints);

        pickupXText = new JTextField();
        pickupXText.setPreferredSize(new Dimension(100, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(3, 0, 0, 20);
        topPanel.add(pickupXText, constraints);

        JLabel pickupY = new JLabel("Pickup longitude ");
        pickupY.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(25, 0, 0, 0);
        topPanel.add(pickupY, constraints);

        pickupYText = new JTextField();
        pickupYText.setPreferredSize(new Dimension(100, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(3, 0, 0, 0);
        topPanel.add(pickupYText, constraints);

        JLabel deliverX = new JLabel("Deliver latitude ");
        deliverX.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 0);
        topPanel.add(deliverX, constraints);

        deliverXText = new JTextField();
        deliverXText.setPreferredSize(new Dimension(100, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(3, 0, 15, 20);
        topPanel.add(deliverXText, constraints);

        JLabel deliverY = new JLabel("Deliver longitude ");
        deliverY.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 0);
        topPanel.add(deliverY, constraints);

        deliverYText = new JTextField();
        deliverYText.setPreferredSize(new Dimension(100, 22));

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(3, 0, 15, 0);
        topPanel.add(deliverYText, constraints);

///////////
// Result
///////////

        JLabel cost = new JLabel("Cost (\u20AC): ");
        cost.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 0);
        rootPanel.add(cost, constraints);

        JLabel costText = new JLabel("-");
        costText.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 10, 0, 0);
        rootPanel.add(costText, constraints);

        JLabel earning = new JLabel("Earning (\u20AC): ");
        earning.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 0, 0);
        rootPanel.add(earning, constraints);

        JLabel earningText = new JLabel("-");
        earningText.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 10, 0, 0);
        rootPanel.add(earningText, constraints);

        JLabel revenue = new JLabel("Revenue (\u20AC): ");
        revenue.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 0, 15, 0);
        rootPanel.add(revenue, constraints);

        JLabel revenueText = new JLabel("-");
        revenueText.setHorizontalAlignment(SwingConstants.CENTER);

        constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy =2;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.insets = new Insets(15, 10, 15, 0);
        rootPanel.add(revenueText, constraints);

///////////
// Button
///////////

        JButton calcBtn = new JButton();
        calcBtn.setText("Calculate");
        calcBtn.setFocusPainted(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(15, 0, 0, 0);
        bottomPanel.add(calcBtn, constraints);

///////////
// Log
///////////

        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(errorColor);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        //Input must be number format.
        errorLabel.setText("");

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = java.awt.GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(15, 0, 20, 0);
        bottomPanel.add(errorLabel, constraints);

        calcBtn.addActionListener(e -> {
            if (!areAllInputFilled()) {
                errorLabel.setText("All fields must be filled.");
            } else if(!verifyCoordinateInput()) {
                errorLabel.setText("Input must be number format.");
            } else {
                TransportRequest tr = new TransportRequest(0,tour.getAgent(),getPickupX(),getPickupY(),getDeliverX(),getDeliverY());
                errorLabel.setText("");
                costText.setText(String.format("%.2f", tour.getTransportCostOut(tr)).replace(",","."));
                earningText.setText(String.format("%.2f", tour.getTransportCostIn(tr)).replace(",","."));
                revenueText.setText(String.format("%.2f", tour.getProfit(tr)).replace(",","."));

            }

        });

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

    public float getPickupX() {
        return Float.parseFloat(pickupXText.getText());
    }

    public float getPickupY() {
        return Float.parseFloat(pickupYText.getText());
    }

    public float getDeliverX() {
        return Float.parseFloat(deliverXText.getText());
    }

    public float getDeliverY() {
        return Float.parseFloat(deliverYText.getText());
    }

    public boolean areAllInputFilled() {
        String pickupX = pickupXText.getText().trim();
        String pickupY = pickupYText.getText().trim();
        String deliverX = deliverXText.getText().trim();
        String deliverY = deliverXText.getText().trim();
        return !(pickupX.equals("") || pickupY.equals("") || deliverX.equals("") || deliverY.equals(""));
    }

    public boolean verifyCoordinateInput() {
        String regex = "^-?[1-9][0-9]*?(\\.[0-9]+?)?$";
        String pickupX = pickupXText.getText().trim();
        String pickupY = pickupYText.getText().trim();
        String deliverX = deliverXText.getText().trim();
        String deliverY = deliverXText.getText().trim();
        return pickupX.matches(regex) && pickupY.matches(regex) && deliverX.matches(regex) && deliverY.matches(regex);
    }
}
