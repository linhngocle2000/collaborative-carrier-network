package CarrierUI;


import UIResource.UIData;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;

public class VisualizationUI extends JFrame {

    private Color background = UIData.getBackground();
    private JPanel leftVisualPanel, rightVisualPanel;

    public VisualizationUI() {

        super();

        setTitle("CCN");
        setLocationRelativeTo(null);

        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel rightPanel = new JPanel();

        TitledBorder leftVisualTitle = new TitledBorder("Panel 1");
        leftVisualTitle.setTitleJustification(TitledBorder.CENTER);
        leftVisualTitle.setTitlePosition(TitledBorder.TOP);

        TitledBorder rightVisualTitle = new TitledBorder("Panel 2");
        rightVisualTitle.setTitleJustification(TitledBorder.CENTER);
        rightVisualTitle.setTitlePosition(TitledBorder.TOP);

        leftVisualPanel = new JPanel();
        leftVisualPanel.setBorder(leftVisualTitle);
        leftVisualPanel.setMinimumSize(new Dimension(600, 615));
        leftVisualPanel.setPreferredSize(new Dimension(600, 615));

        rightVisualPanel = new JPanel();
        rightVisualPanel.setBorder(rightVisualTitle);
        rightVisualPanel.setMinimumSize(new Dimension(600, 615));
        rightVisualPanel.setPreferredSize(new Dimension(600, 615));

        rightPanel.add(leftVisualPanel);
        rightPanel.add(rightVisualPanel);


        getContentPane().add(rightPanel, BorderLayout.CENTER);

        pack();

        setResizable(false);
    }

    public JPanel getLeftVisualPanel() {
        return leftVisualPanel;
    }

    public JPanel getRightVisualPanel() {
        return rightVisualPanel;
    }


}
