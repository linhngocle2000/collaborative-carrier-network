package CarrierUI;


import UIResource.UIData;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VisualizationUI extends JFrame {

    private Color background = UIData.getBackground();
    private JPanel leftVisualPanel, rightVisualPanel;

    public VisualizationUI() {

        super();

///////////
// Frame
///////////

        setTitle("CCN");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);


///////////
// Panels
///////////

        JPanel rightPanel = new JPanel();

        TitledBorder leftVisualTitle = new TitledBorder("Before");
        leftVisualTitle.setTitleJustification(TitledBorder.CENTER);
        leftVisualTitle.setTitlePosition(TitledBorder.TOP);

        TitledBorder rightVisualTitle = new TitledBorder("After");
        rightVisualTitle.setTitleJustification(TitledBorder.CENTER);
        rightVisualTitle.setTitlePosition(TitledBorder.TOP);

        leftVisualPanel = new JPanel();
        leftVisualPanel.setBorder(leftVisualTitle);
        leftVisualPanel.setMinimumSize(new Dimension(830, 650));
        leftVisualPanel.setPreferredSize(new Dimension(830, 650));

        rightVisualPanel = new JPanel();
        rightVisualPanel.setBorder(rightVisualTitle);
        rightVisualPanel.setMinimumSize(new Dimension(830, 650));
        rightVisualPanel.setPreferredSize(new Dimension(830, 650));

        rightPanel.add(leftVisualPanel);
        rightPanel.add(rightVisualPanel);

///////////
// Combine
///////////

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
