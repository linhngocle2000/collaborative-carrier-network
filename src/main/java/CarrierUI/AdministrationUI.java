package CarrierUI;

import Utils.TourPlanning;
import UIResource.TableData;
import UIResource.UIData;
import UIResource.scrollbar.ScrollBarCustom;
import UIResource.TextIcon;
import Agent.Agent;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;

import UIResource.Utils;
import com.graphhopper.jsprit.core.problem.Location;
import org.openide.awt.*;

public class AdministrationUI extends JFrame {

    private Color background = UIData.getBackground();
    private Font font = UIData.getFont();
    private Color errorColor = UIData.getErrorColor();
    private Border emptyBorder = UIData.getEmptyBorder();

    Object[][] data = {
            {"((0,1),(2,3))", "1000"},
            {"((4,5),(8,9))", "1000"},
            {"((32,5),(13,53))", "1000"},
            {"((76,32),(-87,34))", "1000"},
            {"((76,22),(4,93))", "1000"},
            {"((62,13),(76,23))", "1000"},
            {"((20,46),(78,23))", "1000"},
            {"((10,3),(82,6))", "1000"},
            {"((9,34),(29,98))", "1000"},
            {"((14,97),(2,41))", "1000"},
            {"((83,37),(25,6))", "1000"},
            {"((94,87),(85,45))", "1000"},
            {"((43,1),(36,64))", "1000"}
    };

    public AdministrationUI(Agent user) {

        super();

        setTitle("CCN");
        setLocationRelativeTo(null);

        setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel leftPanel = new JPanel();
        JPanel leftBottomPanel = new JPanel();
        leftBottomPanel.setBackground(background);
        leftPanel.setBackground(background);
        leftPanel.setLayout(new GridBagLayout());
        JPanel rightPanel = new JPanel();

        leftPanel.setMinimumSize(new Dimension(400, 540));
        leftPanel.setPreferredSize(new Dimension(400, 540));

        TitledBorder leftVisualTitle = new TitledBorder("Panel 1");
        leftVisualTitle.setTitleJustification(TitledBorder.CENTER);
        leftVisualTitle.setTitlePosition(TitledBorder.TOP);

        TitledBorder rightVisualTitle = new TitledBorder("Panel 2");
        rightVisualTitle.setTitleJustification(TitledBorder.CENTER);
        rightVisualTitle.setTitlePosition(TitledBorder.TOP);

        JPanel leftVisualPanel = new JPanel();
        leftVisualPanel.setBorder(leftVisualTitle);
        leftVisualPanel.setMinimumSize(new Dimension(600,650));
        leftVisualPanel.setPreferredSize(new Dimension(600,650));

        JPanel rightVisualPanel = new JPanel();
        rightVisualPanel.setBorder(rightVisualTitle);
        rightVisualPanel.setMinimumSize(new Dimension(600,650));
        rightVisualPanel.setPreferredSize(new Dimension(600,650));

        rightPanel.add(leftVisualPanel);
        rightPanel.add(rightVisualPanel);

        JLabel tableHeader = new JLabel("Transport requests");
        tableHeader.setFont(font.deriveFont(Font.BOLD, 16));
        tableHeader.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 30, 0);
        leftPanel.add(tableHeader, constraints);

        JTable table = new JTable(data, TableData.getRequestColumnNames()) {

            public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
            {
                Component c = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent)c;

                // Add a border to the selected row

                if (isRowSelected(row)) {
                    jc.setBorder(emptyBorder);
                }

                return c;
            }
        };
        table.setShowHorizontalLines(false);

        table.setRowHeight(25);
        table.setIntercellSpacing(new Dimension(0, 0));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(220);
        columnModel.getColumn(1).setPreferredWidth(130);
        for (int i = 0; i<2; i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
        JScrollPane scrollPane = new JScrollPane(table);
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false);
        table.clearSelection();

        if (data.length <= 12) {
            scrollPane.setPreferredSize(new Dimension(350, data.length*25+23));
        } else {
            scrollPane.setPreferredSize(new Dimension(350, 323));
            scrollPane.setVerticalScrollBar(new ScrollBarCustom(data.length));
        }

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(0, 0, 0, 0);
        leftPanel.add(scrollPane, constraints);

        JButton auctionOff = new JButton("Auction off");
        auctionOff.setFocusPainted(false);
        JButton costCalc = new JButton("Cost calculator");
        costCalc.setFocusPainted(false);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem visualizeInP1 = new JMenuItem("Panel 1");
        popupMenu.add(visualizeInP1);
        visualizeInP1.addActionListener(e -> {
            Location depot = Location.newInstance(23.76, 7.82);
            TourPlanning currentTour = new TourPlanning(depot, "amy");
            int selectedRow[] = table.getSelectedRows();
            float[] tr;
            String requestID;
            for (int i = 0; i < table.getSelectedRowCount(); i++) {
                requestID = Integer.toString(i);
                tr = Utils.convertTransportRequests(table.getValueAt(selectedRow[i],0).toString());
                Location pickup = Location.newInstance(tr[0], tr[1]);
                Location deliver = Location.newInstance(tr[2], tr[3]);
                currentTour.addRequest(requestID, pickup, deliver);
            }

            leftVisualPanel.removeAll();
            leftVisualPanel.add(currentTour.visualize());
            leftVisualPanel.revalidate();


        });
        JMenuItem visualizeInP2 = new JMenuItem("Panel 2");
        popupMenu.add(visualizeInP2);
        visualizeInP2.addActionListener(e -> {
            Location depot = Location.newInstance(23.76, 7.82);
            TourPlanning currentTour = new TourPlanning(depot, "amy");
            int selectedRow[] = table.getSelectedRows();
            float[] tr;
            String requestID;
            for (int i = 0; i < table.getSelectedRowCount(); i++) {
                requestID = Integer.toString(i);
                tr = Utils.convertTransportRequests(table.getValueAt(selectedRow[i],0).toString());
                Location pickup = Location.newInstance(tr[0], tr[1]);
                Location deliver = Location.newInstance(tr[2], tr[3]);
                currentTour.addRequest(requestID, pickup, deliver);
            }

            rightVisualPanel.removeAll();
            rightVisualPanel.add(currentTour.visualize());
            rightVisualPanel.revalidate();


        });

        TextIcon icon = new TextIcon(leftBottomPanel, "Show in", TextIcon.Layout.HORIZONTAL);
        icon.setFont(font.deriveFont(Font.BOLD, 12));;
        JButton showIn = DropDownButtonFactory.createDropDownButton(icon, popupMenu);
        showIn.setFocusPainted(false);

        auctionOff.setPreferredSize(costCalc.getPreferredSize());

        leftBottomPanel.add(auctionOff);
        leftBottomPanel.add(showIn);
        leftBottomPanel.add(costCalc);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new Insets(35, 0, 0, 0);
        leftPanel.add(leftBottomPanel, constraints);


        getContentPane().add(leftPanel, BorderLayout.LINE_START);
        getContentPane().add(rightPanel, BorderLayout.CENTER);

        pack();

        setResizable(false);




    }
}