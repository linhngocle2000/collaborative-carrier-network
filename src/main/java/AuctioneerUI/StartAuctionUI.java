package AuctioneerUI;

import Agent.CarrierAgent;
import UIResource.UIData;
import UIResource.HTTPResource.HTTPRequests;

import javax.swing.*;

import Auction.Auction;
import Auction.TransportRequest;
import Auction.Bid;
import Auction.BundleHelper;
import Auction.VickreyAuction;
import Utils.TourPlanning;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class StartAuctionUI extends JFrame {

    private Color background = UIData.getBackground();
    private Font font = UIData.getFont();
    private int width = UIData.getWidth();
    private int height = UIData.getHeight();
    static final int MAX_T = Runtime.getRuntime().availableProcessors() + 1;
    private int iter = 3;
    private List<CarrierAgent> bidders;

    private static Logger LOGGER = LoggerFactory.getLogger(StartAuctionUI.class);

    public StartAuctionUI() {
        try {
            bidders = HTTPRequests.getCarrierAgents();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }

        ///////////
        // Frame
        ///////////

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setTitle("CCN");
        setSize(width, height);
        setMinimumSize(new Dimension(width, height));
        setLocationRelativeTo(null);

        ///////////
        // Panels
        ///////////

        JPanel panel = new JPanel();
        panel.setBackground(background);
        panel.setLayout(new GridBagLayout());

        JLabel topLabel = new JLabel("Auction is running.");
        topLabel.setFont(font.deriveFont(Font.BOLD, 14));
        topLabel.setVisible(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 20, 0);
        panel.add(topLabel, constraints);

        JLabel bottomLabel = new JLabel("Window closes automatically.");
        bottomLabel.setFont(font.deriveFont(Font.BOLD, 14));
        bottomLabel.setVisible(false);

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 0, 0);
        panel.add(bottomLabel, constraints);

        JButton button = new JButton();
        button.setText("Start auction");
        button.addActionListener(e -> {
            topLabel.setVisible(true);
            bottomLabel.setVisible(true);
            button.setEnabled(false);
            bundleAuction();        
        });

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(0, 0, 0, 0);
        panel.add(button, constraints);

        getContentPane().add(panel);
        pack();
        setResizable(false);
    }

    public void checkCarrierList() {
        if (bidders == null || bidders.isEmpty()) {
            LOGGER.info("No carrier available");
            this.dispose();
            HTTPRequests.logout();
            System.exit(0);
        }
    }

    public void bundleAuction() {
        try {
            checkCarrierList();
            HTTPRequests.resetCost();
            HTTPRequests.stashTransportRequests();

            // Add all transport requests to the same root auction
            Auction rootAuction = HTTPRequests.addAuction();
            ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
            for (CarrierAgent carrier : bidders) {
                pool.submit(new BundleAuctionTask(rootAuction, carrier));
            }
            pool.shutdown();
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Without this call, the executor service may not finish all auction tasks!

            // Generate bundles
            List<TransportRequest> elementaryRequests = HTTPRequests.getTransportRequestsOfAuction(rootAuction);
            BundleHelper helper = new BundleHelper(bidders, elementaryRequests);
            var bundles = helper.generateBundles();

            // Create separate auction for each bundle
            ArrayList<Auction> auctions = new ArrayList<>();
            for (var bundle : bundles) {
                Auction bundleAuction = HTTPRequests.addAuction();
                for (TransportRequest request : bundle) {
                    HTTPRequests.addTransportRequestToAuction(bundleAuction, request);
                }
                bundleAuction.setTransportRequests(bundle);
                bundleAuction.setAuctionStrategy(new VickreyAuction());
                auctions.add(bundleAuction);
                LOGGER.info("Auction " + bundleAuction.getID() + " for " +
                        bundleAuction.getTransportRequestRoutes() + " generated");
            }

            // Delete root auction
            HTTPRequests.resetAuction(rootAuction);

            // Generate bids
            pool = Executors.newFixedThreadPool(MAX_T);
            for (var bidder : bidders) {
                for (var auction : auctions) {
                    pool.submit(new BundleBidTask(bidder, auction));        
                }
            }
            pool.shutdown();
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Without this call, the executor service may not finish all auction tasks!

            // Decide who wins
            List<Auction> winningAuctions = helper.decisionMaking(auctions);

            // Transfer ownership of transport requests
            for (Auction auction : winningAuctions) {
                auction.notifyWinner();
                LOGGER.info("Winner for auction " + auction.getID() + " is " + auction.getWinningBid().getBidder().getUsername());
            }

            // Reset auction table
            HTTPRequests.resetAuction(null);
            LOGGER.info("Auction process ended");
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }

        HTTPRequests.logout();
        System.exit(0);
    }

}

class BundleBidTask implements Runnable {
    private CarrierAgent carrier;
    private Auction auction;

    private static Logger LOGGER = LoggerFactory.getLogger(BundleBidTask.class);

    public BundleBidTask(CarrierAgent carrier, Auction auction) {
        this.carrier = carrier;
        this.auction = auction;
    }

    public void run() {
        try {
            TourPlanning tour = new TourPlanning(carrier);
            double profit = 0;

            for (var request : auction.getTransportRequests()) {
                if (tour.contains(request)) {
                    return;
                }
                tour.addRequest(request);
                profit += tour.getProfit(request);
            }
            
            if (profit >= carrier.getMinProfit()) {
                double price = profit - carrier.getMinProfit();
                Bid bid = HTTPRequests.addBid(auction, carrier, price);
                assert bid != null;
                auction.addBid(bid);
                LOGGER.info(carrier.getUsername() + " bids " + price + " on auction " + auction.getID());
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
}

class BundleAuctionTask implements Runnable {
    private Auction auction;
    private CarrierAgent carrier;

    private static Logger LOGGER = LoggerFactory.getLogger(BundleAuctionTask.class);

    public BundleAuctionTask(Auction auction, CarrierAgent carrier) {
        this.auction = auction;
        this.carrier = carrier;
    }

    public void run() {
        try {
            TourPlanning tour = new TourPlanning(carrier);
            for (TransportRequest tr : tour.getRequests()) {
                if (tour.getProfit(tr) < carrier.getMaxProfit()) {
                    HTTPRequests.addTransportRequestToAuction(auction, tr);
                    LOGGER.info(carrier.getUsername() + " auctions off request " + tr.getID() + ": " + tr.getRouteString());
                    System.out.println("Request " + tr.getRouteString() + " sent to auction.");
                }
            }
            LOGGER.info("Requests of carrier " + carrier.getUsername() + " checked.");
            System.out.println("Requests of carrier " + carrier.getUsername() + " checked.");
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
