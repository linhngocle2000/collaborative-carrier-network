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

    public void checkCarrierList() {
        if (bidders == null || bidders.isEmpty()) {
            LOGGER.info("No carrier available");
            this.dispose();
            HTTPRequests.logout();
            System.exit(0);
        }
    }

    public void auctionOff() {
        try {
            checkCarrierList();
            ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
            for (CarrierAgent carrier : bidders) {
                Runnable r = new AuctionTask(carrier);
                pool.submit(r); // Use submit instead of execute
            }
            pool.shutdown();
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Without this call, the executor service may
                                                                         // not finish all auction tasks!
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public void bundleAuction() throws Exception {
        checkCarrierList();

        // Add all transport requests to the same root auction
        Auction auction = HTTPRequests.addAuction();
        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
        for (CarrierAgent carrier : bidders) {
            pool.submit(new BundleAuctionTask(auction, carrier));
        }
        pool.shutdown();
        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Without this call, the executor service may not
                                                                     // finish all auction tasks!

        // Generate bundles
        List<TransportRequest> elementaryRequests = HTTPRequests.getTransportRequestsOfAuction(auction);
        BundleHelper helper = new BundleHelper(bidders, elementaryRequests);
        var bundles = helper.generateBundles();

        // Create separate auction for each bundle
        for (var bundle : bundles) {
            Auction bundleAuction = HTTPRequests.addAuction();
            for (TransportRequest request : bundle) {
                HTTPRequests.addTransportRequestToAuction(bundleAuction, request);
            }
        }

        // Delete root auction
        HTTPRequests.resetAuction(auction);

        // Generate bids

        // Decide who wins
    }

    public void startAuctions() {
        try {
            checkCarrierList();
            HTTPRequests.resetCost();
            HTTPRequests.stashTransportRequests();
            for (int i = 0; i < iter; i++) {
                LOGGER.info("Iteration " + i);
                List<Auction> auctions = HTTPRequests.getAllAuctions();
                if (auctions != null && !auctions.isEmpty()) {
                    for (Auction auction : auctions) {
                        auction.setAuctionStrategy(new VickreyAuction());
                        auction.start();
                        LOGGER.info(
                                "Auction for " + auction.getDefaultTransportRequest().getRouteString() + " started");
                        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
                        for (CarrierAgent bidder : bidders) {
                            Runnable r = new BidTask(bidder, auction);
                            pool.submit(r); // Use submit instead of execute
                        }
                        pool.shutdown();
                        pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Without this call, the executor
                                                                                     // service may not finish all bid
                                                                                     // tasks!
                        auction.end();
                        LOGGER.info(
                                "Auction for " + auction.getDefaultTransportRequest().getRouteString() + " terminated");
                        List<Bid> bids = HTTPRequests.getBids(auction);
                        if (bids != null) {
                            for (Bid bid : bids) {
                                auction.addBid(bid);
                            }
                            LOGGER.info("Winner for auction " + auction.getDefaultTransportRequest().getRouteString()
                                    + " is " + auction.getWinningBid().getBidder().getUsername());
                            auction.notifyWinner();
                        }
                    }
                }
            }
            HTTPRequests.resetAuction(null);
            this.dispose();
            HTTPRequests.logout();
            LOGGER.info("Auction process ended");
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
        System.exit(0);
    }

}

class BidTask implements Runnable {
    private CarrierAgent carrier;
    private TransportRequest transReq;
    private Auction auction;

    private static Logger LOGGER = LoggerFactory.getLogger(BidTask.class);

    public BidTask(CarrierAgent agent, Auction a) {
        this.carrier = agent;
        this.transReq = a.getDefaultTransportRequest();
        this.auction = a;
    }

    public void run() {
        try {
            TourPlanning tour = new TourPlanning(carrier);
            if (!tour.getRequestIDs().contains(Integer.parseInt(transReq.getID()))) {
                tour.addRequest(transReq);
                double price = tour.getProfit(transReq) - 1;
                price = Math.round(price * 100.0) / 100.0;
                LOGGER.info(carrier.getUsername() + " profits " + price + " from " + transReq.getRouteString());
                if (price >= 0) {
                    Bid bid = HTTPRequests.addBid(auction, carrier, price);
                    assert bid != null;
                    LOGGER.info(carrier.getUsername() + " bids for request " + transReq.getRouteString() + " with " + bid.getBidPrice());
                }
            }
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }

    }
}

class AuctionTask implements Runnable {
    private CarrierAgent carrier;

    private static Logger LOGGER = LoggerFactory.getLogger(AuctionTask.class);

    public AuctionTask(CarrierAgent agent) {
        this.carrier = agent;
    }

    public void run() {
        try {
            TourPlanning tour = new TourPlanning(carrier);
            for (TransportRequest tr : tour.getRequests()) {
                if (tour.getProfit(tr) <= 0) {
                    Auction auction = HTTPRequests.addAuction();
                    HTTPRequests.addTransportRequestToAuction(auction, tr);
                    LOGGER.info("Request " + tr.getRouteString() + " sent to auction.");
                }
            }
            LOGGER.info("Requests of carrier " + carrier.getUsername() + " checked.");
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
}

class BundleAuctionTask implements Runnable {
    private Auction auction;
    private CarrierAgent carrier;

    private static Logger LOGGER = LoggerFactory.getLogger(AuctionTask.class);

    public BundleAuctionTask(Auction auction, CarrierAgent carrier) {
        this.auction = auction;
        this.carrier = carrier;
    }

    public void run() {
        try {
            TourPlanning tour = new TourPlanning(carrier);
            for (TransportRequest tr : tour.getRequests()) {
                if (tour.getProfit(tr) <= 0) {
                    HTTPRequests.addTransportRequestToAuction(auction, tr);
                    System.out.println("Request " + tr.getRouteString() + " sent to auction.");
                }
            }
            System.out.println("Requests of carrier " + carrier.getUsername() + " checked.");
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
