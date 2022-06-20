package AuctioneerUI;

import Agent.CarrierAgent;
import UIResource.UIData;
import UIResource.HTTPResource.HTTPRequests;
import UIResource.scrollbar.ScrollBarCustom;
import javax.swing.*;
import javax.swing.border.Border;

import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import Agent.AuctioneerAgent;
import Auction.Auction;
import Auction.TransportRequest;
import Auction.Bid;
import Auction.VickreyAuction;
import Utils.TourPlanning;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class StartAuctionUI extends JFrame {


    private Color background = UIData.getBackground();
    private Font font = UIData.getFont();
    private int width = UIData.getWidth();
    private int height = UIData.getHeight();
    static final int MAX_T = 5;

    public StartAuctionUI() {

///////////
// Frame
///////////

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setTitle("CCN");
        setSize(width,height);
        setMinimumSize(new Dimension(width,height));
        setLocationRelativeTo(null);

///////////
// Panels
///////////

        JPanel panel = new JPanel();
        panel.setBackground(background);
        panel.setLayout(new GridBagLayout());

        JLabel topLabel = new JLabel("Auction is running.");
        topLabel.setFont(font.deriveFont(Font.BOLD, 14));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 20, 0);
        panel.add(topLabel, constraints);

        JLabel bottomLabel = new JLabel("Window closes automatically.");
        bottomLabel.setFont(font.deriveFont(Font.BOLD, 14));

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 0, 0);
        panel.add(bottomLabel, constraints);

        getContentPane().add(panel);


        pack();

        setResizable(false);


    }

    public void startAuctions() {
        HTTPRequests.resetCost();
        HTTPRequests.stashTransportRequests();
        List<CarrierAgent> bidders = HTTPRequests.getCarrierAgents();
        if (bidders!=null && !bidders.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                List<Auction> auctions = HTTPRequests.getAllAuctions();
                if (auctions!=null && !auctions.isEmpty()) {
                    for (Auction auction : auctions) {
                        auction.setAuctionStrategy(new VickreyAuction());
                        auction.start();
                        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
                        for (CarrierAgent bidder : bidders) {
                            Runnable r = new Task(bidder, auction);
                            pool.execute(r);
                        }
                        pool.shutdown();
                        auction.end();
                        Bid winningBid = auction.getWinningBid();
                        if (winningBid == null) {
                            auction.notifyWinner();
                        }
                    }
                }
            }
        }
        HTTPRequests.resetAuction();
        this.dispose();
        HTTPRequests.logout();
    }


}

class Task implements Runnable
{
    private CarrierAgent carrier;
    private TransportRequest transReq;
    private Auction auction;

    public Task(CarrierAgent agent, Auction a)
    {
        this.carrier = agent;
        this.transReq = a.getDefaultTransportRequest();
        this.auction = a;
    }

    // Prints task name and sleeps for 1s
    // This Whole process is repeated 5 times
    public void run()
    {
        TourPlanning tour = new TourPlanning(carrier);
        double price = tour.getProfit(transReq)-1000.00;
        if (price>=0) {
            Bid bid = HTTPRequests.addBid(auction, carrier, price);
            auction.addBid(bid);
        }
        System.out.println("Tour planning for carrier "+carrier.getUsername()+" and request " + transReq.getRouteString() +" completed");
    }
}
