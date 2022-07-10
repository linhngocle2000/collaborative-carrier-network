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
import java.io.IOException;
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
            LOGGER.error("Exception :: " , e);
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

        JLabel topLabel = new JLabel("Auction started.");
        topLabel.setFont(font.deriveFont(Font.BOLD, 14));
        topLabel.setVisible(false);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panel.add(topLabel, constraints);

        JButton button = new JButton();
        button.setFocusPainted(false);
        button.setText("Start auction");
        button.addActionListener(e -> {
            topLabel.setVisible(true);
            button.setEnabled(false);
            try {
                bundleAuction();
            } catch (IOException | InterruptedException ex) {
                LOGGER.error("Exception :: ", ex);
            }
        });

        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
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

    public void bundleAuction() throws IOException, InterruptedException {
        List<TransportRequest> unsoldList;

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
            assert rootAuction != null;
            List<TransportRequest> elementaryRequests = HTTPRequests.getTransportRequestsOfAuction(rootAuction);
            BundleHelper helper = new BundleHelper(bidders, elementaryRequests);
            var bundles = helper.generateBundles();

            // Create separate auction for each bundle
            ArrayList<Auction> auctions = new ArrayList<>();
            for (var bundle : bundles) {
                Auction bundleAuction = HTTPRequests.addAuction();
                for (TransportRequest request : bundle) {
                    assert bundleAuction != null;
                    HTTPRequests.addTransportRequestToAuction(bundleAuction, request);
                }
                assert bundleAuction != null;
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

            unsoldList = helper.getUnsoldList();

            for (TransportRequest t : unsoldList) {
                Auction auction = HTTPRequests.addAuction();
                assert auction != null;
                HTTPRequests.addTransportRequestToAuction(auction, t);
                LOGGER.info("Request " + t.getRouteString() + " is auctioned off.");
            }

            for (int i = 0; i < iter; i++) {
                LOGGER.info("Unsold auctions round: " + (i + 1));
                List<Auction> unsoldListAuctions = HTTPRequests.getAllAuctions();
                if (unsoldListAuctions == null || unsoldListAuctions.isEmpty()) {
                    break;
                } else {
                    for (Auction auction : unsoldListAuctions) {
                        auction.setAuctionStrategy(new VickreyAuction());
                        LOGGER.info("Auction for " + auction.getDefaultTransportRequest().getRouteString() + " started");
                        ExecutorService unsoldListPool = Executors.newFixedThreadPool(MAX_T);
                        for (CarrierAgent bidder : bidders) {
                            Runnable r = new SingleBidTask(bidder, auction);
                            unsoldListPool.submit(r); // Use submit instead of execute
                        }
                        unsoldListPool.shutdown();
                        unsoldListPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Without this call, the executor service may not finish all bid tasks!
                        LOGGER.info("Auction for " + auction.getDefaultTransportRequest().getRouteString() + " terminated");
                        LOGGER.info("Winner for auction " + auction.getDefaultTransportRequest().getRouteString() + " is " + auction.getWinningBid().getBidder().getUsername());
                        auction.notifyWinner();
                    }
                }
            }

            // Reset auction table
            HTTPRequests.resetAuction(null);
            this.dispose();
            LOGGER.info("Auction process ended");
        } catch (Exception e) {
            LOGGER.error("Exception :: " , e);
            HTTPRequests.resetAuction(null);
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
            int bundleSize = auction.getTransportRequests().size();

            tour.addRequests(auction.getTransportRequests());
            for (var request : auction.getTransportRequests()) {
                profit += tour.getProfit(request);
            }
            LOGGER.info(carrier.getUsername() + " profits " + profit + " from bundle " + auction.getTransportRequestRoutes());
            if ((profit / bundleSize) >= carrier.getMinProfit()) {
                double price = profit - (carrier.getMinProfit() * bundleSize);
                Bid bid = new Bid(auction, carrier, price);
                auction.addBid(bid);
                LOGGER.info(carrier.getUsername() + " bids " + price + " on auction " + auction.getID());
            }
        } catch (Exception e) {
            LOGGER.error("Exception :: " , e);
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
                }
            }
            LOGGER.info("Requests of carrier " + carrier.getUsername() + " checked.");
        } catch (Exception e) {
            LOGGER.error("Exception :: " , e);
        }
    }
}

class SingleBidTask implements Runnable {
    private CarrierAgent carrier;
    private TransportRequest transReq;
    private Auction auction;

    private static Logger LOGGER = LoggerFactory.getLogger(SingleBidTask.class);

    public SingleBidTask(CarrierAgent agent, Auction a) {
        this.carrier = agent;
        this.transReq = a.getDefaultTransportRequest();
        this.auction = a;
    }

    public void run() {
        try {
            TourPlanning tour = new TourPlanning(carrier);
            if (!tour.getRequestIDs().contains(Integer.parseInt(transReq.getID()))) {
                tour.addRequest(transReq);
                double profit = Math.round(tour.getProfit(transReq) * 100.0) / 100.0;
                LOGGER.info(carrier.getUsername() + " profits " + profit + " from " + transReq.getRouteString());
                if (profit >= carrier.getMinProfit()) {
                    Bid bid = new Bid(auction, carrier, profit-carrier.getMinProfit());
                    auction.addBid(bid);
                    LOGGER.info(carrier.getUsername() + " bids for request " + transReq.getRouteString() + " with "
                            + bid.getBidPrice());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception :: " , e);
        }

    }
}

